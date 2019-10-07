package com.legend.im.protoctol.response;

import com.legend.im.protoctol.Packet;
import com.legend.im.protoctol.command.Command;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description 心跳响应包
 */
public class HeartBeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
