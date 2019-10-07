package com.legend.liteim.ui.contacts.adapter;

import android.support.annotation.Nullable;
import android.util.ArrayMap;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Legend
 * @data by on 19-9-20.
 * @description 创建群组时被拉取的成员列表item的适配器
 */
public class GroupUserAddItemAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    private List<Long> selectedList = new ArrayList<>();
    private Map<Long, Boolean> map = new ArrayMap<>();

    public GroupUserAddItemAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_username, item.getUsername());
        GlideUtil.loadImage(helper.itemView.getContext(),
                item.getPortrait(), helper.getView(R.id.im_portrait));
        // 这里必须先置空，设置选中状态时会回调该监听器, 而RecyclerView滑动时
        // 进行了控件的重用, 所以监听器的状态可能会被其它item更改
        helper.setOnCheckedChangeListener(R.id.cb_select, null);
        helper.setChecked(R.id.cb_select, map.containsKey(item.getId()) ?
                map.get(item.getId()) : false);
        helper.setOnCheckedChangeListener(R.id.cb_select, (buttonView, isChecked) -> {
            map.put(item.getId(), isChecked);
            if (isChecked) {
                selectedList.add(item.getId());
                DialogUtils.showToast(helper.itemView.getContext(), item.getId() + " 已选中");
            } else {
                selectedList.remove(item.getId());
                DialogUtils.showToast(helper.itemView.getContext(), item.getId() + " 已取消");
            }
        });
    }

    /**
     * 获取被选中的id列表字符串
     * @return
     */
    public String getSelectedIdsStr() {
        if (selectedList.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0;i < selectedList.size();i++) {
            builder.append(selectedList.get(i));
            builder.append(',');
        }
        builder.append(']');
        return builder.toString();
    }
}
