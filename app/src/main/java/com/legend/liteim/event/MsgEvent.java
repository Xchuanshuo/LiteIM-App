package com.legend.liteim.event;

import com.legend.liteim.bean.Message;

/**
 * @author Legend
 * @data by on 19-9-16.
 * @description 消息事件
 */
public class MsgEvent {

    /**
     * 消息未读数量
     */
    private int size;
    /**
     * 消息本身
     */
    private Message message;

    public MsgEvent(Message message) {
        this(message, 0);
    }

    public MsgEvent(Message message, int size) {
        this.message = message;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message msg) {
        this.message = msg;
    }
}
