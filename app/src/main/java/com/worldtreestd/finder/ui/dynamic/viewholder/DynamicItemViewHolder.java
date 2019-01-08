package com.worldtreestd.finder.ui.dynamic.viewholder;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.common.widget.multipicture.MultiPictureLayout;

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
    @BindView(R.id.publisher_nickname)
    TextView publisherName;
    @BindView(R.id.publish_time)
    TextView publishTime;
    @BindView(R.id.dynamic_content)
    TextView dynamicContent;


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
}
