package com.legend.liteim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.legend.im.client.IMClient;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.utils.ScreenUtils;
import com.legend.liteim.data.Blocker;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.db.SessionHelper;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.event.JumpEvent;
import com.legend.liteim.event.RefreshTotalEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.model.ExtraDisposableManager;
import com.legend.liteim.ui.contacts.GroupCreateActivity;
import com.legend.liteim.ui.contacts.fragment.ContactsFragment;
import com.legend.liteim.ui.dynamic.fragment.DynamicFragment;
import com.legend.liteim.ui.release.ReleaseActivity;
import com.legend.liteim.ui.session.fragment.SessionFragment;
import com.legend.liteim.ui.userinfo.UserInfoActivity;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import io.reactivex.annotations.Nullable;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static com.legend.liteim.common.utils.Constant.POSITION;
import static com.legend.liteim.ui.release.ReleaseActivity.RELEASE_TYPE;

/**
 * @author legend
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.float_button)
    FloatingActionButton mFloatButton;
    @BindView(R.id.container)
    FrameLayout mContainer;
    AppCompatTextView mReleasePicture;
    AppCompatTextView mReleaseVideo;
    private PopupWindow mPopupWindow;
    private View mView;
    final SessionFragment mSessionFragment = new SessionFragment();
    final ContactsFragment mContactsFragment = new ContactsFragment();
    final DynamicFragment mDynamicFragment = new DynamicFragment();
    private Badge mRedPoint;
    User user = UserHelper.getInstance().getCurrentUser();

    public static void show(Context context, Integer id) {
        Intent intent = new Intent(context, MainActivity.class);
        if (id != null) {
            intent.putExtra(POSITION, id);
        }
        context.startActivity(intent);
    }

    public static void show(Context context) {
        show(context, null);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            item -> {
                loadFragment(item.getItemId());
                return true;
            };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);

        // 设置不可滑动
        setSwipeBackEnable(false);
        loadMultipleRootFragment(R.id.container,0, mSessionFragment,
                mContactsFragment, mDynamicFragment);
        int id = getIntent().getIntExtra(POSITION, -1);
        setSelected(id);
        registerJumpEvent();
    }

    private void registerTotalEvent() {
        RxBus.getDefault().toObservable(RefreshTotalEvent.class)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserver<RefreshTotalEvent>() {
                    @Override
                    public void onSuccess(RefreshTotalEvent data) {
                        mRedPoint.setBadgeNumber(SessionHelper.getInstance().getTotalUnReadCount());
                        mRedPoint.setOnDragStateChangedListener((dragState, badge1, targetView) -> {
                            if (dragState == Badge.OnDragStateChangedListener.STATE_SUCCEED) {
                                mRedPoint.setBadgeNumber(0);
                                SessionHelper.getInstance().updateAllUnReadCountAndNotify();
                            }
                        });
                    }
                });
    }

    private void registerJumpEvent() {
        RxBus.getDefault().toObservable(JumpEvent.class)
                .subscribe(new BaseObserver<JumpEvent>() {
                    @Override
                    public void onSuccess(JumpEvent data) {
                        setSelected(data.getPosition());
                    }
                });
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        GlideUtil.loadImage(this, user.getPortrait(), mPortrait);
        mPortrait.setOnClickListener(v -> {
            String jwt = GlobalData.getInstance().getJWT();
            if (TextUtils.isEmpty(jwt)) {
                LoginActivity.show(this);
                return;
            }
            UserInfoActivity.show(this, user);
        });
        mFloatButton.setOnClickListener(view->onClick(bottomNavigationView.getSelectedItemId()));
        mRedPoint = new QBadgeView(getContext()).bindTarget(bottomNavigationView
                .findViewById(R.id.navigation_session));
        mRedPoint.setBadgeGravity(Gravity.END | Gravity.TOP);
        mRedPoint.setGravityOffset(30, 0, true);
        registerTotalEvent();
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        // 获取用户信息
        initPopupWindow();
        mWriteButton.setOnClickListener(v -> {
            int[] windowPos = ScreenUtils.calculatePopWindowPos(mWriteButton, mView);
            mPopupWindow.showAtLocation(v, Gravity.TOP|Gravity.START
                    , windowPos[0]-50, windowPos[1]+20);
        } );
    }

    private void initPopupWindow() {
        mView = LayoutInflater.from(this).inflate(R.layout.dynamic_popupwindow, null);
        mPopupWindow= new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //点击 back 键的时候，窗口会自动消失
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mReleasePicture = mView.findViewById(R.id.tv_release_picture);
        mReleasePicture.setOnClickListener(v-> startActivity(new Intent().setClass(this, ReleaseActivity.class).putExtra(RELEASE_TYPE, "0")));
        mReleaseVideo = mView.findViewById(R.id.tv_release_video);
        mReleaseVideo.setOnClickListener(v-> startActivity(new Intent().setClass(this, ReleaseActivity.class).putExtra(RELEASE_TYPE, "1")));
    }

    private void loadFragment(int id) {
        switch (id) {
            case R.id.navigation_session:
                setToolbarTitle(getString(R.string.title_home));
                showHideFragment(mSessionFragment);
                mSearchButton.setVisibility(View.VISIBLE);
                mWriteButton.setVisibility(View.INVISIBLE);
                mFloatButton.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                break;
            case R.id.navigation_contacts:
                setToolbarTitle(getString(R.string.title_dashboard));
                showHideFragment(mContactsFragment);
                mSearchButton.setVisibility(View.VISIBLE);
                mWriteButton.setVisibility(View.INVISIBLE);
                mFloatButton.setImageResource(R.drawable.ic_group_add);
                break;
            case R.id.navigation_dynamic:
                setToolbarTitle(getString(R.string.title_notifications));
                showHideFragment(mDynamicFragment);
                mSearchButton.setVisibility(View.GONE);
                mWriteButton.setVisibility(View.VISIBLE);
                mFloatButton.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                break;
            default: break;
        }
    }

    private void setSelected(int id) {
        if (id != -1) {
            bottomNavigationView.setSelectedItemId(id);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void onClick(int id) {
        switch (id) {
            case R.id.navigation_session:
                mSessionFragment.jumpTop();
                break;
            case R.id.navigation_contacts:
                DialogUtils.showToast(getContext(), "进入群创建页面!");
                GroupCreateActivity.show(this);
                break;
            case R.id.navigation_dynamic:
                mDynamicFragment.jumpTop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清除所有粘性事件
        RxBus.getDefault().removeAllStickyEvent();
        // 清除额外的Disposable资源
        ExtraDisposableManager.getInstance().destroyAll();
        // 清除所有Blocker资源
        Blocker.clear();
        // 关闭连接
        if (!GlobalData.getInstance().isFirstIn()) {
            // 正常情况下 直接退出关闭就可以
            // 重新登录时,因为要finishAllActivity() 而IMClient是
            // 全局共享的,所以要防止重新登录后 建立的连接被断开
            IMClient.getInstance().destroy();
        }
    }
}
