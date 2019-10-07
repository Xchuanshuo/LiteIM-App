package com.legend.liteim.contract.userinfo;

import com.legend.liteim.bean.User;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public interface UserFollowContract {

    interface View extends BaseContract.View<Presenter> {
        /**
         * 显示关注人列表
         * @param userBeanList
         */
        void showFollowerList(List<User> userBeanList);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 关注人列表
         * @param userId
         * @param page
         */
        void followerList(Long userId, Integer page);
    }
}
