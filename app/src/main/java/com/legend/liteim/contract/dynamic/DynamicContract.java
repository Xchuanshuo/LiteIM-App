package com.legend.liteim.contract.dynamic;

import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.contract.base.BaseContract;

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
        /**
         * 得到全部动态数据
         * @param page
         */
        void getDynamicData(Integer page);
    }
}
