package com.worldtreestd.finder.ui.search.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.SearchKeywordBean;
import com.worldtreestd.finder.common.utils.TestDataUtils;
import com.worldtreestd.finder.contract.search.SearchRecordContract;
import com.worldtreestd.finder.data.FlowLayoutData;
import com.worldtreestd.finder.presenter.search.SearchRecordPresenter;
import com.worldtreestd.finder.ui.search.SearchActivity;
import com.worldtreestd.finder.ui.search.adapter.SearchRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description 搜索记录Fragment 包括热搜和搜索历史
 */
public class SearchRecordFragment extends BaseFragment<SearchRecordContract.Presenter>
        implements SearchRecordContract.View {

    private List<SearchKeywordBean> searchHistoryBeanList = new ArrayList<>();
    private List<SearchKeywordBean> searchHotBeanList = new ArrayList<>();
    @BindView(R.id.flex_layout)
    FlexboxLayout mFlexboxLayout;
    @BindView(R.id.clear_all_history)
    AppCompatTextView mClearAllHistory;

    @Override
    protected SearchRecordContract.Presenter initPresenter() {
        return new SearchRecordPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_record;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchRecordAdapter(R.layout.item_search_history, searchHistoryBeanList);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mClearAllHistory.setOnClickListener(v->{
            searchHistoryBeanList.clear();
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void refreshData() {
        searchHotBeanList.clear();
        searchHistoryBeanList.clear();
        searchHotBeanList = TestDataUtils.getSearchData();
        searchHistoryBeanList = TestDataUtils.getSearchData();
        FlowLayoutData<SearchKeywordBean> mFlowLayoutData = new FlowLayoutData<>(searchHotBeanList, mFlexboxLayout, getContext());
        mFlowLayoutData.setItemClickListener((position, data) -> {
            SearchActivity mSearchActivity = (SearchActivity) _mActivity;
            mSearchActivity.jumpResultFragment(searchHotBeanList.get(position).getName());
        });
        mAdapter.setNewData(searchHistoryBeanList);
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }
}
