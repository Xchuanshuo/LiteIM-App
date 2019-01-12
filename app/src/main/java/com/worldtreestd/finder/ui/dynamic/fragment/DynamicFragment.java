package com.worldtreestd.finder.ui.dynamic.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.bean.DynamicBean;
import com.worldtreestd.finder.common.utils.ImageDisposeUtils;
import com.worldtreestd.finder.common.utils.TestDataUtils;
import com.worldtreestd.finder.common.widget.multipicture.MultiPictureLayout;
import com.worldtreestd.finder.contract.dynamic.DynamicContract;
import com.worldtreestd.finder.presenter.dynamic.DynamicPresenter;
import com.worldtreestd.finder.ui.dynamic.adapter.DynamicItemAdapter;

import java.util.ArrayList;
import java.util.List;

import byc.imagewatcher.ImageWatcher;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;

import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 18-5-30.
 * @description
 */
public class DynamicFragment extends BaseFragment<DynamicContract.Presenter>
    implements DynamicContract.View, MultiPictureLayout.Callback {

    ImageWatcher mImageWatcher;
    private List<CommonMultiBean<DynamicBean>> dynamicBeanList = new ArrayList<>();

    @Override
    protected DynamicContract.Presenter initPresenter() {
        return new DynamicPresenter(this);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new DynamicItemAdapter(dynamicBeanList, _mActivity).setCallback(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }


    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mImageWatcher = ImageDisposeUtils.getWatcher(_mActivity);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!_mActivity.isDestroyed()) {
                        Glide.with(_mActivity).resumeRequests();
                    }
                } else {
                    Glide.with(_mActivity).pauseRequests();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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
        dynamicBeanList.clear();
        if (getUserVisibleHint()) {
            dynamicBeanList = TestDataUtils.getDynamicData();
            setLoadDataResult(dynamicBeanList, REFRESH_SUCCESS);
        }
    }

    @Override
    public void onImageClickListener(ImageView i, List<ImageView> imageGroupList, List<String> urlList) {
        mImageWatcher.show(i, imageGroupList, urlList);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (!mImageWatcher.handleBackPressed() && !JZVideoPlayer.backPress()) {
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
    public void onPause() {
        super.onPause();
        Log.d(this.getClass().getName(), "onPause()");
    }


}
