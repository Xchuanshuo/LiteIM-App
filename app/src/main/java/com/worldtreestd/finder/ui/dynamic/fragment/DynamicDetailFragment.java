package com.worldtreestd.finder.ui.dynamic.fragment;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.CommentBean;
import com.worldtreestd.finder.common.utils.GlideUtil;
import com.worldtreestd.finder.common.utils.TestDataUtils;
import com.worldtreestd.finder.common.widget.multipicture.MultiPictureLayout;
import com.worldtreestd.finder.common.widget.picturewatcher.PreviewActivity;
import com.worldtreestd.finder.contract.dynamic.DynamicDetailContract;
import com.worldtreestd.finder.presenter.dynamic.DynamicDetailPresenter;
import com.worldtreestd.finder.ui.dynamic.adapter.DynamicCommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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
        implements DynamicDetailContract.View {

    MultiPictureLayout mMultiPictureLayout;
    TextView mDynamicContent, mUsernameTv, mPublishTimeTV;
    JZVideoPlayerStandard mJzVideoPlayerStandard;
    ImageView mPortraitImg;
    @BindView(R.id.img_collect)
    AppCompatImageView mCollectImg;
    private List<CommentBean> commentBeanList = new ArrayList<>();
    private Dynamic dynamic;
    View detailContentLayout;
    private boolean isCollected = false;

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
    protected void initWidget() {
        super.initWidget();
        this.detailContentLayout = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dynamic_content, null);
        mDynamicContent = detailContentLayout.findViewById(R.id.tv_dynamic_content);
        mMultiPictureLayout = detailContentLayout.findViewById(R.id.pictures);
        mJzVideoPlayerStandard = detailContentLayout.findViewById(R.id.video_player);
        mPortraitImg = detailContentLayout.findViewById(R.id.portrait);
        mUsernameTv = detailContentLayout.findViewById(R.id.tv_nickname);
        mPublishTimeTV = detailContentLayout.findViewById(R.id.tv_publish_time);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mAdapter.addHeaderView(detailContentLayout);
        Intent intent = _mActivity.getIntent();
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
        this.dynamic = (Dynamic) intent.getSerializableExtra(getString(R.string.dynamic));
        if (dynamic != null) {
            mUsernameTv.setText(dynamic.getUsername()+"");
            mPublishTimeTV.setText(dynamic.getCreateTime().replace("T"," ")+"");
            mDynamicContent.setText(dynamic.getContent()+"");
            this.isCollected = dynamic.isCollected();
            mCollectImg.setImageResource(isCollected?R.drawable.ic_collect_solid:R.drawable.ic_collect_hollow);
            GlideUtil.loadImage(getContext(), dynamic.getPortrait(), mPortraitImg);
        }
    }

    @OnClick(R.id.img_collect)
    public void collectEvent() {
        if (dynamic != null) {
            if (!isCollected) {
                mPresenter.collectDynamic(dynamic.getId());
            } else {
                mPresenter.unCollectDynamic(dynamic.getId());
            }
        }
    }




    @Override
    public boolean onBackPressedSupport() {
        if (JZVideoPlayer.backPress()) {
            return true;
        }
        JZVideoPlayer.releaseAllVideos();
        return false;
    }

    /** 图片加载 **/
    private void setImageData(List<String> urlList) {
        mMultiPictureLayout.set(urlList, urlList);
        mMultiPictureLayout.setCallback((position, urlList1) -> PreviewActivity.come(_mActivity, urlList1, position));
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

    @Override
    public void showCollectedSuccess() {
        isCollected = true;
        if (mCollectImg != null) {
            mCollectImg.setImageResource(R.drawable.ic_collect_solid);
        }
    }

    @Override
    public void showUnCollectedSuccess() {
        isCollected = false;
        if (mCollectImg != null) {
            mCollectImg.setImageResource(R.drawable.ic_collect_hollow);
        }
    }
}
