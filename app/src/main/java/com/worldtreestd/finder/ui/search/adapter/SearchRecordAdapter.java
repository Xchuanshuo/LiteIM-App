package com.worldtreestd.finder.ui.search.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.bean.SearchKeywordBean;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description 搜索记录适配器
 */
public class SearchRecordAdapter extends BaseQuickAdapter<SearchKeywordBean, BaseViewHolder> {

    public SearchRecordAdapter(int layoutResId, @Nullable List<SearchKeywordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchKeywordBean item) {
        helper.setText(R.id.history_item_tv, item.getName());
    }
}
