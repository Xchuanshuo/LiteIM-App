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

        /**
         * 收藏成功
         */
        void showCollectedSuccess();

        /**
         * 收藏失败
         */
        void showUnCollectedSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 得到全部动态数据
         * @param page
         */
        void getDynamicData(Integer page);

        /**
         * 收藏动态
         * @param dynamicId
         */
        void collectDynamic(Integer dynamicId);

        /**
         * 取消收藏动态
         * @param dynamicId
         */
        void unCollectDynamic(Integer dynamicId);
    }
}
