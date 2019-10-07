package com.legend.liteim.ui.dynamic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.google.gson.Gson;
import com.legend.liteim.R;
import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.utils.DataUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.utils.IntentUtils;
import com.legend.liteim.common.widget.CircleImageView;
import com.legend.liteim.common.widget.multipicture.MultiPictureLayout;
import com.legend.liteim.common.widget.picturewatcher.PreviewActivity;
import com.legend.liteim.event.CollectEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.model.DynamicCollectModel;
import com.legend.liteim.model.ExtraDisposableManager;
import com.legend.liteim.ui.dynamic.viewholder.DynamicItemViewHolder;
import com.legend.liteim.ui.userinfo.UserInfoActivity;

import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayerStandard;

import static cn.jzvd.JZVideoPlayer.SCREEN_WINDOW_LIST;
import static com.legend.liteim.ui.release.ReleaseActivity.DYNAMIC_ITEM_WORD_PICTURE;
import static com.legend.liteim.ui.release.ReleaseActivity.DYNAMIC_ITEM_WORD_VIDEO;

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
    private DynamicCollectModel collectModel;
    private int curPosition = -1;
    ExtraDisposableManager manager = ExtraDisposableManager.getInstance();
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
        collectModel = new DynamicCollectModel(mContext);
        registerEvent();
    }

    private void registerEvent() {
        manager.addDisposable(RxBus.getDefault().toObservable(CollectEvent.class)
                .subscribe(event -> {
                    if (event.isCollected()) {
                        showCollectSuccess();
                    } else {
                        showUnCollectSuccess();
                    }
                }));
    }

    @Override
    protected void convert(DynamicItemViewHolder helper, CommonMultiBean<Dynamic> item) {
        helper.addOnClickListener(R.id.cardView);
        helper.addOnClickListener(R.id.img_collect);
        final Dynamic dynamic = item.getData();
        CircleImageView portrait = helper.getView(R.id.publisher_portrait);
        portrait.setOnClickListener(view -> {
            User user = new User();
            user.setId(dynamic.getUserId());
            user.setUsername(dynamic.getUsername());
            user.setPortrait(dynamic.getPortrait());
            user.setBackground(dynamic.getBackground());
            user.setSignature(dynamic.getSignature());
            UserInfoActivity.show(mContext, user);
        });
        AppCompatImageView mCollectImg = helper.getView(R.id.img_collect);
        mCollectImg.setOnClickListener(v -> {
            curPosition = helper.getAdapterPosition();
            if (!dynamic.isCollected()) {
                collectModel.collectDynamic(dynamic.getId());
            } else {
                collectModel.unCollectDynamic(dynamic.getId());
            }
        });
        helper.itemView.setOnClickListener(v -> {
            curPosition = helper.getAdapterPosition();
            AppCompatImageView mSelector = helper.getView(R.id.img_selector);
            mSelector.setOnClickListener(v1 -> {
                if (selectorListener!=null) {
                    selectorListener.onSelectorClickListener(v1, helper.getAdapterPosition());
                }
            });
            Intent intent = IntentUtils.createDynamicIntent(mContext,item.getData());
            mContext.startActivity(intent);
        });
        GlideUtil.loadImage(mContext, item.getData().getPortrait(), helper.getView(R.id.publisher_portrait));
        helper.setText(R.id.tv_publisher_nickname, item.getData().getUsername());
        helper.setText(R.id.tv_dynamic_content, item.getData().getContent());
        helper.setImageResource(R.id.img_collect, dynamic.isCollected()?R.drawable.ic_collect_solid: R.drawable.ic_collect_hollow);
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
        multiPictureLayout.setCallback((position, urlList1) -> PreviewActivity.show(mContext, urlList1, position));
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

    private void showCollectSuccess() {
        if (curPosition != -1 && curPosition < getData().size()) {
            Dynamic dynamic = getData().get(curPosition).getData();
            dynamic.setCollected(true);
            dynamic.setCollectNum(dynamic.getCollectNum() + 1);
            notifyItemChanged(curPosition, 0);
        }
    }

    private void showUnCollectSuccess() {
        if (curPosition != -1 && curPosition < getData().size()) {
            Dynamic dynamic = getData().get(curPosition).getData();
            dynamic.setCollected(false);
            dynamic.setCollectNum(dynamic.getCollectNum()>0?dynamic.getCollectNum() - 1:0);
            notifyItemChanged(curPosition, 0);
        }
    }


}
