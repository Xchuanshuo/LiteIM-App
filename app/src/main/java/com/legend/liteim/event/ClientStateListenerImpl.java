package com.legend.liteim.event;

import com.legend.im.bean.MsgModel;
import com.legend.im.common.listener.ClientStateListener;
import com.legend.im.protoctol.command.Command;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.data.MsgProcessor;

/**
 * @author Legend
 * @data by on 19-9-16.
 * @description 客户端连接状态监听器
 */
public class ClientStateListenerImpl implements ClientStateListener {

    @Override
    public void connected() {
        System.out.println("客户端已经连接...");
        unAuth();
    }

    @Override
    public void unAuth() {
        if (GlobalData.getInstance().getCurrentUserId() == null) {
            return;
        }
        MsgModel msgModel = MsgModel.builder()
                .content(GlobalData.getInstance().getJWT())
                .fromId(Long.valueOf(GlobalData.getInstance().getCurrentUserId()))
                .command(Command.LOGIN_REQUEST)
                .build();
        MsgProcessor.getInstance().sendMsg(msgModel);
    }

    @Override
    public void alreadyAuth() {
        // 登录成功后主动拉取离线消息
        MsgProcessor.getInstance().pullAllOfflineMsg();
    }

    @Override
    public void closed() {
        System.out.println("客户端已经关闭...");
    }
}
