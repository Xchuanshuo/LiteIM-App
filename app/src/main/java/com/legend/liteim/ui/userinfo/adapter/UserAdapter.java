package com.legend.liteim.ui.userinfo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.common.widget.CircleImageView;
import com.legend.liteim.ui.userinfo.UserInfoActivity;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    protected int curPosition;

    public UserAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_username, item.getUsername());
        String signature = item.getSignature();
        LogUtils.logD(mContext, item.toString());
        if (TextUtils.isEmpty(signature)) {
            signature = "一切都是空空如也~";
        }
        helper.setText(R.id.tv_signature, signature);
        CircleImageView mPortraitImg = helper.getView(R.id.portrait);
        GlideUtil.loadImage(mContext, item.getPortrait(), mPortraitImg);
        LinearLayout mLayout = helper.getView(R.id.layout_main);
        mLayout.setOnClickListener(v -> {
            curPosition = helper.getAdapterPosition();
            UserInfoActivity.show(mContext, item);
        });
    }
}
