package com.worldtreestd.finder.common.base.mvp.activity;

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
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.contract.base.BaseContract;
import com.worldtreestd.finder.ui.search.SearchActivity;

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
        initWindows();
        setContentView(getLayoutId());
        initPresenter();
        initBefore();
        initWidget();
        initEventAndData();
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
        initmToolbar();
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
            mAdapter = getAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mEmptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRecyclerView.getParent(), false);
            mEmptyView.setOnClickListener(v->refreshData());
        }
    }

    private void initmToolbar() {
        if (mToolbar == null) {
            return;
        }
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(showHomeAsUp());
        /**mToolbar除掉阴影*/
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0);
        }
        if (showHomeAsUp()) {
            mToolbar.setNavigationOnClickListener(v -> finish());
            if (mPortrait!=null) mPortrait.setVisibility(View.GONE);
        }
        if (mTitle !=null) {
            setToolbarTitle(getString(R.string.title_home));
        }
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

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressedSupport() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null&& fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof Fragment) {
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
