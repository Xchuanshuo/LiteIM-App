package com.legend.im.protoctol.response;

import com.legend.im.bean.Msg;
import com.legend.im.protoctol.Packet;
import com.legend.im.protoctol.command.Command;

/**
 * @author Legend
 * @data by on 19-9-9.
 * @description 普通消息响应包
 */
public class UserMsgResponsePacket extends Packet {

    private Msg msg;

    @Override
    public Byte getCommand() {
        return Command.USER_MSG_RESPONSE;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
