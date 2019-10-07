package com.legend.im.protoctol.request;

import com.legend.im.protoctol.Packet;
import com.legend.im.protoctol.command.Command;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description 群消息请求包
 */
public class GroupMsgRequestPacket extends Packet {

    private Long fromId;
    private Long toId;
    private String username;
    private String portrait;
    private String msg;
    private String attach;
    private Integer contentType;

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    @Override
    public Byte getCommand() {
        return Command.GROUP_MSG_REQUEST;
    }
}
