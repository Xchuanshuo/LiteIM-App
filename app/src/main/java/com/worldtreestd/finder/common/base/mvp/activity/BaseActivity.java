package com.worldtreestd.finder.common.base.mvp.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.contract.base.BaseContract;
import com.worldtreestd.finder.ui.search.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

import static com.worldtreestd.finder.common.base.mvp.StatusType.LOAD_MORE_FAILURE;
import static com.worldtreestd.finder.common.base.mvp.StatusType.LOAD_MORE_SUCCESS;
import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_FAILURE;
import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 18-5-18.
 * @description
 */
public abstract class BaseActivity<T extends BaseContract.Presenter>
        extends SupportActivity implements BaseContract.View<T> {

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
    private Dialog mDialog;
    protected BaseQuickAdapter mAdapter;
    protected T mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        setContentView(getLayoutId());
        initPresenter();
        initBefore();
        initWidget();
        initEventAndData();
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
//            mEmptyView.setOnClickListener(v->refreshData());
        }
        mDialog = DialogUtils.showLoadingDialog(this,getString(R.string.common_loading));
    }


    /**
     *  对数据加载结果进行统一处理
     */
    protected void setLoadDataResult(SwipeRefreshLayout refreshLayout,
                                     List list, int type) {
        switch (type) {
            case REFRESH_SUCCESS:
                mAdapter.setNewData(list);
                refreshLayout.setRefreshing(false);
                break;
            case REFRESH_FAILURE:
                refreshLayout.setRefreshing(false);
                break;
            case LOAD_MORE_SUCCESS:
                if (list != null) {
                    mAdapter.addData(list);
                    mAdapter.loadMoreComplete();
                }
                break;
            case LOAD_MORE_FAILURE:
                mAdapter.loadMoreFail();
                break;
            default: break;
        }
        if (mAdapter.getData().isEmpty()) {
            mAdapter.setEmptyView(mEmptyView);
        }
    }

    private void initmToolbar() {
        if (mToolbar == null) {
            return;
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp());
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
