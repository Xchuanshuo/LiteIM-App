package com.legend.liteim.common.base.mvp.presenter;

import com.legend.liteim.bean.User;
import com.legend.liteim.contract.base.BaseContract;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.db.UserHelper;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Legend
 * @data by on 18-5-28.
 * @description
 */
public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    protected GlobalData globalData = GlobalData.getInstance();
    protected T mView;
    protected User curUser = UserHelper.getInstance().getCurrentUser();
    /**
     * 统一Disposable管理
     */
    private CompositeDisposable mDisposables;

    public BasePresenter(T view) {
        if (view != null) {
            WeakReference<T> reference = new WeakReference<>(view);
            this.mView = reference.get();
            this.mView.setPresenter(this);
        }
    }

    /**
     * 给子类获取View进行操作
     * @return
     */
    protected final T getView() {
        return mView;
    }

//    @Override
//    public void start() {
//        T view = getView();
//        if (view != null) {
//            view.showLoading();
//        }
//    }

    @Override
    public void detach() {
//        this.mView = null;
        unDisposable();
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (mDisposables == null || mDisposables.isDisposed()) {
            mDisposables = new CompositeDisposable();
        }
        mDisposables.add(disposable);
    }

    public User getCurUser() {
        return curUser;
    }

    @Override
    public void unDisposable() {
        if (mDisposables != null) {
            mDisposables.clear();
        }
    }
}
