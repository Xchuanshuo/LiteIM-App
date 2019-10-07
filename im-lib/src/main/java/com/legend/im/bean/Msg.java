package com.legend.im.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Msg {

    /**
     * 消息id
     */
    private Long msgId;

    /**
     * 发送方id
     */
    private Long fromId;

    /**
     * 接收方id
     */
    private Long toId;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 名称
     */
    private String username;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息附加信息
     */
    private String attach;

    /**
     * 消息内容的类型
     *  0: 文本消息
     *  1: 图片消息
     *  2: 语音消息
     *  3: 文件消息
     */
    private Integer contentType;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 标志位,目的是为了当消息状态更改时
     * 去定位到已经在内存中的消息
     */
    @JSONField(serialize = false)
    private String flag;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

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

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
