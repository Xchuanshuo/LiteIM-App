package com.worldtreestd.finder.common.bean;

/**
 * @author Legend
 * @data by on 18-8-25.
 * @description
 */
public class CommonMoreInfoBean<T> {

    private int code;
    private String name;
    private T mData;

    public CommonMoreInfoBean(T mData, int code) {
        this.mData = mData;
        this.code = code;
    }

    public T getData() {
        return mData;
    }

    public int getCode() {
        return code;
    }
}
