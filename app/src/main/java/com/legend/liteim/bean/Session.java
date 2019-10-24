package com.legend.liteim.bean;

import com.legend.liteim.db.MessageHelper;

import org.litepal.crud.LitePalSupport;

/**
 * @author Legend
 * @data by on 19-9-17.
 * @description 会话基础bean(只在本地保存)
 */
public class Session extends LitePalSupport {

    private long id;

    /**
     * 用户或群组的id
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
     * 未读数量，当没有在当前界面时，应当增加未读数量
     */
    private int unReadCount;

    /**
     * 最后更改时间
     */
    private String updateDate;

    /**
     * 最后收到的一条消息
     */
    private Message message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Message getMessage() {
        return MessageHelper.getInstance().getLastMsgBySessionId(id);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
