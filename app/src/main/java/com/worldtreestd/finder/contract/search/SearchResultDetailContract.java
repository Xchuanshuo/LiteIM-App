package com.worldtreestd.finder.contract.search;

import com.worldtreestd.finder.common.bean.SearchKeywordBean;
import com.worldtreestd.finder.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public interface SearchResultDetailContract {

    interface View extends BaseContract.View<Presenter> {
        void searchResult(List<SearchKeywordBean> searchKeywordBeanList);
        /**
         * 设置搜索页面类型
         * @param keyWord
         * @param type
         */
        void setPageType(String keyWord, int type);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         *  请求搜索
         * @param keyword
         * @param type
         */
        void requestSearch(String keyword, int type);

    }
}
