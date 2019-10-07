package com.legend.liteim.ui.search.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.event.UserAddEvent;
import com.legend.liteim.model.ExtraDisposableManager;
import com.legend.liteim.model.UserAddModel;
import com.legend.liteim.ui.userinfo.adapter.UserAdapter;

import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class UserSearchItemAdapter extends UserAdapter {

    private UserAddModel userAddModel;
    private Context mContext;
    private ArrayMap<Integer, ImageView> mAddImgMap = new ArrayMap<>();
    private ExtraDisposableManager manager = ExtraDisposableManager.getInstance();

    public UserSearchItemAdapter(int layoutResId, @Nullable List<User> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
        userAddModel = new UserAddModel(mContext);
        registerEvent();
    }

    private void registerEvent() {
        manager.addDisposable(RxBus.getDefault().toObservable(UserAddEvent.class)
                .subscribe(userAddEvent -> {
                    if (userAddEvent.isSuccess()) {
                        showAddSuccess();
                    } else {
                        // 进入好友资料卡后删除好友 也调用此方法进行处理
                        showAddFailure();
                    }
                }));
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        super.convert(helper, item);
        if (mAddImgMap.containsKey(curPosition)) {
            return;
        }
        ImageView mAddImage = helper.getView(R.id.img_add);
        if (item.isFriend()) {
            mAddImage.setBackgroundColor(helper.itemView.getResources().getColor(R.color.gray));
            mAddImage.setImageResource(R.drawable.ic_done);
        } else {
            mAddImage.setBackgroundColor(helper.itemView.getResources().getColor(R.color.colorPrimary));
            mAddImage.setImageResource(R.drawable.ic_add);
        }
        mAddImage.setEnabled(!item.isFriend());
        mAddImage.setOnClickListener(v -> {
            curPosition = helper.getAdapterPosition();
            if (mAddImage.isEnabled()) {
                // 暂时设置为不可用
                mAddImage.setEnabled(false);
                // 显示loading动画
                DialogUtils.showLoading(helper.itemView.getContext(), mAddImage);
                // 网络请求
                userAddModel.addFriend(item.getId());
                // 放入map方便修改状态
                mAddImgMap.put(curPosition, mAddImage);
            }
        });
    }

    private void showAddSuccess() {
        if (curPosition != -1 && curPosition < getData().size()) {
            User user = getData().get(curPosition);
            user.setFriend(true);
            notifyItemChanged(curPosition, 0);
            // 添加成功后从Map中移除
            mAddImgMap.remove(curPosition);
            UserHelper.getInstance().saveOrUpdate(user);
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
            User user = getData().get(curPosition);
            user.setFriend(false);
            notifyItemChanged(curPosition, 0);
            UserHelper.getInstance().saveOrUpdate(user);
        }
    }
}
