package com.worldtreestd.finder.presenter.search;

import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.contract.search.SearchRecordContract;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description 搜索记录Presenter
 */
public class SearchRecordPresenter extends BasePresenter<SearchRecordContract.View>
    implements SearchRecordContract.Presenter {

    public SearchRecordPresenter(SearchRecordContract.View view) {
        super(view);
    }
}
