package com.legend.liteim;

import android.Manifest;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.legend.im.client.IMClient;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.data.GlobalData;
import com.tbruyelle.rxpermissions2.RxPermissions;

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
        // 设置不可滑动
        setSwipeBackEnable(false);
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
                requestPermission();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void requestPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO};
        rxPermissions.request(permissions)
                .subscribe(new BaseObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        if (data) {
                            IMClient.getInstance().startClient();
                            deal();
//                            emulator();
                        } else {
                            DialogUtils.showToast("请先授予相关权限!");
                        }
                    }
                });
    }

    private void deal() {
        GlobalData data = GlobalData.getInstance();
        String jwt = data.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(this);
        } else {
            MainActivity.show(this);
        }
        finish();
    }

    private void emulator() {
        GlobalData data = GlobalData.getInstance();
        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzIiwiZXhwIjoxNTcxMzYzNDYxfQ.epVyqZTo3buaukOq4s34OHtS_-ViLwQh9mDIPy5js5E";
        data.saveJWT(jwt);
        User user = new User();
        user.setId(3L);
        user.setUsername("测试人员2号");
        user.setOpenId("111111");
        user.setPortrait("https://uploadfile.bizhizu.cn/up/13/89/f3/1389f30dc821201347b10b3058c50f6d.jpg.source.jpg");
        user.setBackground("http://qzapp.qlogo.cn/qzapp/1106570475/1BB7E661E7042D1C41F0F033C83FFB45/100");
        user.setSignature("我是测试人员2号");
        user.saveOrUpdate();
        data.saveLoginUserId(String.valueOf(user.getId()));
        MainActivity.show(this);
//        if (TextUtils.isEmpty(jwt)) {
//            LoginActivity.show(this);
//        } else {
//        }
        finish();
    }
}
