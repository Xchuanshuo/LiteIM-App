package com.legend.liteim.contract.userinfo;

import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-1-19.
 * @description
 */
public interface PersonalDynamicContract {

    interface View extends BaseContract.View<Presenter> {
        /**
         *  显示数据
         * @param multiBeanList
         */
        void showData(List<CommonMultiBean<Dynamic>> multiBeanList);

        /**
         * 显示删除成功
         * @Param content
         */
        void showDeleteSuccess(String msg);
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 个人发布的动态
         * @param userId
         * @param page
         */
        void personalDynamic(Long userId, Integer page);

        /**
         * 删除已经发布的动态
         * @param dynamicId
         */
        void deleteDynamic(Long dynamicId);
    }
}
