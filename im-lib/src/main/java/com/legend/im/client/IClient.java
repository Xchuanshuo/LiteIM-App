package com.legend.im.client;

import com.legend.im.bean.MsgModel;

/**
 * @author Legend
 * @data by on 19-9-14.
 * @description IM客户端抽象接口
 */
public interface IClient {

    /**
     * 是否正在连接
     * @return
     */
    boolean isConnecting();

    /**
     * 重置连接
     */
    void reconnect();

    /**
     * 销毁客户端,释放资源
     */
    void destroy();

    /**
     * 客户端是否已被销毁
     * @return
     */
    boolean isDestroyed();

    /**
     * 客户端是否已被关闭
     * @return
     */
    boolean isClosed();

    /**
     * 发送消息
     * @param
     */
    void sendMsg(MsgModel model);

    /**
     * 获取连接最大重试次数
     * @return
     */
    int getMaxConnectionRetry();

    /**
     * 获取应用层消息发送超时重发次数
     *
     * @return
     */
    int getResendCount();

    /**
     * 获取最大连接读写空闲的时间空闲
     *
     * @return
     */
    int getMaxIdleTime();

    /**
     * 获取心跳包发送时间
     * @return
     */
    int getHeartbeatInterval();

}
