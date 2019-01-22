package com.worldtreestd.finder.ui.userinfo.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.utils.GlideUtil;
import com.worldtreestd.finder.common.utils.LogUtils;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.ui.userinfo.UserInfoActivity;

import java.util.List;

import static com.worldtreestd.finder.common.utils.Constant.LOOK_USER;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

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
            Bundle bundle = new Bundle();
            bundle.putSerializable(LOOK_USER, item);
            UserInfoActivity.come(mContext, bundle);
        });
    }
}
