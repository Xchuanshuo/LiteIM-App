package com.legend.liteim.ui.dynamic.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.legend.liteim.R;
import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.widget.CommonPopupWindow;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.widget.FixLinearLayoutManager;
import com.legend.liteim.contract.dynamic.DynamicContract;
import com.legend.liteim.presenter.dynamic.DynamicPresenter;
import com.legend.liteim.ui.dynamic.adapter.DynamicItemAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;

import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 18-5-30.
 * @description
 */
public class DynamicFragment extends BaseFragment<DynamicContract.Presenter, DynamicItemAdapter>
    implements DynamicContract.View {

    private int curPosition = -1;
    private int currentPage = 1;
    private List<CommonMultiBean<Dynamic>> beanList = new ArrayList<>();

    @Override
    protected DynamicContract.Presenter initPresenter() {
        return new DynamicPresenter(this);
    }

    @Override
    protected DynamicItemAdapter getAdapter() {
        return new DynamicItemAdapter(beanList, _mActivity);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new FixLinearLayoutManager(mView.getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        getEmptyTextView().setText(getString(R.string.empty_dynamic_list));
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mAdapter.setSelectorListener((v, position) -> {
            CommonPopupWindow mPopupWindow = CommonPopupWindow.getInstance()
                    .buildPopupWindow(v, -30, -27).onlyHideDelete();
            curPosition = position;
            Dynamic dynamic = beanList.get(curPosition).getData();
            mPopupWindow.setReportListener(v1 -> {
                DialogUtils.showToast(v1.getContext(), "举报了动态"+dynamic.getId());
            });
            mPopupWindow.setShareListener(v1 -> {
                DialogUtils.showToast(v1.getContext(), "分享了动态"+dynamic.getId());
            });
            mPopupWindow.show();
        });
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!_mActivity.isDestroyed()) {
//                        Glide.with(_mActivity).resumeRequests();
//                    }
//                } else {
//                    Glide.with(_mActivity).pauseRequests();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                // 移除小窗口
                JZVideoPlayer jzvd = view.findViewById(R.id.video_player);

                if (jzvd != null && JZUtils.dataSourceObjectsContainsUri(jzvd.dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                    JZVideoPlayer currentJzvd = JZVideoPlayerManager.getCurrentJzvd();
                    if (currentJzvd != null && currentJzvd.currentScreen != JZVideoPlayer.SCREEN_WINDOW_FULLSCREEN) {
                        JZVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });
    }

    @Override
    public void refreshData() {
        beanList.clear();
        if (getUserVisibleHint()) {
            this.currentPage = 1;
            mPresenter.getDynamicData(currentPage);
        }
    }

    @Override
    public void loadMoreData() {
        if (getUserVisibleHint()) {
            currentPage++;
            mPresenter.getDynamicData(currentPage);
        }
    }

    @Override
    public void showNoMoreData() {
        DialogUtils.showToast(getContext(), "没有更多数据!");
        mAdapter.loadMoreEnd();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (!JZVideoPlayer.backPress()) {
            return super.onBackPressedSupport();
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 当前Fragment隐藏时移除正在播放的视频
        if (hidden) {
            JZVideoPlayer.releaseAllVideos();
        }
        Log.d(this.getClass().getName(), "Hidden:" +hidden);
    }

    @Override
    public void showData(List<CommonMultiBean<Dynamic>> multiBeanList) {
        beanList.addAll(multiBeanList);
        setLoadDataResult(beanList, REFRESH_SUCCESS);
    }
}
