package com.legend.liteim.ui.search.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.legend.liteim.R;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.ui.search.adapter.SearchPageAdapter;

import butterknife.BindView;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchPageFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout  mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_page;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        SearchPageAdapter adapter = new SearchPageAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

}
