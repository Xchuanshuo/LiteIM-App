package com.worldtreestd.finder.contract.dynamic;

import com.worldtreestd.finder.contract.base.BaseContract;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public interface DynamicDetailContract {

    interface View extends BaseContract.View<Presenter> {

        void showCollectedSuccess();

        void showUnCollectedSuccess();
    }

    interface Presenter extends BaseContract.Presenter {

        void collectDynamic(Integer dynamicId);

        void unCollectDynamic(Integer dynamicId);
    }
}
