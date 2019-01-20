package com.worldtreestd.finder.ui.dynamic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.google.gson.Gson;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.utils.DataUtils;
import com.worldtreestd.finder.common.utils.GlideUtil;
import com.worldtreestd.finder.common.utils.IntentUtils;
import com.worldtreestd.finder.common.widget.CircleImageView;
import com.worldtreestd.finder.common.widget.multipicture.MultiPictureLayout;
import com.worldtreestd.finder.common.widget.picturewatcher.PreviewActivity;
import com.worldtreestd.finder.ui.dynamic.viewholder.DynamicItemViewHolder;
import com.worldtreestd.finder.ui.userinfo.UserInfoActivity;

import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayerStandard;

import static cn.jzvd.JZVideoPlayer.SCREEN_WINDOW_LIST;
import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_PICTURE;
import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_VIDEO;
import static com.worldtreestd.finder.common.utils.Constant.LOOK_USER;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description
 */
public class DynamicItemAdapter extends BaseMultiItemQuickAdapter<CommonMultiBean<Dynamic>, DynamicItemViewHolder>  {

    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private MultiPictureLayout multiPictureLayout;
    private MultiPictureLayout.Callback callback;
    private SelectorListener selectorListener;
    private Context mContext;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public DynamicItemAdapter(List<CommonMultiBean<Dynamic>> data, Context context) {
        super(data);
        this.mContext = context;
        addItemType(DYNAMIC_ITEM_WORD_PICTURE, R.layout.item_dynamic_word_picture);
        addItemType(DYNAMIC_ITEM_WORD_VIDEO, R.layout.item_dynamic_word_video);
    }

    @Override
    protected void convert(DynamicItemViewHolder helper, CommonMultiBean<Dynamic> item) {
        helper.addOnClickListener(R.id.cardView);
        final Dynamic dynamic = item.getData();
        helper.itemView.setOnClickListener(v -> {
            Intent intent = IntentUtils.createDynamicIntent(mContext,item.getData());
            CircleImageView portrait = helper.getView(R.id.publisher_portrait);
            portrait.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                User user = new User();
                user.setId(item.getData().getUserId());
                user.setUsername(item.getData().getUsername());
                user.setPortrait(item.getData().getPortrait());
                user.setBackground(item.getData().getPortrait());
                bundle.putSerializable(LOOK_USER, user);
                UserInfoActivity.come(mContext, bundle);
            });
            AppCompatImageView mSelector = helper.getView(R.id.img_selector);
            mSelector.setOnClickListener(v1 -> {
                if (selectorListener!=null) {
                    selectorListener.onSelectorClickListener(v1, helper.getAdapterPosition());
                }
            });
            TextView publisherName = helper.getView(R.id.tv_publisher_nickname);
            TextView publishTime = helper.getView(R.id.tv_publish_time);
            TextView dynamicContent = helper.getView(R.id.tv_dynamic_content);
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
        GlideUtil.loadImage(mContext, item.getData().getPortrait(), helper.getView(R.id.publisher_portrait));
        helper.setText(R.id.tv_publisher_nickname, item.getData().getUsername());
        helper.setText(R.id.tv_dynamic_content, item.getData().getContent());
        helper.setText(R.id.tv_publish_time, item.getData().getCreateTime().replace("T", " "));
        helper.setText(R.id.tv_comment_num, item.getData().getCommentNum()+"条评论");
        helper.setText(R.id.tv_watch_num, item.getData().getWatchNum()+"次围观");
        helper.setText(R.id.tv_collect_num, item.getData().getCollectNum()+"收藏");
        Gson gson = new Gson();
        switch (helper.getItemViewType()) {
            case DYNAMIC_ITEM_WORD_PICTURE:
                if (!TextUtils.isEmpty(dynamic.getUrls())) {
                    List<String> urlList = gson.fromJson(dynamic.getUrls(), List.class);
                    setImageData(helper, DataUtils.totalListUrl(urlList));
                }
                break;
            case DYNAMIC_ITEM_WORD_VIDEO:
                if (!TextUtils.isEmpty(dynamic.getUrls())) {
                    Map<String, String> map = gson.fromJson(dynamic.getUrls(), Map.class);
                    videoPlayerConfig(helper, DataUtils.totalMapUrl(map));
                }
                break;
            default: break;
        }
    }

    private void videoPlayerConfig(DynamicItemViewHolder viewHolder, Map<String, String> urls) {
        jzVideoPlayerStandard = viewHolder.getView(R.id.video_player);
        jzVideoPlayerStandard.setUp(urls.get("url"), SCREEN_WINDOW_LIST);
        Glide.with(jzVideoPlayerStandard.getContext()).
                load(urls.get("coverPath")).into(jzVideoPlayerStandard.thumbImageView);
    }

    /** 图片加载 **/
    private void setImageData(DynamicItemViewHolder viewHolder, List<String> urlList) {
        multiPictureLayout = viewHolder.getView(R.id.pictures);
        multiPictureLayout.setCallback((position, urlList1) -> PreviewActivity.come(mContext, urlList1, position));
        multiPictureLayout.set(urlList, urlList);
    }

    public DynamicItemAdapter setCallback(MultiPictureLayout.Callback callback) {
        this.callback = callback;
        return this;
    }



    public void setSelectorListener(SelectorListener selectorListener) {
        this.selectorListener = selectorListener;
    }

    public interface SelectorListener {
        void onSelectorClickListener(View v, int position);
    }
}
