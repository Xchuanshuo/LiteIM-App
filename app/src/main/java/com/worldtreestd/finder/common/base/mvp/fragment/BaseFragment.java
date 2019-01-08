package com.worldtreestd.finder.common.base.mvp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.widget.CustomerLoadMoreView;
import com.worldtreestd.finder.contract.base.BaseContract;

import java.util.List;

import butterknife.BindView;

import static com.worldtreestd.finder.common.base.mvp.StatusType.LOAD_MORE_FAILURE;
import static com.worldtreestd.finder.common.base.mvp.StatusType.LOAD_MORE_SUCCESS;
import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_FAILURE;
import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_SUCCESS;

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
    private Dialog mDialog;
    protected BaseQuickAdapter mAdapter;
    private boolean isRefreshing = false;
    @Nullable
    @BindView(R.id.mSwipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initPresenter();
        initArgs(getArguments());
    }

    @Override
    protected void initEventAndData() {
        init();
        if (!isRefreshing) {
            refreshData();
        }
        isRefreshing = !isRefreshing;
    }

    private void init() {
        if (null == mRecyclerView) {
            mRecyclerView = getView().findViewById(R.id.mRecyclerView);
            if (null != getAdapter()) {
                RecyclerView.LayoutManager mLayoutManager =
                        null==getLayoutManager()?new LinearLayoutManager(getContext()):getLayoutManager();
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = getAdapter();
                mAdapter.setLoadMoreView(new CustomerLoadMoreView());
                mAdapter.setEnableLoadMore(true);
                mRecyclerView.setAdapter(mAdapter);
                mEmptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) mRecyclerView.getParent(), false);
                mEmptyView.setOnClickListener(v -> refreshData());
                if (mAdapter.getData().isEmpty()) {
                    mAdapter.setEmptyView(mEmptyView);
                }
            }
        }
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setOnRefreshListener(() -> refreshData());
        }

        mDialog = DialogUtils.showLoadingDialog(_mActivity,getString(R.string.common_loading));
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
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
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case REFRESH_FAILURE:
                mAdapter.notifyDataSetChanged();
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mAdapter.loadMoreFail();
                break;
            case LOAD_MORE_SUCCESS:
                if (list != null) {
                    mAdapter.addData(list);
                    mAdapter.loadMoreComplete();
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case LOAD_MORE_FAILURE:
                mAdapter.loadMoreFail();
                break;
            default: break;
        }
        hideLoading();
        if (mAdapter.getData().isEmpty()) {
            mAdapter.setEmptyView(mEmptyView);
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
    protected abstract T initPresenter();

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
        AppCompatTextView textView = mEmptyView.findViewById(R.id.empty_txt);
        ProgressBar progressBar = mEmptyView.findViewById(R.id.mProgressBar);
        AppCompatTextView retryTextView = mEmptyView.findViewById(R.id.tv_retry);
        progressBar.setVisibility(View.GONE);
        retryTextView.setVisibility(View.INVISIBLE);
        return textView;
    }

    @Override
    public void setPresenter(T presenter) {
        this.mPresenter = presenter;
    }
}
