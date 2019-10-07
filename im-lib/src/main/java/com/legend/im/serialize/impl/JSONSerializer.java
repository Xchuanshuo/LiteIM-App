package com.legend.im.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.legend.im.serialize.Serializer;
import com.legend.im.serialize.SerializerAlogrithm;

/**
 * @author Legend
 * @data by on 19-2-12.
 * @description
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlogrithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
