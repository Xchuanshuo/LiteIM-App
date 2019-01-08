package com.worldtreestd.finder.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_CENTER;

/**
 * @author Legend
 * @data by on 18-7-15.
 * @description
 */
public class DetailBean implements MultiItemEntity {

    private String name;

    public DetailBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getItemType() {
        return HOME_ITEM_CENTER;
    }
}
