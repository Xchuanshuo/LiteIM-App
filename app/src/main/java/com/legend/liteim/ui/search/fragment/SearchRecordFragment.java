package com.legend.liteim.ui.search.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;
import com.legend.liteim.R;
import com.legend.liteim.bean.SearchKeyword;
import com.legend.liteim.common.adapter.FlowLayoutAdapter;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.db.SearchHelper;
import com.legend.liteim.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description 搜索记录Fragment 包括热搜和搜索历史
 */
public class SearchRecordFragment extends BaseFragment {

    private List<SearchKeyword> searchHistoryBeanList = new ArrayList<>();
    @BindView(R.id.flex_layout)
    FlexboxLayout mFlexboxLayout;
    @BindView(R.id.tv_clear_recent)
    AppCompatTextView mClearTv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_record;
    }

    @Override
    public void refreshData() {
        searchHistoryBeanList.clear();
        searchHistoryBeanList = SearchHelper.getInstance().getKeywordList();
        FlowLayoutAdapter<SearchKeyword> mFlowLayoutAdapter =
                new FlowLayoutAdapter<>(searchHistoryBeanList, mFlexboxLayout, getContext());
        mFlowLayoutAdapter.setItemClickListener((position, data) -> {
            SearchActivity mSearchActivity = (SearchActivity) _mActivity;
            mSearchActivity.jumpResultFragment(searchHistoryBeanList.get(position).getName());
        });
        mClearTv.setVisibility(searchHistoryBeanList.size() == 0 ?
                View.INVISIBLE : View.VISIBLE);
        mClearTv.setOnClickListener(v -> {
            SearchHelper.getInstance().clearAll();
            searchHistoryBeanList.clear();
            mFlowLayoutAdapter.autoLayout();
            mClearTv.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }
}
