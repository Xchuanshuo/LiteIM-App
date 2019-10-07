package com.legend.liteim.common.base.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.R;
import com.legend.liteim.common.utils.ActivityManager;
import com.legend.liteim.common.widget.CircleImageView;
import com.legend.liteim.common.widget.FixLinearLayoutManager;
import com.legend.liteim.contract.base.BaseContract;
import com.legend.liteim.ui.search.SearchActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * @author Legend
 * @data by on 18-5-18.
 * @description
 */
public abstract class BaseActivity<T extends BaseContract.Presenter>
        extends SwipeBackActivity implements BaseContract.View<T> {

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @Nullable
    @BindView(R.id.toolbar_title_tv)
    protected TextView mTitle;
    @Nullable
    @BindView(R.id.portrait)
    protected CircleImageView mPortrait;
    @Nullable
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.mSearch_button)
    protected AppCompatImageView mSearchButton;
    @Nullable
    @BindView(R.id.mWrite_Button)
    protected AppCompatImageView mWriteButton;
    private View mEmptyView;
    protected BaseQuickAdapter mAdapter;
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
        }
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        initWindows();
        if (initArgs(getIntent().getExtras())) {
            setContentView(getLayoutId());
            initPresenter();
            initBefore();
            initWidget();
            initEventAndData();
        } else {
            finish();
        }
    }

    private boolean fixOrientation(){
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo)field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    private boolean isTranslucentOrFloating(){
        boolean isTranslucentOrFloating = false;
        try {
            int [] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean)m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * 初始化相关参数
     * @param bundle 参数bundle
     * @return  如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }


    /**
     *  设置布局前进行的操作
     */
    protected void initWindows() {

    }


    /**
     *  初始化控件调用之前
     */
    protected void initBefore() {

    }

    /**
     *  加载控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
        initToolbar();
    }

    /**
     * 加载数据
     */
    protected void initEventAndData() {
        init();
        if (mSearchButton != null) {
            mSearchButton.setOnClickListener(v -> startActivity(SearchActivity.getIntent(this)));
        }
    }

    private void init() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        if (null != getAdapter() && null != mRecyclerView) {
            RecyclerView.LayoutManager mLayoutManager =
                    null==getLayoutManager()?new FixLinearLayoutManager(getContext()):getLayoutManager();
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = getAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mEmptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRecyclerView.getParent(), false);
            mEmptyView.setOnClickListener(v->refreshData());
        }
    }

    private void initToolbar() {
        if (mToolbar == null) {
            return;
        }
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(showHomeAsUp());
        // mToolbar除掉阴影
        getSupportActionBar().setElevation(0);
        if (!showDefaultTitle()) {
            getSupportActionBar().setTitle("");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0);
        }
        if (showHomeAsUp()) {
            if (getNavigationIcon() != 0) {
                mToolbar.setNavigationIcon(getNavigationIcon());
            }
            mToolbar.setNavigationOnClickListener(v -> finish());
            if (mPortrait!=null) mPortrait.setVisibility(View.GONE);
        }
        if (mTitle !=null) {
            setToolbarTitle(getString(R.string.title_home));
        }
    }

    protected int getNavigationIcon() {
        return 0;
    }

    protected boolean showDefaultTitle() {
        return false;
    }

    protected void setToolbarTitle(String title) {
        mTitle.setText(title);
    }

    /**
     *  获取适配器
     * @return
     */
    protected BaseQuickAdapter getAdapter() { return null; }

    /**
     *  是否显示返回图标
     * @return
     */
    public boolean showHomeAsUp() {
        return false;
    }

    /**
     *  设置主布局
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 加载Presenter
     * @return
     */
    protected T initPresenter() {
        return null;
    }

    @Override
    public void setPresenter(T presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     *  设置布局管理器
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    @Override
    public void onBackPressedSupport() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null&& fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    return;
                }
            }
        }
        super.onBackPressedSupport();
        finish();
    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

}
