package com.legend.im.client.handler;

import com.legend.im.bean.Msg;
import com.legend.im.client.IMClient;
import com.legend.im.common.MsgType;
import com.legend.im.common.listener.MsgListener;
import com.legend.im.protoctol.request.MsgAckRequestPacket;
import com.legend.im.protoctol.response.UserMsgResponsePacket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Legend
 * @data by on 19-9-9.
 * @description
 */
@ChannelHandler.Sharable
public class UserMsgResponseHandler extends SimpleChannelInboundHandler<UserMsgResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserMsgResponsePacket responsePacket) throws Exception {
        Msg msg = responsePacket.getMsg();
        MsgListener listener = IMClient.getInstance().getMsgListener();
        if (listener != null) {
            listener.receiveUserMessage(msg);
        }
        String fromUserId = msg.getFromId() + "";
        String fromUsername = msg.getUsername();
        System.out.println("["+fromUserId + ":" + fromUsername + "] 对你说: " + msg.getMsg());
        System.out.println(msg);
        // 签收消息成功后回复应答包
        MsgAckRequestPacket ackRequestPacket = new MsgAckRequestPacket();
        ackRequestPacket.setType(MsgType.TO_USER);
        ackRequestPacket.setMsgId(responsePacket.getMsg().getMsgId());
        ctx.channel().writeAndFlush(ackRequestPacket);
    }
}
