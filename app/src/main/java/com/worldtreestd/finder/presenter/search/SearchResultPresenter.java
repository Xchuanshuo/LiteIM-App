package com.worldtreestd.finder.presenter.search;

import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.contract.search.SearchResultContract;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchResultPresenter extends BasePresenter<SearchResultContract.View>
    implements SearchResultContract.Presenter {

    public SearchResultPresenter(SearchResultContract.View view) {
        super(view);
    }

}
