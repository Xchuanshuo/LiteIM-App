package com.worldtreestd.finder.presenter.search;

import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.contract.search.SearchResultDetailContract;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchResultDetailPresenter extends BasePresenter<SearchResultDetailContract.View>
    implements SearchResultDetailContract.Presenter {

    public SearchResultDetailPresenter(SearchResultDetailContract.View view) {
        super(view);
    }

    @Override
    public void requestSearch(String keyword, int type) {
        DialogUtils.showToast(MyApplication.getInstance(),"search: "+type);
        switch (type) {
            case 100:
                break;
            case 101:
                break;
            case 102:
                break;
            case 103:
                break;
            case 104:
                break;
            default: break;
        }
    }
}
