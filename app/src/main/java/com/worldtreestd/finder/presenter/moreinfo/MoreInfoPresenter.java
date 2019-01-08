package com.worldtreestd.finder.presenter.moreinfo;

import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.utils.TestDataUtils;
import com.worldtreestd.finder.contract.moreinfo.MoreInfoContract;

/**
 * @author Legend
 * @data by on 18-8-22.
 * @description
 */
public class MoreInfoPresenter extends BasePresenter<MoreInfoContract.View>
    implements MoreInfoContract.Presenter {

    public MoreInfoPresenter(MoreInfoContract.View view) {
        super(view);
    }

    @Override
    public void getMoreInfoList() {
        mView.showMoreInfoList(TestDataUtils.getItemBeanList());
    }
}
