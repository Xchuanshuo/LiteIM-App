package com.worldtreestd.finder.ui.dynamic.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.Comment;
import com.worldtreestd.finder.common.utils.GlideUtil;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public class DynamicCommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    public DynamicCommentAdapter(int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        GlideUtil.loadImage(mContext, item.getPortrait(), helper.getView(R.id.portrait));
        helper.setText(R.id.tv_username, item.getUsername()+"");
        helper.setText(R.id.tv_content, item.getContent()+"");
        helper.setText(R.id.tv_favor_num, item.getFavorNum()+"");
        helper.setText(R.id.tv_time, item.getTimeAgoStr()+"");
    }
}
