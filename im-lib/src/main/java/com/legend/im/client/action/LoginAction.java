package com.legend.im.client.action;

import com.legend.im.bean.MsgModel;
import com.legend.im.client.IMClient;
import com.legend.im.common.Session;
import com.legend.im.common.SessionUtil;
import com.legend.im.protoctol.request.LoginRequestPacket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author Legend
 * @data by on 19-9-14.
 * @description 构建并发送登录请求包
 */
public class LoginAction implements MsgAction {

    @Override
    public void exec(MsgModel model, Channel channel) {
        System.out.println("发起用户登录请求: \n + " + model);
        LoginRequestPacket requestPacket = new LoginRequestPacket();
        requestPacket.setJwt(model.getContent());
        ChannelFuture future = channel.pipeline().writeAndFlush(requestPacket);
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                Session session = new Session();
                session.setId(model.getFromId());
                session.setUsername(model.getUsername());
                session.setPortrait(model.getPortrait());
                SessionUtil.bindChannel(session, channel);
                System.out.println("客户端发起登录请求成功！");
                if (IMClient.getInstance().getStateListener() != null) {
                    IMClient.getInstance().getStateListener().alreadyAuth();
                }
            } else {
                System.out.println("客户端发起登录请求失败");
            }
        });
    }
}
