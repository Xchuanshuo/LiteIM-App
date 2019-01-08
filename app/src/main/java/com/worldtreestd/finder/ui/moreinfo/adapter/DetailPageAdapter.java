package com.worldtreestd.finder.ui.moreinfo.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.worldtreestd.finder.common.bean.ItemBean;
import com.worldtreestd.finder.common.utils.Constant;
import com.worldtreestd.finder.ui.moreinfo.fragment.DetailListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-15.
 * @description 分区具体类别详情页
 */
public class DetailPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<ItemBean.Type> typeList;

    public DetailPageAdapter(FragmentManager fm, List<ItemBean.Type> typeList) {
        super(fm);
        this.typeList = typeList;
        fragmentList = new ArrayList<>();
        for (int i=0;i<typeList.size();i++) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.PARAM1, typeList.get(i).getId());
            bundle.putString(Constant.PARAM2, typeList.get(i).getName());
            DetailListFragment fragment = new DetailListFragment();
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
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
        return typeList.get(position).getName();
    }
}
