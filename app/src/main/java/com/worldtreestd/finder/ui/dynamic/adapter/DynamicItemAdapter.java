package com.worldtreestd.finder.ui.dynamic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.bean.DynamicBean;
import com.worldtreestd.finder.common.utils.IntentUtils;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.common.widget.multipicture.MultiPictureLayout;
import com.worldtreestd.finder.ui.dynamic.viewholder.DynamicItemViewHolder;

import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

import static cn.jzvd.JZVideoPlayer.SCREEN_WINDOW_LIST;
import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_PICTURE;
import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_VIDEO;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description
 */
public class DynamicItemAdapter extends BaseMultiItemQuickAdapter<CommonMultiBean<DynamicBean>, DynamicItemViewHolder> implements BaseQuickAdapter.OnItemChildClickListener {

    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private MultiPictureLayout multiPictureLayout;
    private MultiPictureLayout.Callback callback;
    private Context mContext;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public DynamicItemAdapter(List<CommonMultiBean<DynamicBean>> data, Context context) {
        super(data);
        this.mContext = context;
        addItemType(DYNAMIC_ITEM_WORD_PICTURE, R.layout.item_dynamic_word_picture);
        addItemType(DYNAMIC_ITEM_WORD_VIDEO, R.layout.item_dynamic_word_video);
    }

    @Override
    protected void convert(DynamicItemViewHolder helper, CommonMultiBean<DynamicBean> item) {
        helper.addOnClickListener(R.id.dynamic_item);
        helper.itemView.setOnClickListener(v -> {
            Intent intent = IntentUtils.createDynamicIntent(mContext, item.getData());
            CircleImageView portrait = helper.getView(R.id.publisher_portrait);
            TextView publisherName = helper.getView(R.id.publisher_nickname);
            TextView publishTime = helper.getView(R.id.publish_time);
            TextView dynamicContent = helper.getView(R.id.dynamic_content);
            MultiPictureLayout multiPicture = helper.getView(R.id.pictures);
            JZVideoPlayerStandard videoPlayer = helper.getView(R.id.video_player);
            Pair<View, String> pair1 = Pair.create(portrait, v.getContext().getString(R.string.portrait));
            Pair<View, String> pair2 = Pair.create(publishTime, v.getContext().getString(R.string.publish_time));
            Pair<View, String> pair3 = Pair.create(publisherName, v.getContext().getString(R.string.publisher_name));
            Pair<View, String> pair4 = Pair.create(dynamicContent, v.getContext().getString(R.string.dynamic_content));
            Pair<View, String> pair5;
            if (helper.getItemViewType() == DYNAMIC_ITEM_WORD_PICTURE) {
                pair5 = Pair.create(multiPicture, v.getContext().getString(R.string.dynamic_picture_urlList));
            } else {
                pair5 = Pair.create(videoPlayer, v.getContext().getString(R.string.dynamic_video_url));
            }
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) mContext, pair1, pair2, pair3, pair4, pair5);
            mContext.startActivity(intent, options.toBundle());
        });
        helper.setText(R.id.publisher_nickname, item.getData().getName());
        helper.setText(R.id.dynamic_content, item.getData().getContent());
        switch (helper.getItemViewType()) {
            case DYNAMIC_ITEM_WORD_PICTURE:
                helper.setText(R.id.publisher_nickname, item.getData().getName());
                helper.setText(R.id.dynamic_content, item.getData().getContent());
                if (item.getData().getImageUrlList() != null) {
                    setImageData(helper, item.getData().getImageUrlList());
                }
                break;
            case DYNAMIC_ITEM_WORD_VIDEO:
                videoPlayerConfig(helper, item.getData());
                break;
            default: break;
        }
    }

    // 视频配置
    private void videoPlayerConfig(DynamicItemViewHolder viewHolder, DynamicBean data) {
        jzVideoPlayerStandard = viewHolder.getView(R.id.video_player);
        jzVideoPlayerStandard.setUp(data.getVideoUrl(), SCREEN_WINDOW_LIST);
        Glide.with(jzVideoPlayerStandard.getContext()).
                load(data.getVideoImageUrl()).into(jzVideoPlayerStandard.thumbImageView);
    }

    // 图片加载
    private void setImageData(DynamicItemViewHolder viewHolder, List<String> urlList) {
        multiPictureLayout = viewHolder.getView(R.id.pictures);
        multiPictureLayout.setCallback(callback);
        multiPictureLayout.set(urlList, urlList);
    }

    public DynamicItemAdapter setCallback(MultiPictureLayout.Callback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
    }
}
