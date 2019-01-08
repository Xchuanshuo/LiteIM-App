package com.worldtreestd.finder.common.widget.behavior;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.worldtreestd.finder.common.widget.DiNestedScrollView;

/**
 * @author Legend
 * @data by on 18-7-20.
 * @description 个人信息页AppBarLayout的Behavior
 */
public class AppBarLayoutBehavior extends MyBehavior {

    private static final String TAG = "overScroll";
    private static final String TAG_TOOLBAR = "toolbar";
    private static final String TAG_MIDDLE = "middle";
    private static final float TARGET_HEIGHT = 1500;
    private View mTargetView;
    private int mParentHeight;
    private int mTargetViewHeight;
    private float mTotalDy;
    private float mLastScale;
    private int mLastBottom;
    private boolean isAnimate;
    private Toolbar mToolbar;
    /**
     *  个人信息布局
     */
    private ViewGroup middleLayout;
    private int mMiddleHeight;
    /**
     *  是否正在回弹
     */
    private boolean isRecovering = false;
    /**
     *  达到这个下拉临界值就开始刷新动画
     */
    private final float MAX_REFRESH_LIMIT = 0.3f;
    /**
     *  最大滑动速度
     */
    private final float MAX_VELOCITY_Y = 100;
    private OnProgressChangeListener onProgressChangeListener;

    public AppBarLayoutBehavior() {

    }

    public AppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
        if (mToolbar == null) {
            mToolbar = parent.findViewWithTag(TAG_TOOLBAR);
        }
        if (middleLayout == null) {
            middleLayout = parent.findViewWithTag(TAG_MIDDLE);
        }
        // 需要调用super.onLayoutChild()方法之后获取
        if (mTargetView == null) {
            mTargetView = parent.findViewWithTag(TAG);
            if (mTargetView != null) {
                initial(abl);
            }
        }
        // AppBarLayout垂直方向产生偏移量时　改变Toolbar的Alpha
        abl.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> mToolbar.setAlpha(Float.valueOf(Math.abs(verticalOffset))/Float.valueOf(appBarLayout.getTotalScrollRange())));
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        isAnimate = true;
        if (target instanceof DiNestedScrollView) {
            return true;
        }
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (!isRecovering) {
            boolean isOffSet = (dy<0 && child.getBottom()>=mParentHeight)
                    || (dy>0 && child.getBottom()>mParentHeight);
            if (mTargetView!=null && isOffSet) {
                scale(child, target, dy);
                return;
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY) {
        // 大于滑动的最大速度就秒弹回
        if (velocityY > MAX_VELOCITY_Y) {
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
    }

    private void initial(AppBarLayout appBarLayout) {
        appBarLayout.setClipChildren(false);
        mParentHeight = appBarLayout.getHeight();
        mTargetViewHeight = mTargetView.getHeight();
        mMiddleHeight = middleLayout.getHeight();
    }

    private void scale(AppBarLayout abl, View target, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
        mLastScale = Math.max(1f, 1f+mTotalDy/TARGET_HEIGHT);
        ViewCompat.setScaleX(mTargetView, mLastScale);
        ViewCompat.setScaleY(mTargetView, mLastScale);
        mLastBottom = mParentHeight + (int)(mTargetViewHeight/2*(mLastScale-1));
        abl.setBottom(mLastBottom);
        target.setScrollY(0);

        middleLayout.setTop(mLastBottom-mMiddleHeight);
        middleLayout.setBottom(mLastBottom);
        if (onProgressChangeListener != null) {
            float progress = Math.min((mLastScale-1)/MAX_REFRESH_LIMIT, 1);
            onProgressChangeListener.onProgressChange(progress, false);
        }

    }

    public interface OnProgressChangeListener {
        /**
         *  范围0-1
         * @param progress
         * @param isRelease 是否是释放状态
         */
        void onProgressChange(float progress, boolean isRelease);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    private void recovery(final AppBarLayout abl) {
        if (isRecovering) {
            return;
        }
        if (mTotalDy > 0) {
            isRecovering = true;
            mTotalDy = 0;
            if (isAnimate) {
                ValueAnimator animator = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
                animator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    ViewCompat.setScaleX(mTargetView, value);
                    ViewCompat.setScaleY(mTargetView, value);
                    abl.setBottom((int)(mLastBottom-(mLastBottom-mParentHeight)*animation.getAnimatedFraction()));
                    middleLayout.setTop((int) (mLastBottom -
                            (mLastBottom - mParentHeight) * animation.getAnimatedFraction() - mMiddleHeight));
                    if (onProgressChangeListener != null) {
                        float progress = Math.min((value-1)/MAX_REFRESH_LIMIT, 1);
                        onProgressChangeListener.onProgressChange(progress, true);
                    }
                });
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRecovering = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            } else {
                ViewCompat.setScaleX(mTargetView, 1f);
                ViewCompat.setScaleY(mTargetView, 1f);
                abl.setBottom(mParentHeight);
                middleLayout.setTop(mParentHeight-mMiddleHeight);
                isRecovering = false;
                if (onProgressChangeListener != null) {
                    onProgressChangeListener.onProgressChange(0, true);
                }
            }
        }
    }
}
