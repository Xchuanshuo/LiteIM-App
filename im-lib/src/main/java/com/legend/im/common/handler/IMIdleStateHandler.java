package com.legend.im.common.handler;

import com.legend.im.client.IMClient;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description 连接空闲处理器
 */
public class IMIdleStateHandler extends IdleStateHandler {

    public IMIdleStateHandler() {
        super(IMClient.getInstance().getMaxIdleTime(), 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(IMClient.getInstance().getMaxIdleTime() + "秒未读到数据,关闭连接!");
        ctx.channel().close();
    }
}
