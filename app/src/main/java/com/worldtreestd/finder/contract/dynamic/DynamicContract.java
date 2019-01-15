package com.worldtreestd.finder.contract.dynamic;

import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description
 */
public interface DynamicContract {

    interface View extends BaseContract.View<Presenter> {

        /**
         *  显示数据
         * @param multiBeanList
         */
        void showData(List<CommonMultiBean<Dynamic>> multiBeanList);
    }

    interface Presenter extends BaseContract.Presenter {
        void getDynamicData(Integer page);
    }
}
