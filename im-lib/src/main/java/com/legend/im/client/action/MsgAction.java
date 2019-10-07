package com.legend.im.client.action;

import com.legend.im.bean.MsgModel;

import io.netty.channel.Channel;

/**
 * @author Legend
 * @data by on 19-2-14.
 * @description
 */
public interface MsgAction {

    /**
     * 消息行为
     * @param model 消息基本信息的载体
     * @param channel 当前连接通道
     */
    void exec(MsgModel model, Channel channel);
}
