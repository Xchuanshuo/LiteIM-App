package com.worldtreestd.finder.contract.dynamic;

import com.worldtreestd.finder.contract.base.BaseContract;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description
 */
public interface DynamicContract {

    interface View extends BaseContract.View<Presenter> {

    }

    interface Presenter extends BaseContract.Presenter {

    }
}
