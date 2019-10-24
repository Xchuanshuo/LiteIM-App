package com.legend.liteim.ui.dynamic.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.legend.liteim.R;
import com.legend.liteim.bean.Comment;
import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.MyApplication;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.common.utils.DataUtils;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.widget.CircleImageView;
import com.legend.liteim.common.widget.CommonPopupWindow;
import com.legend.liteim.common.widget.multipicture.MultiPictureLayout;
import com.legend.liteim.common.widget.picturewatcher.PreviewActivity;
import com.legend.liteim.contract.dynamic.DynamicDetailContract;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.presenter.dynamic.DynamicDetailPresenter;
import com.legend.liteim.ui.dynamic.adapter.DynamicCommentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import static cn.jzvd.JZVideoPlayer.SCREEN_WINDOW_LIST;
import static com.legend.liteim.common.utils.Constant.LOOK_DYNAMIC;
import static com.legend.liteim.ui.release.ReleaseActivity.DYNAMIC_ITEM_WORD_PICTURE;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public class DynamicDetailFragment extends BaseFragment<DynamicDetailContract.Presenter, DynamicCommentAdapter>
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
    private int currentPosition = -1;

    @Override
    protected DynamicDetailContract.Presenter initPresenter() {
        return new DynamicDetailPresenter(this);
    }

    @Override
    protected DynamicCommentAdapter getAdapter() {
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
        this.detailContentLayout = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_dynamic_content, null);
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
        // 数据显示
        if (dynamic != null) {
            mUsernameTv.setText(dynamic.getUsername());
            mPublishTimeTV.setText(dynamic.getCreateTime().replace("T"," "));
            mDynamicContent.setText(dynamic.getContent());
            this.isCollected = dynamic.isCollected();
            mCollectImg.setImageResource(isCollected?R.drawable.ic_collect_solid:R.drawable.ic_collect_hollow);
            GlideUtil.loadImage(MyApplication.getInstance(), dynamic.getPortrait(), mPortraitImg);
        }
        // 图片或者视频url
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
        // 设置点赞事件监听
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            currentPosition = position;
            Comment comment = commentList.get(currentPosition);
            switch (view.getId()) {
                case R.id.img_praise:
                    if (!comment.isFavor()) {
                        mPresenter.praiseComment(comment.getId());
                    } else {
                        mPresenter.unPraiseComment(comment.getId());
                    }
                    break;
                case R.id.img_menu:
                    CommonPopupWindow mPopupWindow = CommonPopupWindow.getInstance()
                            .buildPopupWindow(view, -60, -40).onlyHideShare();
                    User operateUser = UserHelper.getInstance().getCurrentUser();
                    if (operateUser==null || !operateUser.getId().equals(comment.getUserId())) {
                        mPopupWindow.hideDelete();
                    }
                    mPopupWindow.setReportListener(view1 -> {
                        DialogUtils.showToast(view1.getContext(), "举报了评论"+comment.getId());
                    });
                    mPopupWindow.setDeleteListener(view1 -> {
                        mPresenter.deleteComment(comment.getId());
                        mPopupWindow.dismiss();
                    });
                    mPopupWindow.show();
                    break;
                default: break;
            }
        });
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
            mAdapter.setHeaderAndEmpty(true);
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
    public void deleteCommentSuccess(String msg) {
        try {
            mRecyclerView.stopScroll();
            mAdapter.getData().remove(currentPosition);
            mAdapter.notifyItemRangeRemoved(currentPosition + 1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void praiseCommentSuccess(String msg) {
        Comment comment = commentList.get(currentPosition);
        comment.setFavor(true);
        comment.setFavorNum(comment.getFavorNum()+1);
        mAdapter.notifyItemChanged(currentPosition+1, 0);
    }

    @Override
    public void unPraiseCommentSuccess() {
        Comment comment = commentList.get(currentPosition);
        comment.setFavor(false);
        comment.setFavorNum(comment.getFavorNum()-1);
        mAdapter.notifyItemChanged(currentPosition+1, 0);
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
        mMultiPictureLayout.setCallback((position, urlList1) -> PreviewActivity.show(_mActivity, urlList1, position));
    }

    private void videoPlayerConfig(String videoUrl, String videoImageUrl) {
        mJzVideoPlayerStandard.setUp(videoUrl, SCREEN_WINDOW_LIST);
        Glide.with(this).
                load(videoImageUrl).into(mJzVideoPlayerStandard.thumbImageView);
    }
}
