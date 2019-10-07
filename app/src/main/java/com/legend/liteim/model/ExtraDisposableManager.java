package com.legend.liteim.model;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class ExtraDisposableManager {

    private CompositeDisposable mDisposables;

    private ExtraDisposableManager() {
        this.mDisposables = new CompositeDisposable();
    }

    public static ExtraDisposableManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static ExtraDisposableManager INSTANCE = new ExtraDisposableManager();
    }

    public void addDisposable(Disposable disposable) {
        if (mDisposables == null || mDisposables.isDisposed()) {
            mDisposables = new CompositeDisposable();
        }
        mDisposables.add(disposable);
    }

    public void destroyAll() {
        if (mDisposables != null) {
            mDisposables.clear();
        }
    }
}
