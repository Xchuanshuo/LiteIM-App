package com.worldtreestd.finder.presenter.dynamic;

import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.contract.dynamic.DynamicDetailContract;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public class DynamicDetailPresenter extends BasePresenter<DynamicDetailContract.View>
    implements DynamicDetailContract.Presenter {

    public DynamicDetailPresenter(DynamicDetailContract.View view) {
        super(view);
    }
}
