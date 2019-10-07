package com.legend.liteim.contract.contacts;

import com.legend.liteim.bean.User;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public interface GroupInfoContract {

    interface View extends BaseContract.View<Presenter> {

        /**
         * 显示群成员列表, 创建群组时可以进行拉取
         * @param users
         */
        void showGroupUserList(List<User> users);

        /**
         * 显示是否已经加入的状态
         * @param state
         */
        void showJoinState(boolean state);

        /**
         * 群组删除成功
         */
        void showDeleteSuccess();
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 请求加入群组
         */
        void joinGroup();

        /**
         * 退出群组
         */
        void exitGroup();

        /**
         * 删除群组，只有群主才有该权限
         */
        void deleteGroup();

        /**
         * 拉取群成员列表
         */
        void pullGroupUserList();
    }
}
