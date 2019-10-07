package com.legend.im.protoctol.request;

import com.legend.im.protoctol.Packet;
import com.legend.im.protoctol.command.Command;


/**
 * @author Legend
 * @data by on 19-9-8.
 * @description 离线请求包
 */
public class OfflineRequestPacket extends Packet {



    @Override
    public Byte getCommand() {
        return Command.OFFLINE_REQUEST;
    }
}
