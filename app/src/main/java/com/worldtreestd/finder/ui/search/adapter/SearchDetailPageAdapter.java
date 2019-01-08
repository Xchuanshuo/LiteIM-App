package com.worldtreestd.finder.ui.search.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.ArrayMap;

import com.worldtreestd.finder.common.utils.EnumUtil;
import com.worldtreestd.finder.ui.search.fragment.SearchResultDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchDetailPageAdapter extends FragmentPagerAdapter {

    /**
     * 偏移量　Enum对应的code是从100开始的
     */
    public static final Integer OFFSET = 100;
    private List<Fragment> fragmentList;
    private ArrayMap<Integer, String> titleMap;

    public SearchDetailPageAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentList = new ArrayList<>();
        this.titleMap = new ArrayMap<>();
        for (EnumUtil childPage: EnumUtil.values()) {
            fragmentList.add(new SearchResultDetailFragment());
            titleMap.put(childPage.getCode(), childPage.getText());
        }
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
        return titleMap.get(position+OFFSET);
    }
}
