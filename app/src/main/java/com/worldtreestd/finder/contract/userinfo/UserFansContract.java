package com.worldtreestd.finder.contract.userinfo;

import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public interface UserFansContract {

    interface View extends BaseContract.View<Presenter> {
        /**
         * 显示关注人列表
         * @param userBeanList
         */
        void showFansList(List<User> userBeanList);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 用户粉丝列表
         * @param userId
         * @param page
         */
        void fansList(Integer userId, Integer page);
    }
}
