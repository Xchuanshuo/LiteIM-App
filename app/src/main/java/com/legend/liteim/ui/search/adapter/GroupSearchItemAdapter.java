package com.legend.liteim.ui.search.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.liteim.R;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.common.net.FinderApiService;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.db.GroupHelper;
import com.legend.liteim.event.GroupJoinEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.model.ExtraDisposableManager;
import com.legend.liteim.model.GroupAddModel;
import com.legend.liteim.ui.contacts.GroupInfoActivity;

import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public class GroupSearchItemAdapter extends BaseQuickAdapter<ChatGroup, BaseViewHolder> {

    private Context mContext;
    private static int curPosition = 0;
    private ArrayMap<Integer, ImageView> mAddImgMap = new ArrayMap<>();
    private ExtraDisposableManager manager = ExtraDisposableManager.getInstance();
    private GroupAddModel groupAddModel;

    public GroupSearchItemAdapter(int layoutResId, @Nullable List<ChatGroup> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
        this.groupAddModel = new GroupAddModel(mContext);
        registerEvent();
    }

    private void registerEvent() {
        manager.addDisposable(RxBus.getDefault().toObservable(GroupJoinEvent.class)
                .subscribe(groupJoinEvent -> {
                    if (groupJoinEvent.isSuccess()) {
                        showAddSuccess();
                    } else {
                        showAddFailure();
                    }
                }));
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatGroup item) {
        helper.setText(R.id.tv_username, item.getName());
        helper.setText(R.id.tv_signature, item.getDescription());
        if (!item.getPortrait().startsWith("http")) {
            item.setPortrait(FinderApiService.BASE_URL + item.getPortrait());
        }
        GlideUtil.loadImage(mContext, item.getPortrait(), helper.getView(R.id.portrait));
        LinearLayout mLayout = helper.getView(R.id.layout_main);
        mLayout.setOnClickListener(v -> {
            curPosition = helper.getAdapterPosition();
            GroupInfoActivity.show(mContext, item);
        });
        if (mAddImgMap.containsKey(curPosition)) {
            return;
        }
        ImageView mAddImage = helper.getView(R.id.img_add);
        if (item.isJoined()) {
            mAddImage.setBackgroundColor(helper.itemView.getResources().getColor(R.color.gray));
            mAddImage.setImageResource(R.drawable.ic_done);
        } else {
            mAddImage.setBackgroundColor(helper.itemView.getResources().getColor(R.color.colorPrimary));
            mAddImage.setImageResource(R.drawable.ic_add);
        }
        mAddImage.setEnabled(!item.isJoined());
        mAddImage.setOnClickListener(v -> {
            curPosition = helper.getAdapterPosition();
            if (mAddImage.isEnabled()) {
                // 暂时设置为不可用
                mAddImage.setEnabled(false);
                // 显示loading动画
                DialogUtils.showLoading(mContext, mAddImage);
                // 网络请求
                groupAddModel.joinGroup(item.getId());
                // 放入map方便修改状态
                mAddImgMap.put(curPosition, mAddImage);
            }
        });
    }

    private void showAddSuccess() {
        if (curPosition != -1 && curPosition < getData().size()) {
            ChatGroup chatGroup = getData().get(curPosition);
            chatGroup.setJoined(true);
            notifyItemChanged(curPosition, 0);
            // 添加成功后从Map中移除
            mAddImgMap.remove(curPosition);
            GroupHelper.getInstance().saveOrUpdate(chatGroup);
        }
    }

    private void showAddFailure() {
        if (curPosition != -1 && curPosition < getData().size()) {
            ImageView mAddImage = mAddImgMap.get(curPosition);
            if (mAddImage != null &&
                    mAddImage.getDrawable() instanceof LoadingCircleDrawable) {
                LoadingDrawable drawable = (LoadingDrawable) mAddImage.getDrawable();
                // 添加失败时停止动画
                drawable.setProgress(1);
                drawable.stop();
                mAddImage.setEnabled(true);
            }
            ChatGroup chatGroup = getData().get(curPosition);
            chatGroup.setJoined(false);
            notifyItemChanged(curPosition, 0);
            GroupHelper.getInstance().saveOrUpdate(chatGroup);
        }
    }
}
