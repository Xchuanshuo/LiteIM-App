package com.worldtreestd.finder.contract.moreinfo;

import com.worldtreestd.finder.common.bean.ItemBean;
import com.worldtreestd.finder.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-8-22.
 * @description
 */
public interface MoreInfoContract {

    interface View extends BaseContract.View<Presenter> {
        void showMoreInfoList(List<ItemBean> moreInfoList);
    }

    interface Presenter extends BaseContract.Presenter {
        void getMoreInfoList();
    }
}
