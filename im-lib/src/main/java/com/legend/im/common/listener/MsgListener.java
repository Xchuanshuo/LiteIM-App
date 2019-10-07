package com.legend.im.common.listener;

import com.legend.im.bean.Msg;

/**
 * @author Legend
 * @data by on 19-9-15.
 * @description 消息监听器
 */
public interface MsgListener {

    /**
     * 消息发送成功
     */
    void sendUserMsgSuccess(Msg msg);

    void sendGroupMsgSuccess(Msg msg);

    /**
     * 消息发送失败
     */
    void sendUserMsgFailure(Msg msg);

    void sendGroupMsgFailure(Msg msg);

    /**
     * 收到用户消息
     * @param msg
     */
    void receiveUserMessage(Msg msg);

    /**
     * 收到群消息
     * @param msg
     */
    void receiveGroupMessage(Msg msg);

    /**
     * 消息ack成功
     * @param type
     * @param msgId
     */
    void ackSuccess(Integer type, Long msgId);

    /**
     * 消息ack失败
     * @param type
     * @param msgId
     */
    void ackFailure(Integer type, Long msgId);
}
