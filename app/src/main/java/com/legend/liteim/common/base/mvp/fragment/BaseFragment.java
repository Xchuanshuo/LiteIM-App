package com.legend.liteim.common.base.mvp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.R;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.widget.CustomerLoadMoreView;
import com.legend.liteim.common.widget.FixLinearLayoutManager;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

import butterknife.BindView;

import static com.legend.liteim.common.base.mvp.StatusType.LOAD_MORE_FAILURE;
import static com.legend.liteim.common.base.mvp.StatusType.LOAD_MORE_SUCCESS;
import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_FAILURE;
import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 18-5-18.
 * @description
 */
public abstract class BaseFragment<T extends BaseContract.Presenter> extends SuperFragment
        implements BaseContract.View<T> {

    protected T mPresenter;
    protected RecyclerView mRecyclerView;
    private View mEmptyView;
    protected BaseQuickAdapter mAdapter;
    private boolean isRefreshing = false;
    private boolean isEnableLodeMore = true;
    @Nullable
    @BindView(R.id.mSwipeRefresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initArgs(getArguments());
    }

    @Override
    protected void initWidget() {
        init();
        initPresenter();

    }

    @Override
    protected void initEventAndData() {
        if (!isRefreshing) {
            refreshData();
        }
        isRefreshing = !isRefreshing;
    }

    protected void setEnableLoaderMore(boolean value) {
        isEnableLodeMore = value;
    }

    private void init() {
        if (null == mRecyclerView) {
            mRecyclerView = getView().findViewById(R.id.mRecyclerView);
            if (null != getAdapter()) {
                RecyclerView.LayoutManager mLayoutManager =
                        null==getLayoutManager()?new FixLinearLayoutManager(getContext()):getLayoutManager();
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = getAdapter();
                mAdapter.setEnableLoadMore(isEnableLodeMore);
                if (isEnableLodeMore) {
                    mAdapter.setLoadMoreView(new CustomerLoadMoreView());
                    mAdapter.setOnLoadMoreListener(this::loadMoreData, mRecyclerView);
                }
                mRecyclerView.setAdapter(mAdapter);
                mEmptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRecyclerView.getParent(), false);
                mEmptyView.setOnClickListener(v -> refreshData());
                if (mAdapter.getData().isEmpty()) {
                    mAdapter.setEmptyView(mEmptyView);
                }
            }
        }
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            mSwipeRefreshLayout.setOnRefreshListener(() -> refreshData());
        }
    }

    @Override
    public void showErrorMessage(int code) {
        if (code == 0) {

        } else {
            DialogUtils.showToast(getContext(),""+code);
        }
    }

    /**
     *  对数据加载结果进行统一处理
     */
    protected void setLoadDataResult(List list, int type) {
        switch (type) {
            case REFRESH_SUCCESS:
                mAdapter.setNewData(list);
                break;
            case REFRESH_FAILURE:
                mAdapter.notifyDataSetChanged();
                mAdapter.loadMoreFail();
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
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mAdapter.getData().isEmpty()) {
            mAdapter.setEmptyView(mEmptyView);
        }
    }

    @Override
    public void refreshFailure() {
        if (mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    protected View getChildView(int position) {
        RecyclerView.LayoutManager mLayoutManager = mRecyclerView.getLayoutManager();
        return mLayoutManager.findViewByPosition(position);
    }

    public void jumpTop() {
        if (null != mRecyclerView) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    /**
     *  加载Presenter
     * @return
     */
    protected T initPresenter() {
        return null;
    }


    /**
     *  获取适配器
     * @return
     */
    protected BaseQuickAdapter getAdapter() { return null; }


    /**
     *  加载需要的参数
     * @param bundle
     */
    protected void initArgs(Bundle bundle) { }

    /**
     *  设置布局管理器
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    public AppCompatTextView getEmptyTextView() {
        return mEmptyView.findViewById(R.id.empty_txt);
    }

    @Override
    public void setPresenter(T presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showNoMoreData() {
        if (mAdapter != null) {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
