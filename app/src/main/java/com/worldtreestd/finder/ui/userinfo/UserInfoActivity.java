package com.worldtreestd.finder.ui.userinfo;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.common.utils.GlideUtil;
import com.worldtreestd.finder.common.widget.NoScrollViewPager;
import com.worldtreestd.finder.data.DBData;
import com.worldtreestd.finder.ui.userinfo.adapter.UserRelationPageAdapter;

import butterknife.BindView;

/**
 * @author Legend
 * @data by on 18-7-20.
 * @description 用户信息Activity
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.mViewPager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.tv_username)
    TextView mUsernameTv;
    @BindView(R.id.tv_username_title)
    TextView mUsernameTvTitle;
    @BindView(R.id.portrait_title)
    ImageView mPortraitTitle;
    @BindView(R.id.img_background)
    ImageView mBackgroundImg;
    @BindView(R.id.img_setting)
    ImageView mSettingImg;
    @BindView(R.id.img_message)
    ImageView mMsgImg;
    @BindView(R.id.title_layout)
    ViewGroup titleContainer;
    @BindView(R.id.title_center_layout)
    ViewGroup titleCenterLayout;
    final UserRelationPageAdapter pageAdapter = new UserRelationPageAdapter(getSupportFragmentManager());
    private User user = DBData.getInstance().getCurrentUser();

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info_main;
    }

    @Override
    protected void initWindows() {
        super.initWindows();
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mViewPager.setAdapter(pageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float percent = (float) Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();
            if (titleCenterLayout != null && mPortrait!= null) {
                titleCenterLayout.setAlpha(percent);
                if (percent == 1) {
                    if (mPortrait.getVisibility() != View.GONE) {
                        mPortrait.setVisibility(View.GONE);
                    }
                } else {
                    if (mPortrait.getVisibility() != View.VISIBLE) {
                        mPortrait.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        mUsernameTvTitle.setText(user.getUsername());
        mUsernameTv.setText(user.getUsername());
        GlideUtil.loadImageByBlur(this, user.getBackground(), mBackgroundImg);
        GlideUtil.loadImage(this, user.getPortrait(), mPortrait);
        GlideUtil.loadImage(this, user.getPortrait(), mPortraitTitle);
    }


    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
