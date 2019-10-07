package com.legend.im.protoctol.request;

import com.legend.im.protoctol.Packet;
import com.legend.im.protoctol.command.Command;

/**
 * @author Legend
 * @data by on 19-9-9.
 * @description 消息应答请求包
 */
public class MsgAckRequestPacket extends Packet {

    /**
     * 回复的消息id,客户端收到消息后服务器端需要修改消息的状态
     * 在离线拉取消息成功时,只需要回复最后收到的消息id即可
     */
    private Long msgId;
    /**
     * ack请求包的类型 0 单聊 1 群聊
     */
    private Integer type;

    @Override
    public Byte getCommand() {
        return Command.MSG_ACK_REQUEST;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
