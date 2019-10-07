package com.legend.liteim.presenter.search;

import android.text.TextUtils;

import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.contract.search.SearchResultContract;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.event.SearchEvent;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public abstract class BaseSearchPresenter<T extends SearchResultContract.View> extends BasePresenter<T>
    implements SearchResultContract.Presenter {

    public BaseSearchPresenter(T view) {
        super(view);
        registerSearchEvent();
    }

    private void registerSearchEvent() {
        addDisposable(RxBus.getDefault().toObservableSticky(SearchEvent.class)
                .subscribe(searchEvent -> {
                    if (!TextUtils.isEmpty(searchEvent.getKeyword())) {
                        mView.search(searchEvent.getKeyword());
                    }
                }));
    }
}
