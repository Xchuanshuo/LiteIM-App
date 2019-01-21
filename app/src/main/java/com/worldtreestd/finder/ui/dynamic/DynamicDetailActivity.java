package com.worldtreestd.finder.ui.dynamic;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.ui.dynamic.fragment.DynamicDetailFragment;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description
 */
public class DynamicDetailActivity extends BaseActivity {

    private final DynamicDetailFragment mDynamicDetailFragment = new DynamicDetailFragment();
    @Override
    public boolean showHomeAsUp() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dynamic_detail;
    }

    @Override
    protected void initWindows() {
        super.initWindows();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadRootFragment(R.id.container, mDynamicDetailFragment);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarTitle("动态详情");
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
