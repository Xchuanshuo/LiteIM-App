package com.worldtreestd.finder.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.ui.search.fragment.SearchRecordFragment;
import com.worldtreestd.finder.ui.search.fragment.SearchResultFragment;

import java.lang.ref.WeakReference;

import static com.worldtreestd.finder.common.utils.Constant.KEY_WORD;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchActivity extends BaseActivity {

    private SearchView mSearchView;
    private final SearchRecordFragment mSearchRecordFragment = new SearchRecordFragment();
    final SearchResultFragment mSearchResultFragment = new SearchResultFragment();
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
                mSearchView.clearFocus();
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
                mSearchRecordFragment, mSearchResultFragment);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
    }

    public void jumpResultFragment(String keyWord) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_WORD, keyWord);
        WeakReference<SearchResultFragment> resultFragment = new WeakReference<>(mSearchResultFragment);
        resultFragment.get().hideAfterShow(bundle);
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
