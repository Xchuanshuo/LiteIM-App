package com.worldtreestd.finder.common.widget.banner;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 2018/2/6.
 * @description Banner适配器抽象基类
 */

public abstract class BannerViewBaseAdapter extends PagerAdapter {

    private List<View> mList;
    private View mView;

    public BannerViewBaseAdapter() {
        mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("I am position cx cx xcx", String.valueOf(position));
        if (getSize() != 0) {
            if (mList.size() <= (position % getSize())) {
                for (int i = mList.size();i <= position % getSize();++i) {
                    mList.add(getView(container,i));
                }
            }
            mView = mList.get(position % getSize());
            if (mView.getParent() != null) {
                container.removeView(mView);
            }
            container.addView(mView);
        }

        return mView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 滑动下一张图时销毁当前的图
        if (getSize() != 0 && position != 0) {
            container.removeView(mList.get(position % getSize()));
        }
    }

    /**
     *  获取要显示的View
     * @param container
     * @param position
     * @return
     */
    public abstract View getView(ViewGroup container, int position);

    /**
     *  获取实际ItemView的数量
     * @return
     */
    public abstract int getSize();
}
