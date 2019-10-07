package com.legend.im.client.handler;

import com.legend.im.client.IMClient;
import com.legend.im.common.SessionUtil;
import com.legend.im.common.listener.ClientStateListener;
import com.legend.im.protoctol.request.HeartBeatRequestPacket;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description 定时发送心跳请求包
 */
@ChannelHandler.Sharable
public class HeartBeatTimeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduleSendHeartBeat(ctx);
        super.channelActive(ctx);
    }

    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                if (!SessionUtil.isOnline(ctx.channel())) {
                    ClientStateListener stateListener =  IMClient.getInstance().getStateListener();
                    if (stateListener != null) {
                        stateListener.unAuth();
                    }
                }
                System.out.println(".......发送心跳包........");
                ctx.writeAndFlush(new HeartBeatRequestPacket());
                scheduleSendHeartBeat(ctx);
            }
        }, IMClient.getInstance().getHeartbeatInterval(), TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("客户端自动断开连接~尝试重连...");
        IMClient.getInstance().setClosed(true);
        IMClient.getInstance().reconnect();
    }
}
