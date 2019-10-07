package com.legend.im.client.handler;

import com.legend.im.bean.Msg;
import com.legend.im.client.IMClient;
import com.legend.im.common.MsgType;
import com.legend.im.common.listener.MsgListener;
import com.legend.im.protoctol.request.MsgAckRequestPacket;
import com.legend.im.protoctol.response.GroupMsgResponsePacket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Legend
 * @data by on 19-9-24.
 * @description
 */
@ChannelHandler.Sharable
public class GroupMsgResponseHandler extends SimpleChannelInboundHandler<GroupMsgResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMsgResponsePacket responsePacket) throws Exception {
        Msg msg = responsePacket.getMsg();
        MsgListener msgListener = IMClient.getInstance().getMsgListener();
        if (msgListener != null) {
            msgListener.receiveGroupMessage(msg);
        }
        String fromUserId = msg.getFromId() + "";
        String fromUsername = msg.getUsername();
        System.out.println("群组:" + responsePacket.getMsg().getToId() +
                "["+fromUserId + ":" + fromUsername + "] 对你说: " + msg.getMsg());
        System.out.println(msg);
        MsgAckRequestPacket ackRequestPacket = new MsgAckRequestPacket();
        ackRequestPacket.setType(MsgType.TO_GROUP);
        ackRequestPacket.setMsgId(msg.getMsgId());
        ctx.channel().writeAndFlush(ackRequestPacket);
    }
}
