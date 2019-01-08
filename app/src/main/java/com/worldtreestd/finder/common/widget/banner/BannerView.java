package com.worldtreestd.finder.common.widget.banner;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.worldtreestd.finder.R;


/**
 * @author Legend
 * @data by on 2018/2/6.
 * @description 首页横幅View
 */

public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    /**
     *  圆点布局
     */
    private LinearLayout mPointContainer;

    private BannerViewBaseAdapter mAdapter;

    /**
     *  圆点数量
     */
    private int mPointCount;

    /**
     *  圆点图片
     */
    private ImageView[] mPoints;

    /**
     *  最后一个圆点
     */
    private int mLastPos;

    /**
     *  当前是否触摸
     */
    private boolean isTouch = false;
    private boolean isFirst = true;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isTouch) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1,true);
            }
            handler.postDelayed(this,3000);
        }
    };

    private static final Handler handler = new Handler();

    public BannerView(@NonNull Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.views_container);
        mPointContainer = findViewById(R.id.point_container);
        mViewPager.addOnPageChangeListener(this);
    }

    public void setAdapter(BannerViewBaseAdapter adapter) {
        this.mAdapter = adapter;
        mPointCount = mAdapter.getSize();
        mViewPager.setAdapter(mAdapter);
        Log.d("sddsccdsvdsvdv", String.valueOf(mPointCount*100));
        initPoint();
        mViewPager.setCurrentItem(mPointCount*100+3);
        /**
         *  防止第二次刷新后 显示空白页面
         */
        startScroll();
    }

    /**
     *  加载圆点
     */
    private void initPoint() {
       if (mPointCount == 0) {
           return;
       }

       mPoints = new ImageView[mPointCount];
       // 清除所有圆点
       mPointContainer.removeAllViews();
       for (int i=0;i < mPointCount;i++) {
           ImageView view = new ImageView(getContext());
           view.setImageResource(R.drawable.point_normal);
           mPointContainer.addView(view);
           mPoints[i] = view;
       }
       if (mPoints[0] != null) {
           mPoints[0].setImageResource(R.drawable.point_selected);
       }
       mLastPos = 0;
    }
    private void changePoint(int currentPoint) {
        if (mLastPos == currentPoint) {
            return;
        }
        mPoints[currentPoint].setImageResource(R.drawable.point_selected);
        mPoints[mLastPos].setImageResource(R.drawable.point_normal);
        mLastPos = currentPoint;
    }

    public void startScroll() {
        isTouch = false;
        isFirst = true;
        handler.removeCallbacks(runnable);
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
        handler.postDelayed(runnable,1000);
    }

    public void cancelScroll() {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPointCount != 0) {
            changePoint(position % mPointCount);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
