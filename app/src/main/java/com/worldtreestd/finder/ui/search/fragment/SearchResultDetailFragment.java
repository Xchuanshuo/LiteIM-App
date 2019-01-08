package com.worldtreestd.finder.ui.search.fragment;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.SearchKeywordBean;
import com.worldtreestd.finder.contract.search.SearchResultDetailContract;
import com.worldtreestd.finder.presenter.search.SearchResultDetailPresenter;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description 搜索结果详情页Fragment
 */
public class SearchResultDetailFragment extends BaseFragment<SearchResultDetailContract.Presenter>
    implements SearchResultDetailContract.View{

    @Override
    protected SearchResultDetailContract.Presenter initPresenter() {
        return new SearchResultDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result_detail;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void searchResult(List<SearchKeywordBean> searchKeywordBeanList) {

    }

    @Override
    public void setPageType(String keyWord, int position) {
        mPresenter.requestSearch(keyWord, position);
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }
}
