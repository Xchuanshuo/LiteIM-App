package com.legend.liteim.ui.userinfo.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.legend.liteim.bean.User;
import com.legend.liteim.ui.userinfo.fragment.PersonalDynamicFragment;
import com.legend.liteim.ui.userinfo.fragment.UserFansFragment;
import com.legend.liteim.ui.userinfo.fragment.UserFollowFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-22.
 * @description
 */
public class UserRelationPageAdapter extends FragmentPagerAdapter {

    private final String[] titles = {"动态", "关注", "粉丝"};
    private final List<Fragment> fragmentList = new ArrayList<>();

    public UserRelationPageAdapter(FragmentManager fm, User user) {
        super(fm);
        fragmentList.add(PersonalDynamicFragment.newInstance(user));
        fragmentList.add(UserFollowFragment.newInstance(user));
        fragmentList.add(UserFansFragment.newInstance(user));
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
