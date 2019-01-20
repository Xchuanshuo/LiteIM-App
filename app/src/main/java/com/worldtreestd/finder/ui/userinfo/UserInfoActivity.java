package com.worldtreestd.finder.ui.userinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import static com.worldtreestd.finder.common.utils.Constant.LOOK_USER;

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
    private UserRelationPageAdapter pageAdapter;
    private User localUser = DBData.getInstance().getCurrentUser();
    private User lookUser;

    public static void come(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info_main;
    }

    @Override
    public boolean showHomeAsUp() {
        return true;
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
    protected void initBefore() {
        super.initBefore();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        this.lookUser = (User) bundle.getSerializable(LOOK_USER);
        pageAdapter = new UserRelationPageAdapter(getSupportFragmentManager(), lookUser);
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

        mUsernameTvTitle.setText(lookUser.getUsername());
        mUsernameTv.setText(lookUser.getUsername());
        GlideUtil.loadImageByBlur(this, lookUser.getBackground(), mBackgroundImg);
        GlideUtil.loadImage(this, lookUser.getPortrait(), mPortrait);
        GlideUtil.loadImage(this, lookUser.getPortrait(), mPortraitTitle);
        // 判断当前查看的用户是否是登录的用户
        if (lookUser!=null && localUser!=null) {
            if (!lookUser.getId().equals(localUser.getId())) {
                // todo:

            } else {
                // todo:
            }
        }
    }


    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
