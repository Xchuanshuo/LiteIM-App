package com.legend.im.client.action;


import com.legend.im.bean.MsgModel;
import com.legend.im.common.SessionUtil;
import com.legend.im.protoctol.command.Command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

import static com.legend.im.protoctol.command.Command.LOGIN_REQUEST;

/**
 * @author Legend
 * @data by on 19-2-14.
 * @description 消息行为管理
 */
public class MsgActionManager implements MsgAction {

    private static Map<Byte, MsgAction> commandMap = new ConcurrentHashMap<>();

    static {
        commandMap.put(LOGIN_REQUEST, new LoginAction());
        commandMap.put(Command.USER_MSG_REQUEST, new SendToUserMsgAction());
        commandMap.put(Command.GROUP_MSG_REQUEST, new SendToGroupMsgAction());
        commandMap.put(Command.MSG_ACK_REQUEST, new MsgAckAction());
    }

    public static MsgActionManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static MsgActionManager INSTANCE = new MsgActionManager();
    }

    private MsgActionManager() {
//        commandMap.put("sendToGroup", new SendToGroupConsoleCommand());
//        commandMap.put("logout", new LogoutConsoleCommand());
//        commandMap.put("createGroup", new CreateGroupConsoleCommand());
//        commandMap.put("joinGroup", new JoinGroupConsoleCommand());
//        commandMap.put("quitGroup", new QuitGroupConsoleCommand());
//        commandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
    }

    @Override
    public void exec(MsgModel model, Channel channel) {
        // 获取第一个指令
        if (!SessionUtil.isOnline(channel) && model.getCommand() != LOGIN_REQUEST) {
            return;
        }

        MsgAction msgAction = commandMap.get(model.getCommand());

        if (msgAction != null) {
            msgAction.exec(model, channel);
        } else {
            System.err.println("无法识别[" + model.getCommand() + "]指令, 请重新输入!" );
        }
    }
}
