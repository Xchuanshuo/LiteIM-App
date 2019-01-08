package com.worldtreestd.finder.presenter.dynamic;

import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.contract.dynamic.DynamicContract;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description
 */
public class DynamicPresenter extends BasePresenter<DynamicContract.View>
    implements DynamicContract.Presenter {

    public DynamicPresenter(DynamicContract.View view) {
        super(view);
    }
}
