package com.legend.liteim.presenter.session;

import com.legend.im.common.MsgType;
import com.legend.liteim.bean.Message;
import com.legend.liteim.bean.Session;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.contract.message.SessionContract;
import com.legend.liteim.db.SessionHelper;
import com.legend.liteim.event.MsgEvent;
import com.legend.liteim.event.RefreshSessionEvent;
import com.legend.liteim.event.RefreshTotalEvent;
import com.legend.liteim.event.RxBus;

import net.qiujuer.genius.kit.handler.Run;

import java.util.List;
import java.util.Objects;

import io.reactivex.schedulers.Schedulers;

/**
 * @author Legend
 * @data by on 19-9-7.
 * @description 消息页Presenter
 */
public class SessionPresenter extends BasePresenter<SessionContract.View>
    implements SessionContract.Presenter {

    private SessionHelper sessionHelper = new SessionHelper();

    public SessionPresenter(SessionContract.View view) {
        super(view);
        registerRefreshEvent();
        registerMsgEvent();
    }

    private void registerRefreshEvent() {
        addDisposable(RxBus.getDefault().toObservable(RefreshSessionEvent.class)
                .subscribe(refreshSessionEvent -> pullSessionList()));
    }

    private void registerMsgEvent() {
        addDisposable(RxBus.getDefault().toObservableSticky(MsgEvent.class)
                .observeOn(Schedulers.io())
                .subscribe(event -> {
                    Message message = event.getMessage();
                    LogUtils.logD(this, "接收了session通知:------------"+ message.getFromId());
                    boolean isSelf = message.getFromId().equals(this.curUser.getId());
                    // 新到达消息的状态更新与存储
                    Session session = null;
                    if (isSelf) {
                        // 对于自己发送的消息,不管是用户还是群组消息都根据toId去确定聊天对象
                        session = sessionHelper.getSession(message.getToId(), message.getType());
                    } else if (MsgType.TO_USER == message.getType()) {
                        session = sessionHelper.getSession(message.getFromId(), MsgType.TO_USER);
                    } else if (MsgType.TO_GROUP == message.getType()){
                        session = sessionHelper.getSession(message.getToId(), MsgType.TO_GROUP);
                    }
                    // 主要是更新发送状态
                    sessionHelper.saveOrUpdateAndNotify(Objects.requireNonNull(session), event);
                    // 发送事件给聊天界面的Presenter
                    RxBus.getDefault().post(message);
                }));
    }

    @Override
    public void pullSessionList() {
        List<Session> sessions = sessionHelper.getSessionList();
        Run.onUiAsync(() -> mView.showSessionList(sessions));
        RxBus.getDefault().post(new RefreshTotalEvent());
    }
}
