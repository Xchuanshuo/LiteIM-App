package com.legend.liteim.contract.message;

import com.legend.liteim.bean.Session;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-7.
 * @description
 */
public interface SessionContract {

    interface View extends BaseContract.View<Presenter> {

        /**
         * 显示新的session
         * @param session
         */
        void showNewSession(Session session);

        /**
         * 显示session列表
         * @param sessions
         */
        void showSessionList(List<Session> sessions);
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 拉取session列表
         */
        void pullSessionList();
    }
}
