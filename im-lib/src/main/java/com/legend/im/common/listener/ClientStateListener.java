package com.legend.im.common.listener;

/**
 * @author Legend
 * @data by on 19-9-15.
 * @description 客户端状态监听器
 */
public interface ClientStateListener {

    void connected();

    void unAuth();

    void alreadyAuth();

    void closed();
}
