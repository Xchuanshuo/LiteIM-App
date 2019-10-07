package com.legend.liteim.contract.search;

import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public interface SearchResultContract {

    interface View<T> extends BaseContract.View<Presenter> {
        /**
         * 设置搜索页面类型
         * @param keyWord
         */
        void search(String keyWord);

        void showSearchResult(List<T> data);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         *  请求搜索
         * @param keyword
         */
        void executeSearch(String keyword);
    }

    interface UserSearchView extends View<User> {

    }

    interface GroupSearchView extends View<ChatGroup> {

    }

    interface DynamicSearchView extends View<CommonMultiBean<Dynamic>> {

    }

}
