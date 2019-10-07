package com.legend.liteim.event;

import com.legend.im.bean.Msg;
import com.legend.im.common.MsgType;
import com.legend.im.common.listener.MsgListener;
import com.legend.liteim.bean.Message;
import com.legend.liteim.common.utils.LogUtils;

/**
 * @author Legend
 * @data by on 19-9-16.
 * @description 消息监听器的实现类,使用RxBus进行全局通知
 */
public class MsgListenerImpl implements MsgListener {

    @Override
    public void sendUserMsgSuccess(Msg msg) {
        Message message = Message.buildMsg(msg);
        message.setType(MsgType.TO_USER);
        message.setStatus(Message.STATUS_SUCCESS);
        message.setMsgId(msg.getFlag());
        MsgEvent event = new MsgEvent(message);
        RxBus.getDefault().post(event);
    }

    @Override
    public void sendGroupMsgSuccess(Msg msg) {
        Message message = Message.buildMsg(msg);
        message.setType(MsgType.TO_GROUP);
        message.setStatus(Message.STATUS_SUCCESS);
        message.setMsgId(msg.getFlag());
        MsgEvent event = new MsgEvent(message);
        RxBus.getDefault().post(event);
    }

    @Override
    public void sendUserMsgFailure(Msg msg) {
        Message message = Message.buildMsg(msg);
        message.setMsgId(msg.getFlag());
        message.setType(MsgType.TO_USER);
        message.setStatus(Message.STATUS_FAILURE);
        MsgEvent event = new MsgEvent(message);
        RxBus.getDefault().post(event);
    }

    @Override
    public void sendGroupMsgFailure(Msg msg) {
        Message message = Message.buildMsg(msg);
        message.setMsgId(msg.getFlag());
        message.setType(MsgType.TO_GROUP);
        message.setStatus(Message.STATUS_FAILURE);
        MsgEvent event = new MsgEvent(message);
        RxBus.getDefault().post(event);
    }

    @Override
    public void receiveUserMessage(Msg msg) {
        Message message = Message.buildMsg(msg);
        message.setType(MsgType.TO_USER);
        MsgEvent event = new MsgEvent(message, 1);
        RxBus.getDefault().post(event);
    }

    @Override
    public void receiveGroupMessage(Msg msg) {
        Message message = Message.buildMsg(msg);
        message.setType(MsgType.TO_GROUP);
        MsgEvent event = new MsgEvent(message, 1);
        RxBus.getDefault().post(event);
    }

    @Override
    public void ackSuccess(Integer type, Long msgId) {
        LogUtils.logD(this.getClass(), "ack消息成功!" + type + " : " + msgId);
    }

    @Override
    public void ackFailure(Integer type, Long msgId) {
        LogUtils.logD(this.getClass(), "ack消息失败!" + type + " : " + msgId);
    }
}
