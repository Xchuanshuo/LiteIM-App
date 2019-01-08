package com.worldtreestd.finder.ui.mainpage.adapter;

import android.util.ArrayMap;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.bean.DetailBean;

import java.util.List;

import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_CENTER;
import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_HEAD;
import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_TAIL;

/**
 * @author Legend
 * @data by on 18-7-16.
 * @description 主页RecyclerView适配器
 *
 */
public class HomeAdapter extends BaseMultiItemQuickAdapter<CommonMultiBean<DetailBean>, BaseViewHolder> {

    public static final ArrayMap<Integer, String> mPartMap = new ArrayMap<>();
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HomeAdapter(List<CommonMultiBean<DetailBean>> data) {
        super(data);
        addItemType(HOME_ITEM_HEAD, R.layout.item_home_head);
        addItemType(HOME_ITEM_CENTER, R.layout.item_detail);
        addItemType(HOME_ITEM_TAIL, R.layout.item_home_tail);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommonMultiBean<DetailBean> item) {
        switch (helper.getItemViewType()) {
            case HOME_ITEM_HEAD:
                helper.setText(R.id.mTitle, item.getData().getName()+"");
                helper.addOnClickListener(R.id.mMore);
                break;
            case HOME_ITEM_CENTER:
                helper.setText(R.id.from_Nick, item.getData().getName()+"");
                helper.addOnClickListener(R.id.mCardView);
                break;
            case HOME_ITEM_TAIL:
                mPartMap.put(helper.getAdapterPosition(), item.getData().getName());
                helper.addOnClickListener(R.id.mChange);
                break;
            default: break;
        }
    }

}
