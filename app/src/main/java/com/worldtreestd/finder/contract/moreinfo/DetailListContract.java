package com.worldtreestd.finder.contract.moreinfo;

import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-15.
 * @description
 */
public interface DetailListContract {

    interface View extends BaseContract.View<DetailListContract.Presenter> {
        /**
         *  显示数据
         * @param detailBeanList
         */
        void showData(List<CommonMultiBean> detailBeanList);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         *  请求数据
         * @param type 分类
         * @param args 查询需要传递的参数
         */
        void requestData(int type, String...args);

        /**
         * 可以直接使用一个变长参数 不过为了区别这样写比较好
         * @param json 需要序列化为json字符串
         * @param args 周数、星期
         */
        void dealCourseData(String json, String...args);
    }
}
