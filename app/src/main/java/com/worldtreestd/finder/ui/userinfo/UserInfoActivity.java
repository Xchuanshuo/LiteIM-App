package com.worldtreestd.finder.ui.userinfo;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.common.widget.NoScrollViewPager;
import com.worldtreestd.finder.common.widget.behavior.AppBarLayoutBehavior;
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
    @BindView(R.id.uc_zoomiv)
    ImageView mZoomIv;
    @BindView(R.id.uc_avater)
    CircleImageView mAvater;
    @BindView(R.id.setting_iv)
    ImageView mSettingIv;
    @BindView(R.id.message_iv)
    ImageView mMsgIv;
    @BindView(R.id.title_layout)
    ViewGroup titleContainer;
    @BindView(R.id.title_center_layout)
    ViewGroup titleCenterLayout;
    private int lastState = 1;
    final UserRelationPageAdapter pageAdapter = new UserRelationPageAdapter(getSupportFragmentManager());
    private AppBarLayoutBehavior mAppBarLayoutBehavior;

    @Override
    public boolean showHomeAsUp() {
        return false;
    }

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
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
                if (titleCenterLayout != null && mAvater != null && mSettingIv != null && mMsgIv != null) {
                    titleCenterLayout.setAlpha(percent);
//                    StatusBarUtil.setTranslucentForImageView(Demo1Activity.this, (int) (255f * percent), null);
                    if (percent == 0) {
                        groupChange(1f, 1);
                    } else if (percent == 1) {
                        if (mAvater.getVisibility() != View.GONE) {
                            mAvater.setVisibility(View.GONE);
                        }
                        groupChange(1f, 2);
                    } else {
                        if (mAvater.getVisibility() != View.VISIBLE) {
                            mAvater.setVisibility(View.VISIBLE);
                        }
                        groupChange(percent, 0);
                    }

                }
            }
        });
        mAppBarLayoutBehavior = (AppBarLayoutBehavior) ((CoordinatorLayout.LayoutParams)mAppBarLayout.getLayoutParams()).getBehavior();
        mAppBarLayoutBehavior.setOnProgressChangeListener(new AppBarLayoutBehavior.OnProgressChangeListener() {
            @Override
            public void onProgressChange(float progress, boolean isRelease) {
//                progressBar.setProgress((int) (progress * 360));
//                if (progress == 1 && !progressBar.isSpinning && isRelease) {
//                    // 刷新viewpager里的fragment
//                }
//                if (mMsgIv != null) {
//                    if (progress == 0 && !progressBar.isSpinning) {
//                        mMsgIv.setVisibility(View.VISIBLE);
//                    } else if (progress > 0 && mSettingIv.getVisibility() == View.VISIBLE) {
//                        mMsgIv.setVisibility(View.INVISIBLE);
//                    }
//                }
            }
        });
    }

    public void groupChange(float alpha, int state) {
        lastState = state;

        mSettingIv.setAlpha(alpha);
        mMsgIv.setAlpha(alpha);

        switch (state) {
            case 1://完全展开 显示白色
                mMsgIv.setImageResource(R.drawable.ic_message_black_24dp);
                mSettingIv.setImageResource(R.drawable.ic_settings_black_24dp);
                mViewPager.setNoScroll(false);
                break;
            case 2://完全关闭 显示黑色
                mMsgIv.setImageResource(R.drawable.ic_message_black_24dp);
                mSettingIv.setImageResource(R.drawable.ic_settings_black_24dp);
                mViewPager.setNoScroll(false);
                break;
            case 0://介于两种临界值之间 显示黑色
                if (lastState != 0) {
                    mMsgIv.setImageResource(R.drawable.ic_message_black_24dp);
                    mSettingIv.setImageResource(R.drawable.ic_settings_black_24dp);
                }
                mViewPager.setNoScroll(true);
                break;
        }
    }



    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
