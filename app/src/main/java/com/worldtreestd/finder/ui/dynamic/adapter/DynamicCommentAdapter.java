package com.worldtreestd.finder.ui.dynamic.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.Comment;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.utils.GlideUtil;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.ui.userinfo.UserInfoActivity;

import java.util.List;

import static com.worldtreestd.finder.common.utils.Constant.LOOK_USER;

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
        CircleImageView mPortraitImg = helper.getView(R.id.portrait);
        mPortraitImg.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(LOOK_USER, buildUser(item));
            UserInfoActivity.come(mContext, bundle);
        });
        GlideUtil.loadImage(mContext, item.getPortrait(), helper.getView(R.id.portrait));
        helper.setText(R.id.tv_username, item.getUsername()+"");
        helper.setText(R.id.tv_content, item.getContent()+"");
        helper.setText(R.id.tv_praise_num, item.getFavorNum()+"");
        helper.setText(R.id.tv_time, item.getTimeAgoStr()+"");
        helper.setImageResource(R.id.img_praise, item.isFavor()?
                R.drawable.ic_praise_selected:R.drawable.ic_praise_normal);
        helper.setText(R.id.tv_praise_num, item.getFavorNum()+"");
        helper.addOnClickListener(R.id.img_praise);
        helper.addOnClickListener(R.id.img_menu);
    }

    private User buildUser(Comment item) {
        User user = new User();
        user.setId(item.getUserId());
        user.setPortrait(item.getPortrait());
        user.setUsername(item.getUsername());
        user.setBackground(item.getBackground());
        user.setSignature(item.getSignature());
        return user;
    }
}
