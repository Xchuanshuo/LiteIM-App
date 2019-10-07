package com.legend.im.protoctol.response;

import com.legend.im.bean.Msg;
import com.legend.im.protoctol.Packet;
import com.legend.im.protoctol.command.Command;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description 群消息响应包
 */
public class GroupMsgResponsePacket extends Packet {

    private Msg msg;

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    @Override
    public Byte getCommand() {
        return Command.GROUP_MSG_RESPONSE;
    }
}
