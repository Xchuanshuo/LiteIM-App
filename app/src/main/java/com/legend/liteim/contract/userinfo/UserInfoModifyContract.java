package com.legend.liteim.contract.userinfo;

import com.legend.liteim.bean.User;
import com.legend.liteim.contract.base.BaseContract;

/**
 * @author Legend
 * @data by on 19-10-4.
 * @description 用户信息修改契约
 */
public interface UserInfoModifyContract {

    interface View extends BaseContract.View<Presenter> {

        void updateSuccess();
    }

    interface Presenter extends BaseContract.Presenter {

        void updateUserInfo(User user);
    }
}
