package com.worldtreestd.finder.ui.moreinfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.bean.ItemBean;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.data.FlowLayoutData;
import com.worldtreestd.finder.ui.moreinfo.MoreInfoDetailActivity;

import java.util.List;

import static com.worldtreestd.finder.common.utils.Constant.ITEM_TYPE;
import static com.worldtreestd.finder.common.utils.Constant.PARAM1;

/**
 * @author Legend
 * @data by on 18-8-22.
 * @description
 */
public class MoreInfoContentAdapter extends BaseQuickAdapter<ItemBean, BaseViewHolder> {

    private Context mContext;
    public MoreInfoContentAdapter(int layoutResId, @Nullable List<ItemBean> data, Context mContext) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemBean item) {
        helper.setText(R.id.tv_more_info_title, item.getName());
        List<ItemBean.Type> typeList = item.getTypes();
        FlexboxLayout flexboxLayout = helper.getView(R.id.flex_layout);
        FlowLayoutData<ItemBean.Type> mFLowLayoutData = new FlowLayoutData<>(typeList, flexboxLayout, mContext);
        mFLowLayoutData.setItemClickListener((position, data) -> {
            mContext.startActivity(new Intent(mContext, MoreInfoDetailActivity.class).putExtra(ITEM_TYPE, item)
                    .putExtra(PARAM1, position+""));
            DialogUtils.showToast(flexboxLayout.getContext(), typeList.get(position).getName());
        });
    }
}
