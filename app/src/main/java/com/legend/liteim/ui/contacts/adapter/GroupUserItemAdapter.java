package com.legend.liteim.ui.contacts.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.ui.userinfo.UserInfoActivity;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public class GroupUserItemAdapter extends BaseQuickAdapter<User, BaseViewHolder> {


    public GroupUserItemAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_username, item.getUsername());
        GlideUtil.loadImage(helper.itemView.getContext(),
                item.getPortrait(), helper.getView(R.id.im_portrait));
        helper.setVisible(R.id.cb_select, false);
        helper.itemView.setOnClickListener(v -> UserInfoActivity.show(helper.itemView.getContext(), item));
    }
}
