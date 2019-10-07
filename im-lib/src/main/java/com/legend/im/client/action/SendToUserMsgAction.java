package com.legend.im.client.action;

import com.legend.im.bean.MsgModel;
import com.legend.im.client.IMClient;
import com.legend.im.common.listener.MsgListener;
import com.legend.im.protoctol.request.UserMsgRequestPacket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author Legend
 * @data by on 19-2-14.
 * @description 构建并发送用户消息包
 */
public class SendToUserMsgAction implements MsgAction {

    @Override
    public void exec(MsgModel model, Channel channel) {
        System.out.println("发送用户消息: \n + " + model);
        UserMsgRequestPacket requestPacket = new UserMsgRequestPacket();
        requestPacket.setFromId(model.getFromId());
        requestPacket.setMsg(model.getContent());
        requestPacket.setAttach(model.getAttach());
        requestPacket.setPortrait(model.getPortrait());
        requestPacket.setContentType(model.getContentType());
        requestPacket.setUsername(model.getUsername());
        requestPacket.setToId(model.getToId());
        MsgListener listener = IMClient.getInstance().getMsgListener();
        ChannelFuture future = channel.writeAndFlush(requestPacket);
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                if (listener != null) {
                    listener.sendUserMsgSuccess(model.buildMsg());
                }
            } else {
                if (listener != null) {
                    listener.sendUserMsgFailure(model.buildMsg());
                }
            }
        });
    }
}
