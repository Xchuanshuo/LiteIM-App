package com.legend.liteim.common.widget.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.legend.liteim.common.widget.CircleImageView;
import com.legend.liteim.common.widget.DiNestedScrollView;

/**
 * @author Legend
 * @data by on 18-7-20.
 * @descriptiony 用户头像Behavior
 */
public class UserPortraitBehavior extends CoordinatorLayout.Behavior<CircleImageView>{

    private final String TAG_TOOLBAR = "toolbar";
    private float mStartPortraitX;
    private float mStartPortraitY;
    private int mPortraitMaxHeight;
    private int mToolbarHeight;
    private float mStartDependency;
    private float percent = 0;

    public UserPortraitBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof DiNestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
        // 初始化基础信息
        init(parent, child, dependency);
        // 计算比例
        if (child.getY() <= 0) return false;
        float percent = (child.getY()-mToolbarHeight) / (mStartPortraitY-mToolbarHeight);
        if (percent < 0) {
            percent = 0;
        }
        if (this.percent == percent || percent>1) return true;
        this.percent = percent;
        //　设置头像的大小
        ViewCompat.setScaleX(child, percent);
        ViewCompat.setScaleY(child, percent);

        return false;
    }

    private void init(CoordinatorLayout parent, CircleImageView child, View dependency) {
        if (mStartPortraitX == 0) {
            mStartPortraitX = child.getX();
        }
        if (mStartPortraitY == 0) {
            mStartPortraitY = child.getY();
        }
        if (mStartDependency == 0) {
            mStartDependency = dependency.getY();
        }
        if (mPortraitMaxHeight == 0) {
            mPortraitMaxHeight = child.getHeight();
        }
        if (mToolbarHeight == 0) {
            Toolbar toolbar = parent.findViewWithTag(TAG_TOOLBAR);
            mToolbarHeight = toolbar.getHeight();
        }
    }
}
