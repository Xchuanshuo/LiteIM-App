package com.worldtreestd.finder.contract.userinfo;

import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.contract.base.BaseContract;

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
         * @Param msg
         */
        void showDeleteSuccess(String msg);
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 个人发布的动态
         * @param userId
         * @param page
         */
        void personalDynamic(Integer userId, Integer page);

        /**
         * 删除已经发布的动态
         * @param dynamicId
         */
        void deleteDynamic(Integer dynamicId);
    }
}
