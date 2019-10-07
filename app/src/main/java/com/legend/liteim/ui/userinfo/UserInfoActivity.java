package com.legend.liteim.ui.userinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.widget.NoScrollViewPager;
import com.legend.liteim.common.widget.picturewatcher.PreviewActivity;
import com.legend.liteim.contract.userinfo.UserInfoContract;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.event.UserAddEvent;
import com.legend.liteim.presenter.userinfo.UserInfoPresenter;
import com.legend.liteim.ui.chat.ChatActivity;
import com.legend.liteim.ui.userinfo.adapter.UserRelationPageAdapter;

import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Legend
 * @data by on 18-7-20.
 * @description 用户信息Activity
 */
public class UserInfoActivity extends BaseActivity<UserInfoContract.Presenter>
    implements UserInfoContract.View {

    public static final String LOOK_USER = "look_user";

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
    @BindView(R.id.tv_signature)
    TextView mSignatureTV;
    @BindView(R.id.tv_operate)
    TextView mOperateTv;
    @BindView(R.id.portrait_title)
    ImageView mPortraitTitle;
    @BindView(R.id.img_background)
    ImageView mBackgroundImg;
    @BindView(R.id.img_setting)
    ImageView mSettingImg;
    @BindView(R.id.btn_friend)
    FloatActionButton mFriendBtn;
    @BindView(R.id.title_layout)
    ViewGroup titleContainer;
    @BindView(R.id.title_center_layout)
    ViewGroup titleCenterLayout;
    private UserRelationPageAdapter pageAdapter;
    private boolean isSelf = false;
    private boolean isFollow = false;
    private boolean isFriend = false;
    private User localUser = UserHelper.getInstance().getCurrentUser();
    private User lookUser;

    @Override
    protected UserInfoContract.Presenter initPresenter() {
        return new UserInfoPresenter(this);
    }

    public static void show(Context context, User user) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOOK_USER, user);
        intent.putExtras(bundle);
        intent.setClass(context, UserInfoActivity.class);
        context.startActivity(intent);
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
        if (TextUtils.isEmpty(lookUser.getSignature())) {
            mSignatureTV.setText("签名: 一切都是空空如也~");
        } else {
            mSignatureTV.setText("签名: "+lookUser.getSignature());
        }
        GlideUtil.loadImageByBlur(this, lookUser.getBackground(), mBackgroundImg);
        GlideUtil.loadImage(this, lookUser.getPortrait(), mPortrait);
        GlideUtil.loadImage(this, lookUser.getPortrait(), mPortraitTitle);
        // 判断当前查看的用户是否是登录的用户
        if (lookUser!=null && localUser!=null) {
            this.isSelf = lookUser.getId().equals(localUser.getId());
        }
        if (isSelf) {
            mOperateTv.setText("修改资料");
            mFriendBtn.setVisibility(View.INVISIBLE);
        } else {
            mPresenter.followState(lookUser.getId());
            mPresenter.friendState(lookUser.getId());
        }
    }

    @OnClick(R.id.tv_operate)
    public void operate() {
        if (isSelf) {
            DialogUtils.showToast(this, "进入修改资料页面");
            UserInfoModifyActivity.show(this, localUser);
        } else if (isFollow) {
            mPresenter.unFollowUser(lookUser.getId());
        } else {
            mPresenter.followUser(lookUser.getId());
        }
    }

    @OnClick(R.id.img_return)
    public void returnUp() {
        finish();
    }

    @OnClick(R.id.portrait)
    public void watchPortrait() {
        PreviewActivity.show(this, new ArrayList<>(Collections.singletonList(lookUser.getPortrait())));
    }

    @OnClick(R.id.tv_chat)
    public void chatPage() {
        if (isSelf) {
            // todo
            DialogUtils.showToast(this, "进入留言页面");
        } else {
            if (!isFriend) {
                DialogUtils.showToast("请先成为好友，才可发起私聊!");
            } else {
                finish();
                ChatActivity.show(this, lookUser);
            }
        }
    }

    @OnClick(R.id.img_setting)
    public void onSettingsClick() {
        SettingsActivity.show(this);
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }

    @Override
    public void showFollowState(boolean isFollow) {
        this.isFollow = isFollow;
        mOperateTv.setText(isFollow?"已关注":"关注");
    }

    @OnClick(R.id.btn_friend)
    public void friendBtnClick() {
        if (isFriend) {
            mPresenter.deleteFriend(lookUser.getId());
        } else {
            mPresenter.addFriend(lookUser.getId());
        }
    }

    @Override
    public void showFriendState(boolean isFriend) {
        this.isFriend = isFriend;
        mFriendBtn.setImageResource(isFriend ? R.drawable.ic_done : R.drawable.ic_add);
        lookUser.setFriend(isFriend);
        UserHelper.getInstance().saveOrUpdate(lookUser);
        RxBus.getDefault().post(new UserAddEvent(isFriend));
    }
}
