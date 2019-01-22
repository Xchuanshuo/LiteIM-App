package com.worldtreestd.finder.contract.userinfo;

import com.worldtreestd.finder.contract.base.BaseContract;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public interface UserInfoContract {

    interface View extends BaseContract.View<Presenter> {

        /**
         * 显示关注状态
         * @param isFollow
         */
        void showFollowState(boolean isFollow);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 当前关注状态
         * @param targetId
         */
        void followState(Integer targetId);

        /**
         * 关注用户
         * @param targetId
         */
        void followUser(Integer targetId);

        /**
         * 取消关注
         * @param targetId
         */
        void unFollowUser(Integer targetId);
    }
}
