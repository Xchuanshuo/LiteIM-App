package com.legend.liteim.contract.userinfo;

import com.legend.liteim.contract.base.BaseContract;

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

        /**
         * 显示是否是好友
         * @param isFriend
         */
        void showFriendState(boolean isFriend);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 当前关注状态
         * @param targetId
         */
        void followState(Long targetId);

        /**
         * 关注用户
         * @param targetId
         */
        void followUser(Long targetId);

        /**
         * 取消关注
         * @param targetId
         */
        void unFollowUser(Long targetId);

        /**
         * 查询当前用户与对方是否是好友
         * @param targetId
         */
        void friendState(Long targetId);

        /**
         * 添加好友
         * @param targetId
         */
        void addFriend(Long targetId);

        /**
         * 删除好友
         * @param targetId
         */
        void deleteFriend(Long targetId);
    }
}
