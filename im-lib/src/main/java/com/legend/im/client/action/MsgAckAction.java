package com.legend.im.client.action;

import com.legend.im.bean.MsgModel;
import com.legend.im.client.IMClient;
import com.legend.im.common.listener.MsgListener;
import com.legend.im.protoctol.request.MsgAckRequestPacket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author Legend
 * @data by on 19-9-16.
 * @description
 */
public class MsgAckAction implements MsgAction {

    @Override
    public void exec(MsgModel model, Channel channel) {
        MsgAckRequestPacket requestPacket = new MsgAckRequestPacket();
        // 设置需要ack的消息id
        requestPacket.setMsgId(model.getFromId());
        // 设置消息类型,用户 or 群组
        requestPacket.setType(model.getContentType());
        ChannelFuture future = channel.pipeline().writeAndFlush(requestPacket);
        future.addListener((ChannelFutureListener) channelFuture -> {
            MsgListener msgListener = IMClient.getInstance().getMsgListener();
            if (msgListener != null) {
                if (channelFuture.isSuccess()) {
                    System.out.println("消息ack成功!");
                    msgListener.ackSuccess(requestPacket.getType(), requestPacket.getMsgId());
                } else {
                    System.out.println("消息ack失败!");
                    msgListener.ackFailure(requestPacket.getType(), requestPacket.getMsgId());
                }
            }
        });
    }
}
