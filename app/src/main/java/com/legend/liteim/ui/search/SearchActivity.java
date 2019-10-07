package com.legend.liteim.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.legend.liteim.R;
import com.legend.liteim.bean.SearchKeyword;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.event.SearchEvent;
import com.legend.liteim.ui.search.fragment.SearchPageFragment;
import com.legend.liteim.ui.search.fragment.SearchRecordFragment;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchActivity extends BaseActivity {

    private SearchView mSearchView;
    private final SearchRecordFragment mSearchRecordFragment = new SearchRecordFragment();
    final SearchPageFragment mSearchPageFragment = new SearchPageFragment();
    private boolean isShow = false;

    @Override
    public boolean showHomeAsUp() {
        return true;
    }

    public static Intent getIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, SearchActivity.class);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem mSearchItem = menu.findItem(R.id.app_bar_search);
        mSearchView = (SearchView)mSearchItem.getActionView();
        mSearchView.onActionViewExpanded();
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("请输入搜索内容...");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                jumpResultFragment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadMultipleRootFragment(R.id.container, 0,
                mSearchRecordFragment, mSearchPageFragment);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
    }

    public void jumpResultFragment(String keyWord) {
        // 保存历史记录到本地数据库
        mSearchView.clearFocus();
        SearchKeyword keyword = new SearchKeyword(keyWord);
        keyword.setDate(new Date());
        keyword.saveOrUpdate("content = ?", keyWord);
        WeakReference<SearchPageFragment> resultFragment = new WeakReference<>(mSearchPageFragment);
        RxBus.getDefault().postSticky(new SearchEvent(keyWord));
        if (!isShow) {
            showHideFragment(resultFragment.get()) ;
            isShow = !isShow;
        }
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
