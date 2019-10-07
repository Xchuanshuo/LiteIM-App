package com.legend.liteim.ui.search.fragment.result;

import com.legend.liteim.R;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.contract.search.SearchResultContract;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description 搜索结果详情页Fragment
 */
public abstract class BaseSearchFragment<T> extends BaseFragment<SearchResultContract.Presenter>
        implements SearchResultContract.View<T>  {

    protected AtomicBoolean isLoaded = new AtomicBoolean(false);
    private String lastKeyword = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void search(String keyWord) {
        // 每个关键词的搜索结果只加载一次
        if (!lastKeyword.equals(keyWord) || !isLoaded.get()) {
            mPresenter.executeSearch(keyWord);
            lastKeyword = keyWord;
        }
    }

    @Override
    public void showSearchResult(List<T> data) {
        isLoaded.set(true);
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }
}
