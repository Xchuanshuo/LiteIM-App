package com.legend.liteim.bean;

import android.text.TextUtils;

import com.legend.im.bean.Msg;
import com.legend.liteim.common.utils.DateUtils;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Message extends LitePalSupport {

    public static final int STATUS_CREATED = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILURE = 2;

    /**
     * 消息id
     */
    private String msgId;

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
     * 附加信息
     */
    private String attach;

    /**
     * 用户或群组
     */
    private Integer type;

    /**
     * 消息内容的类型
     *  0: 文本消息
     *  1: 图片消息
     *  2: 语音消息
     *  3: 文件消息
     */
    private Integer contentType;

    /**
     * 发送状态
     */
    private int status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 消息标志位,用来定位到加载到内存中的该消息
     * 由于发送时是先保存到内存 而msgId是服务器端生成的
     * 所以必须要用一个标志位能够唯一确定到一条消息
     */
    @Column(ignore = true)
    private String flag;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
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

    public String getCreateTime() {
        if (TextUtils.isEmpty(createTime)) {
            return DateUtils.getDate();
        }
        return createTime.replace("T", " ");
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public static Message buildMsg(Msg msg) {
        Message message = new Message();
        message.setMsgId(String.valueOf(msg.getMsgId()));
        message.setFromId(msg.getFromId());
        message.setToId(msg.getToId());
        message.setMsg(msg.getMsg());
        message.setAttach(msg.getAttach());
        message.setUsername(msg.getUsername());
        message.setPortrait(msg.getPortrait());
        message.setCreateTime(msg.getCreateTime());
        message.setContentType(msg.getContentType());
        message.setFlag(msg.getFlag());
        return message;
    }
}
