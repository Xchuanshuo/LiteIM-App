package com.worldtreestd.finder.ui.userinfo.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.ui.userinfo.fragment.PersonalDynamicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-22.
 * @description
 */
public class UserRelationPageAdapter extends FragmentPagerAdapter {

    private final String[] titles = {"dynamic", "follow", "fans"};
    private final List<Fragment> fragmentList = new ArrayList<>();

    public UserRelationPageAdapter(FragmentManager fm, User user) {
        super(fm);
        for (int i=0;i<3;i++) {
            fragmentList.add(PersonalDynamicFragment.newInstance(user));
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
        return titles[position%3];
    }
}
