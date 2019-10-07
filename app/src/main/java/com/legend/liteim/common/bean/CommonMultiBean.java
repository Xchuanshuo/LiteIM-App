package com.legend.liteim.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author Legend
 * @data by on 18-7-16.
 * @description RecyclerView多布局统一Bean
 */
public class CommonMultiBean<T> implements MultiItemEntity {

    private T data;
    private int mItemType = 0;
    private int mSpanSize;

    public CommonMultiBean(int mItemType) {
        this(null, mItemType, 6);
    }

    public CommonMultiBean(T data, int mItemType) {
        this(data, mItemType, 6);
    }

    public CommonMultiBean(T data, int mItemType, int mSpanSize) {
        this.data = data;
        this.mItemType = mItemType;
        this.mSpanSize = mSpanSize;
    }

    public int getSpanSize() {
        return mSpanSize;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public T getData() {
        return data;
    }
}
