package com.worldtreestd.finder.contract.moreinfo;

import com.worldtreestd.finder.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-9-3.
 * @description
 */
public interface DetailActivityContract {

    interface View extends BaseContract.View<Presenter> {
        /**
         * 显示班级列表
         * @param gradeList
         */
        void showClassGradeList(List<String> gradeList);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         *  得到班级列表
         */
        void getClassGradeList();
    }

}
