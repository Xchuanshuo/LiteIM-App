package com.worldtreestd.finder.ui.dynamic.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.CommentBean;
import com.worldtreestd.finder.common.utils.TestDataUtils;
import com.worldtreestd.finder.common.widget.multipicture.MultiPictureLayout;
import com.worldtreestd.finder.common.widget.picturewatcher.PreviewActivity;
import com.worldtreestd.finder.contract.dynamic.DynamicDetailContract;
import com.worldtreestd.finder.presenter.dynamic.DynamicDetailPresenter;
import com.worldtreestd.finder.ui.dynamic.adapter.DynamicCommentAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static cn.jzvd.JZVideoPlayer.SCREEN_WINDOW_LIST;
import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public class DynamicDetailFragment extends BaseFragment<DynamicDetailContract.Presenter>
    implements DynamicDetailContract.View, MultiPictureLayout.Callback  {

//    @BindView(R.id.pictures)
    MultiPictureLayout mMultiPictureLayout;
//    @BindView(R.id.dynamic_content)
    TextView mDynamicContent;
//    @BindView(R.id.video_player)
    JZVideoPlayerStandard mJzVideoPlayerStandard;
    private List<CommentBean> commentBeanList = new ArrayList<>();

    @Override
    protected DynamicDetailContract.Presenter initPresenter() {
        return new DynamicDetailPresenter(this);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new DynamicCommentAdapter(R.layout.item_comment, commentBeanList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic_detail;
    }

    @Override
    public void refreshData() {
        commentBeanList.clear();
        if (getUserVisibleHint()) {
            commentBeanList = TestDataUtils.getCommentList();
            setLoadDataResult(commentBeanList, REFRESH_SUCCESS);
        }
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        View detailContentLayout = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dynamic_content, null);
        mDynamicContent = detailContentLayout.findViewById(R.id.dynamic_content);
        mMultiPictureLayout = detailContentLayout.findViewById(R.id.pictures);
        mJzVideoPlayerStandard = detailContentLayout.findViewById(R.id.video_player);
        mAdapter.addHeaderView(detailContentLayout);
        Intent intent = _mActivity.getIntent();
        String content = intent.getStringExtra(getString(R.string.dynamic_content));
        mDynamicContent.setText(content+"");
        if (TextUtils.isEmpty(intent.getStringExtra(getString(R.string.dynamic_video_url)))) {
            List<String> imageUrlList = intent.getStringArrayListExtra(getString(R.string.dynamic_picture_urlList));
            setImageData(imageUrlList);
        } else {
            mMultiPictureLayout.setVisibility(View.GONE);
            mJzVideoPlayerStandard.setVisibility(View.VISIBLE);
            String videoUrl = intent.getStringExtra(getString(R.string.dynamic_video_url));
            String videoImageUrl = intent.getStringExtra(getString(R.string.dynamic_video_image_url));
            videoPlayerConfig(videoUrl, videoImageUrl);
        }
    }

    @Override
    public void onImageClickListener(int position, List<String> urlList) {
        PreviewActivity.come(_mActivity, urlList, position);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (JZVideoPlayer.backPress()) {
            return true;
        }
        JZVideoPlayer.releaseAllVideos();
        return false;
    }

    private void setImageData(List<String> urlList) {
        mMultiPictureLayout.setCallback(this::onImageClickListener);
        mMultiPictureLayout.set(urlList, urlList);
    }

    private void videoPlayerConfig(String videoUrl, String videoImageUrl) {
        mJzVideoPlayerStandard.setUp(videoUrl, SCREEN_WINDOW_LIST);
        Glide.with(this).
                load(videoImageUrl).into(mJzVideoPlayerStandard.thumbImageView);
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        Log.d(this.getClass().getName(), "onPause()");
    }
}
