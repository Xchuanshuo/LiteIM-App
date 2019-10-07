package com.legend.liteim.contract.base;

import android.content.Context;

import com.legend.liteim.bean.User;

import io.reactivex.disposables.Disposable;

/**
 * @author Legend
 * @data by on 18-5-28.
 * @description
 */
public interface BaseContract {

    interface Model {

    }

    interface Presenter {
//        /**
//         *  初始化
//         */
//        void start();

        /**
         *  view销毁操作
         */
        void detach();

        /**
         * 得到登录的用户
         * @return 用户实体
         */
        User getCurUser();

        /**
         *  将网络请求的每一个Disposable加入集合
         * @param disposable
         */
        void addDisposable(Disposable disposable);

        /**
         *  销毁所有的Disposable
         */
        void unDisposable();
    }

    interface View<T extends BaseContract.Presenter> {

        /**
         *  刷新数据
         */
        default void refreshData() {

        }

        /**
         * 加载更多数据
         */
        default void loadMoreData() {

        }

        /**
         * 没有更多数据
         */
        default void showNoMoreData() {

        }

        /**
         * 刷新失败
         */
        default void refreshFailure() {

        }

        /**
         *  显示错误信息　
         * @param code　传入对应的响应码
         */
        default void showErrorMessage(int code) {

        }

        /**
         * 设置Presenter
         * @param presenter
         */
        void setPresenter(T presenter);

        /**
         * 得到上下文
         * @return
         */
        Context getContext();
    }
}
