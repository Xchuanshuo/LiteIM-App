package com.legend.im.protoctol.request;

import com.legend.im.protoctol.Packet;
import com.legend.im.protoctol.command.Command;

/**
 * @author Legend
 * @data by on 19-9-8.
 * @description 上线请求包
 */
public class LoginRequestPacket extends Packet {

    private String jwt;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
