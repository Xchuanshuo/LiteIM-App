package com.worldtreestd.finder.common.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.worldtreestd.finder.R;

/**
 * @author Legend
 * @data by on 18-7-17.
 * @description
 */
public class CustomerLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
