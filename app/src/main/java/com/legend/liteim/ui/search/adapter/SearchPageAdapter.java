package com.legend.liteim.ui.search.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.legend.liteim.ui.search.fragment.result.DynamicSearchFragment;
import com.legend.liteim.ui.search.fragment.result.GroupSearchFragment;
import com.legend.liteim.ui.search.fragment.result.UserSearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchPageAdapter extends FragmentPagerAdapter {

    /**
     * 偏移量　Enum对应的code是从100开始的
     */
    private List<Fragment> fragmentList;
    private final String[] titles = new String[]{"用户", "群组", "动态"};


    public SearchPageAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentList = new ArrayList<>();
        UserSearchFragment userSearchFragment = new UserSearchFragment();
        GroupSearchFragment groupSearchFragment = new GroupSearchFragment();
        DynamicSearchFragment dynamicSearchFragment = new DynamicSearchFragment();
        fragmentList.add(userSearchFragment);
        fragmentList.add(groupSearchFragment);
        fragmentList.add(dynamicSearchFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position % titles.length];
    }
}
