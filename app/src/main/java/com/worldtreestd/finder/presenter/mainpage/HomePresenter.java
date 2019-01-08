package com.worldtreestd.finder.presenter.mainpage;

import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.contract.mainpage.HomeContract;

/**
 * @author Legend
 * @data by on 18-5-30.
 * @description
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements
        HomeContract.Presenter {

    public HomePresenter(HomeContract.View view) {
        super(view);
    }
}
