package com.worldtreestd.finder.ui.dynamic.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.bean.CommentBean;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public class DynamicCommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    public DynamicCommentAdapter(int layoutResId, @Nullable List<CommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {
        helper.setText(R.id.tv_username, item.getName());
        helper.setText(R.id.tv_content, item.getContent());
        helper.setText(R.id.tv_collect_num, item.getPraiseCount());
    }
}
