package com.worldtreestd.finder.ui.moreinfo.adapter;

import android.content.Context;

import com.example.legend.wheel.adapters.AbstractWheelTextAdapter;
import com.worldtreestd.finder.R;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-9-2.
 * @description
 */
public class WheelAdapter extends AbstractWheelTextAdapter {

    private List<String> mData;

    public WheelAdapter(Context context, List<String> mData) {
        super(context, R.layout.item_spinner_drop);
        this.mData = mData;
        setItemTextResource(R.id.tv_item_name);
    }

    @Override
    public String getItemText(int i) {
        return mData.get(i);
    }

    @Override
    public int getItemsCount() {
        return mData.size();
    }
}
