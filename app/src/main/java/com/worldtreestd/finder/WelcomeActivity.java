package com.worldtreestd.finder;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.data.SharedData;

import butterknife.BindView;

/**
 * @author legend
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.img_welcome)
    ImageView mImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initWindows() {
        // 全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initEventAndData() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        mImageView.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                deal();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void deal() {
        SharedData data = SharedData.getInstance();
        String jwt = data.getJWT();
        MainActivity.come(this, null);

//        if (TextUtils.isEmpty(jwt)) {
//            LoginActivity.come(this);
//        } else {
//            MainActivity.come(this, null);
//        }
        finish();
    }

}
