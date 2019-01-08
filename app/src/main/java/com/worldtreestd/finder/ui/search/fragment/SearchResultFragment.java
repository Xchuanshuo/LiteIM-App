package com.worldtreestd.finder.ui.search.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.contract.search.SearchResultContract;
import com.worldtreestd.finder.presenter.search.SearchResultPresenter;
import com.worldtreestd.finder.ui.search.adapter.SearchDetailPageAdapter;

import butterknife.BindView;

import static com.worldtreestd.finder.common.utils.Constant.KEY_WORD;
import static com.worldtreestd.finder.ui.search.adapter.SearchDetailPageAdapter.OFFSET;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchResultFragment extends BaseFragment<SearchResultContract.Presenter>
    implements SearchResultContract.View {

    @BindView(R.id.tab_layout)
    TabLayout  mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    private SearchDetailPageAdapter adapter;
    String keyWord="";

    @Override
    protected SearchResultContract.Presenter initPresenter() {
        return new SearchResultPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        adapter = new SearchDetailPageAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager, false);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SearchResultDetailFragment fragment = (SearchResultDetailFragment) adapter.getItem(position);
                fragment.setPageType(keyWord, position+OFFSET);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void refreshData() {
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    @Override
    public void hideAfterShow(Bundle bundle) {
        keyWord = (String) bundle.get(KEY_WORD);
        if (keyWord == null || keyWord.length()==0) {
            return;
        }
        DialogUtils.showToast(getContext(), "search: "+keyWord);
        mViewPager.setCurrentItem(0);
    }

}
