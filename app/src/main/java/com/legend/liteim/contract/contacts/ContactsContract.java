package com.legend.liteim.contract.contacts;

import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.User;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-7.
 * @description
 */
public interface ContactsContract {

    interface View extends BaseContract.View<Presenter> {

        /**
         * 显示好友列表
         * @param userList
         */
        void showFriendList(List<User> userList);

        /**
         * 显示群组列表
         * @param chatGroupList
         */
        void showGroupList(List<ChatGroup> chatGroupList);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 拉取当前用户的好友列表
         */
        void friendList();

        /**
         * 拉取当前用户的群组列表
         */
        void groupList();
    }
}
