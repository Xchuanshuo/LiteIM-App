package com.worldtreestd.finder.ui.dynamic.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.Comment;
import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.utils.DataUtils;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.utils.GlideUtil;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.common.widget.multipicture.MultiPictureLayout;
import com.worldtreestd.finder.common.widget.picturewatcher.PreviewActivity;
import com.worldtreestd.finder.contract.dynamic.DynamicDetailContract;
import com.worldtreestd.finder.presenter.dynamic.DynamicDetailPresenter;
import com.worldtreestd.finder.ui.dynamic.adapter.DynamicCommentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static cn.jzvd.JZVideoPlayer.SCREEN_WINDOW_LIST;
import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_PICTURE;
import static com.worldtreestd.finder.common.utils.Constant.LOOK_DYNAMIC;

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
    CircleImageView mPortraitImg;
    @BindView(R.id.img_collect)
    AppCompatImageView mCollectImg;
    @BindView(R.id.edt_comment)
    AppCompatEditText mCommentEdt;
    private List<Comment> commentList = new ArrayList<>();
    private Dynamic dynamic;
    View detailContentLayout;
    private boolean isCollected = false;
    private int currentPage = -1;

    @Override
    protected DynamicDetailContract.Presenter initPresenter() {
        return new DynamicDetailPresenter(this);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new DynamicCommentAdapter(R.layout.item_comment, commentList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic_detail;
    }

    public static DynamicDetailFragment newInstance(Dynamic dynamic) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOOK_DYNAMIC, dynamic);
        DynamicDetailFragment fragment = new DynamicDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        this.dynamic = (Dynamic) bundle.getSerializable(LOOK_DYNAMIC);
    }

    @Override
    public void refreshData() {
        commentList.clear();
        this.currentPage = 1;
        mPresenter.commentList(dynamic.getId(), currentPage);
    }

    @Override
    public void loadMoreData() {
        currentPage++;
        mPresenter.commentList(dynamic.getId(), currentPage);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        this.detailContentLayout = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dynamic_content, mRecyclerView);
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
        if (dynamic != null) {
            mUsernameTv.setText(dynamic.getUsername()+"");
            mPublishTimeTV.setText(dynamic.getCreateTime().replace("T"," ")+"");
            mDynamicContent.setText(dynamic.getContent()+"");
            this.isCollected = dynamic.isCollected();
            mCollectImg.setImageResource(isCollected?R.drawable.ic_collect_solid:R.drawable.ic_collect_hollow);
            GlideUtil.loadImage(MyApplication.getInstance(), dynamic.getPortrait(), mPortraitImg);
        }
        Gson gson = new Gson();
        if (dynamic.getType() == DYNAMIC_ITEM_WORD_PICTURE) {
            List<String> urlList = gson.fromJson(dynamic.getUrls(), List.class);
            setImageData(DataUtils.totalListUrl(urlList));
        } else {
            mMultiPictureLayout.setVisibility(View.GONE);
            mJzVideoPlayerStandard.setVisibility(View.VISIBLE);
            Map<String, String> urlMap =
                    DataUtils.totalMapUrl(gson.fromJson(dynamic.getUrls(), Map.class));
            videoPlayerConfig(urlMap.get("url"), urlMap.get("coverPath"));
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

    @OnClick(R.id.img_send)
    public void sendComment() {
        String content = mCommentEdt.getText().toString();
        if (TextUtils.isEmpty(content)) {
            DialogUtils.showToast(getContext(), "评论内容不能为空");
            return;
        }
        mPresenter.releaseComment(dynamic.getId(), content);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (JZVideoPlayer.backPress()) {
            return true;
        }
        JZVideoPlayer.releaseAllVideos();
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        Log.d(this.getClass().getName(), "onPause()");
    }

    @Override
    public void showCommentData(List<Comment> comments) {
        commentList.addAll(comments);
        if (commentList.size() == 0) {
            getEmptyTextView().setText("暂无评论,赶快来抢沙发");
            return;
        }
        mAdapter.setNewData(commentList);
    }

    @Override
    public void showCommentSuccess(String msg) {
        DialogUtils.showToast(getContext(), msg);
        mCommentEdt.setText("");
        refreshData();
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
}
