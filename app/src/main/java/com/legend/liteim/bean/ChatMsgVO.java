package com.legend.liteim.bean;

import com.legend.im.bean.Msg;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-10-1.
 * @description 每个用户对应的消息列表，目的是为了方便
 *              拉取离线消息后在本地的存储
 */
public class ChatMsgVO {

    /**
     * 发送方id
     */
    private Long fromId;

    /**
     * 用户或群组名称
     */
    private String name;

    /**
     * 用户或者群组的头像
     */
    private String portrait;

    /**
     * 消息的类型
     *  0: 好友
     *  1: 群组
     */
    private Integer msgType;

    /**
     * 消息列表
     */
    private List<Msg> msgList;

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public List<Msg> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Msg> msgList) {
        this.msgList = msgList;
    }
}
