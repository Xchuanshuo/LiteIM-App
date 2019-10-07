package com.legend.liteim.ui.dynamic.viewholder;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.liteim.R;
import com.legend.liteim.common.widget.CircleImageView;
import com.legend.liteim.common.widget.multipicture.MultiPictureLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description
 */
public class DynamicItemViewHolder extends BaseViewHolder {

    @BindView(R.id.pictures)
    protected MultiPictureLayout multiPictureLayout;
    @BindView(R.id.video_player)
    JZVideoPlayerStandard jzVideoPlayerStandard;
    @BindView(R.id.publisher_portrait)
    CircleImageView publisherPortrait;
    @BindView(R.id.img_selector)
    AppCompatImageView mSelectorImg;
    @BindView(R.id.tv_publisher_nickname)
    TextView publisherName;
    @BindView(R.id.tv_publish_time)
    TextView publishTime;
    @BindView(R.id.tv_dynamic_content)
    TextView dynamicContent;
    @BindView(R.id.tv_comment_num)
    TextView dynamicCommentNum;
    @BindView(R.id.tv_watch_num)
    TextView dynamicWatchNum;
    @BindView(R.id.tv_collect_num)
    TextView dynamicCollectNum;


    public DynamicItemViewHolder(View view) {
        super(view);
        ButterKnife.bind(view);
    }



    public MultiPictureLayout getMultiPictureLayout() {
        return multiPictureLayout;
    }

    public JZVideoPlayerStandard getJzVideoPlayerStandard() {
        return jzVideoPlayerStandard;
    }

    public CircleImageView getPublisherPortrait() {
        return publisherPortrait;
    }

    public TextView getPublisherName() {
        return publisherName;
    }

    public TextView getPublishTime() {
        return publishTime;
    }

    public TextView getDynamicContent() {
        return dynamicContent;
    }

    public TextView getDynamicCommentNum() {
        return dynamicCommentNum;
    }

    public TextView getDynamicWatchNum() {
        return dynamicWatchNum;
    }

    public TextView getDynamicCollectNum() {
        return dynamicCollectNum;
    }


}
