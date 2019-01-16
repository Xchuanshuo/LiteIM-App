package com.worldtreestd.finder.common.widget.picturewatcher;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-1-16.
 * @description
 */
public class PreviewPagerAdapter extends FragmentPagerAdapter {

    private List<String> urlList;

    public PreviewPagerAdapter(FragmentManager fm, List<String> urList) {
        super(fm);
        this.urlList = urList;
    }

    @Override
    public Fragment getItem(int position) {
        return PreviewFragment.newInstance(urlList.get(position));
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}
