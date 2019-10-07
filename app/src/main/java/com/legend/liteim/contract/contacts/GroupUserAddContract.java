package com.legend.liteim.contract.contacts;

import com.legend.liteim.bean.User;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public interface GroupUserAddContract {

    interface Presenter extends BaseContract.Presenter {

        void pullUnJoinedUserList();

        void submitGroupUser(String ids);
    }

    interface View extends BaseContract.View<Presenter> {

        void showUserList(List<User> users);

        void showSubmitSuccess();
    }
}
