package com.legend.liteim.data;

import com.legend.im.bean.Msg;
import com.legend.im.bean.MsgModel;
import com.legend.im.client.IMClient;
import com.legend.im.protoctol.command.Command;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.ChatMsgVO;
import com.legend.liteim.bean.Message;
import com.legend.liteim.bean.Record;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.MyApplication;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.ActivityManager;
import com.legend.liteim.common.utils.DataUtils;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.common.utils.NetworkUtil;
import com.legend.liteim.db.GroupHelper;
import com.legend.liteim.db.MessageHelper;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.event.MsgEvent;
import com.legend.liteim.event.RefreshEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.model.ExtraDisposableManager;
import com.legend.liteim.ui.chat.ChatActivity;

import net.qiujuer.genius.kit.handler.Run;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.legend.im.common.MsgType.TO_GROUP;
import static com.legend.im.common.MsgType.TO_USER;
import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-22.
 * @description 消息处理器 包括消息的发送与离线消息的拉取
 */
public class MsgProcessor {

    private IMClient client = IMClient.getInstance();
    private GlobalData globalData = GlobalData.getInstance();
    private MessageHelper messageHelper = MessageHelper.getInstance();
    private ExtraDisposableManager manager = ExtraDisposableManager.getInstance();

    public static MsgProcessor getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static MsgProcessor INSTANCE = new MsgProcessor();
    }


    public void sendMsg(MsgModel model) {
        Run.onBackground(() -> {
            Blocker.await(model.getFlag());
            if (!NetworkUtil.checkNetState(MyApplication.getInstance())) {
                Run.onUiAsync(() -> DialogUtils.showToast(MyApplication.getInstance(), "当前网络不可用..."));
            }
            filter(model);
            client.sendMsg(model);
        });
    }

    /**
     * 异常状态下直接改变消息状态
     */
    private void filter(MsgModel model) {
        if (client.isDestroyed() && client.getMsgListener() != null) {
            if (model.getCommand() == Command.USER_MSG_REQUEST) {
                client.getMsgListener().sendUserMsgFailure(model.buildMsg());
            } else if (model.getCommand() == Command.GROUP_MSG_REQUEST) {
                client.getMsgListener().sendGroupMsgFailure(model.buildMsg());
            }
        }
    }

    /**
     * 拉取所有离线消息
     */
    public void pullAllOfflineMsg() {
        LogUtils.logD(this, "上线后主动拉取用户列表~~~");
        pullFriendList();
        LogUtils.logD(this, "上线后主动拉取群组列表~~~");
        pullGroupList();

        // 用户消息和群组消息分别在不同的线程，这样即使不停的有用户消息拉取或者
        // 阻塞获取是否有未签收的消息时 不影响群组消息的拉取
        LogUtils.logD(this, "上线后主动拉取用户离线消息~~~");
        Executors.newSingleThreadExecutor().execute(this::pullAllUserOfflineMsg);
        LogUtils.logD(this, "上线后主动拉取群组离线消息~~~");
        Executors.newSingleThreadExecutor().execute(this::pullAllGroupOfflineMsg);
    }

    /**
     * 拉取用户列表
     */
    private void pullFriendList() {
        manager.addDisposable(NetworkService.getInstance().getFriendList(globalData.getJWT(), 1, 30)
                .subscribeWith(new BaseObserver<ResultVo<Record<User>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<User>> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            Record<User> record = data.getData();
                            UserHelper.getInstance().saveOrUpdateAll(record.getRecords());
                            RxBus.getDefault().post(new RefreshEvent());
                        }
                    }
                }));
    }

    /**
     * 拉取群组列表
     */
    private void pullGroupList() {
        manager.addDisposable(NetworkService.getInstance().getJoinedGroupList(globalData.getJWT(), 1, 30)
                .subscribeWith(new BaseObserver<ResultVo<Record<ChatGroup>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<ChatGroup>> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            Record<ChatGroup> record = data.getData();
                            GroupHelper.getInstance().saveOrUpdateAll(record.getRecords());
                            RxBus.getDefault().post(new RefreshEvent());
                        }
                    }
                }));
    }

    /**
     * 拉取所有用户离线消息
     */
    private void pullAllUserOfflineMsg() {
        String flag = DataUtils.getUUID();
        Blocker.put(flag);
        manager.addDisposable(NetworkService.getInstance().getAllUserOfflineMsgList(globalData.getJWT())
                .map((Function<ResultVo<List<ChatMsgVO>>, List<ChatMsgVO>>) resultVo -> {
                    if (SUCCESS.equals(resultVo.getCode())) {
                        return resultVo.getData();
                    }
                    return new ArrayList<>();
                })
                .flatMap((Function<List<ChatMsgVO>, ObservableSource<ChatMsgVO>>) Observable::fromIterable)
                .map(chatMsgVO -> {
                    List<Msg> msgList = chatMsgVO.getMsgList();
                    List<Message> messages = new ArrayList<>();
                    for (Msg msg : msgList) {
                        Message message  = Message.buildMsg(msg);
                        message.setType(TO_USER);
                        messages.add(message);
                        // 回送消息已经签收的包
                        MsgModel model = MsgModel.builder()
                                .fromId(msg.getMsgId()).contentType(TO_USER)
                                .command(Command.MSG_ACK_REQUEST).build();
                        IMClient.getInstance().sendMsg(model);
                        // 如果当前是在聊天界面,则发送消息到事件总线
                        if (ActivityManager.getInstance().currentActivity() != null
                                && ActivityManager.getInstance().currentActivity()
                                .getClass() == ChatActivity.class) {
                            MsgEvent event = new MsgEvent(message, 0);
                            LogUtils.logD(this, "当前是在聊天界面，直接发送消息到事件总线..........");
                            RxBus.getDefault().post(event);
                        }
                    }
                    return messages;
                })
                .subscribeWith(new BaseObserver<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> data) {
                        if (data.size() > 0) {
                            messageHelper.saveOrUpdateAll(data);
                            // 直接拿消息的最后一条并且携带当前总的数量去通知session进行更新
                            Message message = data.get(data.size() - 1);
                            MsgEvent event = new MsgEvent(message, data.size());
                            // 看当前是否在聊天界面
                            boolean isNotOnChat = ActivityManager.getInstance().currentActivity() == null
                                    || ActivityManager.getInstance().currentActivity()
                                    .getClass() != ChatActivity.class;
                            if (isNotOnChat
                                    || GlobalData.getInstance().getChatType() != TO_USER
                                    || GlobalData.getInstance().getChatReceiverId() != message.getFromId()) {
                                // 如果消息是来自当前聊天的用户,就无需再发送session刷新通知
                                // 因为消息界面监听事件总线后有消息自己会发起更新；这里再发送
                                // 就会导致最后接收到两条消息
                                LogUtils.logD(this, "发送了用户session通知:------------"+ message.getFromId());
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                RxBus.getDefault().postSticky(event);
                            }
                        }
                        Blocker.count(flag);
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        Blocker.count(flag);
                    }
                }));
        // 拉取未完成阻塞在这里 防止重复拉取
        Blocker.await(flag);
        manager.addDisposable(NetworkService.getInstance().isExistUserOfflineMsg(globalData.getJWT())
                .subscribeWith(new BaseObserver<ResultVo<Boolean>>() {

                    @Override
                    public void onSuccess(ResultVo<Boolean> data) {
                        if (data.getData()) {
                            // 如果还存在用户离线消息, 则继续递归拉取
                            pullAllUserOfflineMsg();
                        }
                    }
                }));
    }

    /**
     * 拉取所有群离线消息
     */
    private void pullAllGroupOfflineMsg() {
        String flag = DataUtils.getUUID();
        Blocker.put(flag);
        manager.addDisposable(NetworkService.getInstance().getAllGroupOfflineMsgList(globalData.getJWT())
                .map((Function<ResultVo<List<ChatMsgVO>>, List<ChatMsgVO>>) resultVo -> {
                    if (SUCCESS.equals(resultVo.getCode())) {
                        return resultVo.getData();
                    }
                    return new ArrayList<>();
                })
                .flatMap((Function<List<ChatMsgVO>, ObservableSource<ChatMsgVO>>) Observable::fromIterable)
                .map(chatMsgVO -> {
                    List<Msg> msgList = chatMsgVO.getMsgList();
                    List<Message> messages = new ArrayList<>();
                    for (Msg msg : msgList) {
                        Message message  = Message.buildMsg(msg);
                        message.setType(TO_GROUP);
                        messages.add(message);
                        // 如果当前是在聊天界面,则发送消息到事件总线
                        if (ActivityManager.getInstance().currentActivity() != null
                                && ActivityManager.getInstance().currentActivity()
                                .getClass() == ChatActivity.class) {
                            MsgEvent event = new MsgEvent(message, 0);
                            LogUtils.logD(this, "当前是在聊天界面，直接发送消息到事件总线..........");
                            RxBus.getDefault().post(event);
                        }
                    }
                    // 回送消息已经签收的包
                    if (msgList.size() > 0) {
                        MsgModel model = MsgModel.builder()
                                .fromId(msgList.get(msgList.size() - 1).getMsgId()).contentType(TO_GROUP)
                                .command(Command.MSG_ACK_REQUEST).build();
                        IMClient.getInstance().sendMsg(model);
                    }
                    return messages;
                })
                .subscribeWith(new BaseObserver<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> data) {
                        if (data.size() > 0) {
                            messageHelper.saveOrUpdateAll(data);
                            // 直接拿消息的最后一条并且携带当前总的数量去通知session进行更新
                            Message message = data.get(data.size() - 1);
                            MsgEvent event = new MsgEvent(message, data.size());
                            // 看当前是否在聊天界面
                            boolean isNotOnChat = ActivityManager.getInstance().currentActivity() == null
                                    || ActivityManager.getInstance().currentActivity()
                                    .getClass() != ChatActivity.class;
                            if (isNotOnChat || GlobalData.getInstance().getChatType() != TO_GROUP
                                    || GlobalData.getInstance().getChatReceiverId() != message.getToId()) {
                                // 如果消息是来自当前聊天的用户,就无需再发送session刷新通知
                                // 因为消息界面监听事件总线后有消息自己会发起更新；这里再发送
                                // 就会导致最后接收到两条消息
                                LogUtils.logD(this, "发送了用户session通知:------------"+ message.getFromId());
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                RxBus.getDefault().postSticky(event);
                            }
                        }
                        Blocker.count(flag);
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        Blocker.count(flag);
                    }
                }));

        // 拉取未完成阻塞在这里 防止重复拉取
        Blocker.await(flag);
        manager.addDisposable(NetworkService.getInstance().isExistGroupOfflineMsg(globalData.getJWT())
                .subscribeWith(new BaseObserver<ResultVo<Boolean>>() {
                    @Override
                    public void onSuccess(ResultVo<Boolean> data) {
                        if (data.getData()) {
                            // 如果还存在用户离线消息, 则继续递归拉取
                            pullAllGroupOfflineMsg();
                        }
                    }
                }));
    }
}
