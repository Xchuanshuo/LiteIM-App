package com.legend.liteim.presenter.chat;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.legend.im.bean.MsgModel;
import com.legend.im.common.MsgType;
import com.legend.liteim.bean.Message;
import com.legend.liteim.bean.Session;
import com.legend.liteim.common.base.mvp.MyApplication;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.FinderApiService;
import com.legend.liteim.common.net.FormHelper;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DataUtils;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.contract.chat.ChatContract;
import com.legend.liteim.data.Blocker;
import com.legend.liteim.data.MsgProcessor;
import com.legend.liteim.db.MessageHelper;
import com.legend.liteim.db.SessionHelper;
import com.legend.liteim.event.RxBus;

import net.qiujuer.genius.kit.handler.Run;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

import static com.legend.im.common.MsgType.CONTENT_AUDIO;
import static com.legend.im.common.MsgType.CONTENT_PICTURE;
import static com.legend.im.common.MsgType.CONTENT_TEXT;
import static com.legend.im.common.MsgType.TO_GROUP;
import static com.legend.im.common.MsgType.TO_USER;
import static com.legend.im.protoctol.command.Command.GROUP_MSG_REQUEST;
import static com.legend.im.protoctol.command.Command.USER_MSG_REQUEST;
import static com.legend.liteim.bean.Message.STATUS_FAILURE;
import static com.legend.liteim.bean.Message.STATUS_SUCCESS;
import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-12.
 * @description
 */
public abstract class ChatPresenter extends BasePresenter<ChatContract.View>
        implements ChatContract.Presenter {

    /** 接收者Id，可能是群，或者人的ID */
    protected Long mReceiverId;
    /** 区分是人还是群Id */
    protected int mReceiverType;
    private MsgProcessor processor = MsgProcessor.getInstance();
    private MessageHelper messageHelper = MessageHelper.getInstance();
    private SessionHelper sessionHelper = SessionHelper.getInstance();


    ChatPresenter(ChatContract.View view,
                  Long receiverId, int receiverType) {
        super(view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
        registerMsgEvent();
    }

    private void registerMsgEvent() {
        addDisposable(RxBus.getDefault().toObservable(Message.class)
                .subscribe(message -> {
                    // 内存中的更新
                    int status = message.getStatus();
                    if (STATUS_SUCCESS == status || STATUS_FAILURE == status) {
                        // 更新发送成功或者失败的状态
                        ArrayMap<String, Integer> map = messageHelper.getFlagToPosMap();
                        if (map.containsKey(message.getMsgId())) {
                            Run.onUiAsync(() -> mView.updateSendState(message, map.get(message.getMsgId())));
                        }
                    } else {
                        LogUtils.logD(this, "receiveId: " + mReceiverId);
                        if (message.getType() == TO_USER
                                && message.getFromId().equals(mReceiverId)) {
                            // 当前就在聊天页面的话 直接将未读消息置为0
                            Session session = sessionHelper.getSession(mReceiverId, MsgType.TO_USER);
                            sessionHelper.updateUnReadCountAndNotify(session, 0);
                            // 接收的用户消息fromId必须是当前聊天的对象
                            receiveUserMessage(message);
                        } else if (message.getType() == TO_GROUP
                                && message.getToId().equals(mReceiverId)){
                            Session session = sessionHelper.getSession(mReceiverId, MsgType.TO_GROUP);
                            // 当前就在聊天页面的话 直接将未读消息置为0
                            sessionHelper.updateUnReadCountAndNotify(session, 0);
                            // 接收的群组消息toId必须是当前的群组
                            receiveGroupMessage(message);
                        }
                    }
                }));

    }

    @Override
    public void sendText(String content) {
        MsgModel model = getMsgModel(content, CONTENT_TEXT);
        // 1.先直接将消息添加到聊天列表, 在界面显示
        Message newMsg = Message.buildMsg(model.buildMsg());
        newMsg.setMsgId(newMsg.getFlag());
        Blocker.put(newMsg.getMsgId());
        newMsg.setType(mReceiverType);
        newMsg.setStatus(Message.STATUS_CREATED);
        CommonMultiBean<Message> bean = new CommonMultiBean<>(newMsg, newMsg.getContentType());
        mView.showNewMessage(bean);

        // 2.存储到本地数据库
        messageHelper.saveOrUpdate(newMsg);

        // 3.此时才是发送给服务器
        processor.sendMsg(model);
    }

    @Override
    public void sendPictures(String[] paths) {
        for (String path : paths) {
            // 1.直接添加到聊天列表,此时路径是本地路径
            MsgModel model = getMsgModel(path, CONTENT_PICTURE);
            Message newMsg = Message.buildMsg(model.buildMsg());
            newMsg.setMsgId(newMsg.getFlag());
            Blocker.put(newMsg.getMsgId());
            newMsg.setType(mReceiverType);
            newMsg.setStatus(Message.STATUS_CREATED);
            CommonMultiBean<Message> bean = new CommonMultiBean<>(newMsg, newMsg.getContentType());
            Run.onUiSync(()-> mView.showNewMessage(bean));

            // 2.存储到本地数据库
            messageHelper.saveOrUpdate(newMsg);

            // 3.发送给服务器
            sendFile(model, newMsg, path);
        }
    }

    @Override
    public void sendAudio(String path, long time) {
        MsgModel model = getMsgModel(path, CONTENT_AUDIO);
        model.setAttach(String.valueOf(time));
        // 1.先直接将消息添加到聊天列表, 在界面显示
        Message newMsg = Message.buildMsg(model.buildMsg());
        newMsg.setMsgId(newMsg.getFlag());
        Blocker.put(newMsg.getMsgId());
        newMsg.setType(mReceiverType);
        newMsg.setStatus(Message.STATUS_CREATED);

        CommonMultiBean<Message> bean = new CommonMultiBean<>(newMsg, newMsg.getContentType());
        Run.onUiSync(()-> mView.showNewMessage(bean));

        // 2.存储到本地数据库
        messageHelper.saveOrUpdate(newMsg);

        // 3.上传给服务器 并回调后将消息内容替换服务器的url
        sendFile(model, newMsg, path);

    }

    @Override
    public void onResend(Message message) {
        MsgModel model = getMsgModel(message.getMsg(), message.getContentType());
        model.setAttach(message.getAttach());
        // 替换为已经存在的flag标志位
        model.setFlag(message.getMsgId());
        switch (message.getContentType()) {
            case CONTENT_TEXT:
                processor.sendMsg(model);
                break;
            case CONTENT_PICTURE:
            case CONTENT_AUDIO:
                LogUtils.logD(this, "path: " + model.getContent());
                if (TextUtils.isEmpty(model.getContent())) return;
                if (model.getContent().startsWith("http")) {
                    // 文件已经上传,直接发送消息即可
                    processor.sendMsg(model);
                } else {
                    // 文件还未上传,需要先上传文件,然后发送消息
                    sendFile(model, message, model.getContent());
                }
                break;
            default: break;
        }
    }

    @Override
    public void getMsgList(int page) {
        if (mReceiverType == TO_USER) {
            // 用户消息列表
            List<Message> messages = messageHelper.getUserMessageList(mReceiverId, page);
            processLocalMessage(messages, page);
            inViewProcess(TO_USER);
        } else if (mReceiverType == TO_GROUP) {
            List<Message> messages = messageHelper.getGroupMessageList(mReceiverId, page);
            processLocalMessage(messages, page);
            inViewProcess(TO_GROUP);
        }
    }

    // 进入聊天界面时的处理
    private void inViewProcess(int receiverType) {
        Run.onBackground(() -> {
            if (sessionHelper.isExistSession(mReceiverId, receiverType)) {
                Session session = sessionHelper.getSession(mReceiverId, receiverType);
                sessionHelper.updateUnReadCountAndNotify(session, 0);
            }
        });
    }

    private void processLocalMessage(List<Message> msgList, int page) {
        List<CommonMultiBean<Message>> beanList = new ArrayList<>();
        for (Message message : msgList) {
            beanList.add(new CommonMultiBean<>(message, message.getContentType()));
        }
        if (page > 1) {
            mView.showHistoryMsgList(beanList);
        } else {
            mView.showMsgList(beanList);
        }
    }

    private void sendFile(MsgModel model, Message newMsg, String path) {
        FormHelper formHelper = new FormHelper();
        try {
            List<File> files = new ArrayList<>();
            if (MsgType.CONTENT_AUDIO == newMsg.getContentType()) {
                // 音频直接添加即可
                files.addAll(Collections.singletonList(new File(model.getContent())));
            } else if (CONTENT_PICTURE == newMsg.getContentType()){
                // 图片则传入压缩后的文件路径
                files.addAll(Luban.with(MyApplication.getInstance())
                        .filter(p -> !p.endsWith(".gif")).load(path).get());
            }
            formHelper.addParameter("file", files);
            addDisposable(NetworkService.getInstance().uploadMsgFile(globalData.getJWT(), formHelper.builder())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new BaseObserver<ResultVo<String>>() {
                        @Override
                        public void onSuccess(ResultVo<String> data) {
                            if (SUCCESS.equals(data.getCode())) {
                                String url = FinderApiService.BASE_URL + data.getData();
                                // 替换为上传后的url
                                model.setContent(url);
                                // 发送一条携带文件url的消息
                                processor.sendMsg(model);
                            }
                        }

                        @Override
                        public void onFail(String errorMsg) {
                            super.onFail(errorMsg);
                            // 失败情况下直接发送,processor会直接发送一个失败消息
                            processor.sendMsg(model);
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveUserMessage(Message message) { }

    public void receiveGroupMessage(Message message) { }

    private MsgModel getMsgModel(String content, Integer contentType) {
        return MsgModel.builder()
                .command(mReceiverType == TO_USER ? USER_MSG_REQUEST : GROUP_MSG_REQUEST)
                .contentType(contentType).fromId(curUser.getId())
                .toId(mReceiverId).content(content).username(curUser.getUsername())
                .flag(DataUtils.getUUID()) // 唯一标示
                .portrait(curUser.getPortrait()).build();
    }
}
