package com.worldtreestd.finder.contract.search;

import android.os.Bundle;

import com.worldtreestd.finder.contract.base.BaseContract;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public interface SearchResultContract {

    interface View extends BaseContract.View<Presenter> {
        /**
         *  进入搜索结果页时传入bundle
         * @param bundle
         */
        void hideAfterShow(Bundle bundle);

    }

    interface Presenter extends BaseContract.Presenter {

    }
}
