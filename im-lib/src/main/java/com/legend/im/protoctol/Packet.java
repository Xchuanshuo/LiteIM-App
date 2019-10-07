package com.legend.im.protoctol;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Legend
 * @data by on 19-9-8.
 * @description 基础包
 */
public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false)
    private Byte version = 1;

    /**
     * 获取命令
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }
}
