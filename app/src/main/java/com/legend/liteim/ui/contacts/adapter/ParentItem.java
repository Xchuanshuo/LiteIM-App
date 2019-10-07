package com.legend.liteim.ui.contacts.adapter;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author Legend
 * @data by on 19-9-10.
 * @description
 */
public class ParentItem<Entity> extends AbstractExpandableItem<Entity> implements MultiItemEntity {

    public String title;
    public Integer count;

    public ParentItem(String title, Integer count) {
        this.title = title;
        this.count = count;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return ContactsAdapter.TYPE_PARENT;
    }
}
