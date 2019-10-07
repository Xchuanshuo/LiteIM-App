package com.legend.liteim.contract.contacts;

import com.legend.liteim.bean.User;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @author Legend
 * @data by on 19-9-19.
 * @description
 */
public interface GroupCreateContract {

    interface View extends BaseContract.View<Presenter> {
        /**
         * 显示创建成功
         * @param msg
         */
        void showCreateSuccess(String msg);

        /**
         * 显示好友列表, 创建群组时可以进行拉取
         * @param users
         */
        void showFriendList(List<User> users);
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 创建群组
         * @param params
         */
        void createGroup(Map<String, RequestBody> params);

        /**
         * 拉取好友列表
         */
        void pullFriendList();
    }
}
