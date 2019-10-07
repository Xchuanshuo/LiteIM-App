package com.legend.liteim.contract.userinfo;

import com.legend.liteim.bean.User;
import com.legend.liteim.contract.base.BaseContract;

import org.json.JSONObject;

/**
 * @author Legend
 * @data by on 19-1-9.
 * @description
 */
public interface LoginContract {

    interface View extends BaseContract.View<Presenter> {
        /**
         * 登录后的操作
         */
        void loginAfter();
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 登录
         * @param o
         * @param user
         */
        void login(JSONObject o, User user);

        /**
         * 注册
         * @param o
         * @param openId
         */
        void register(JSONObject o, String openId);
    }
}
