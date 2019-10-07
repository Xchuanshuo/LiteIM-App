package com.legend.liteim.ui.userinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.legend.liteim.R;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.ui.userinfo.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    public static void show(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SettingsFragment.newInstance())
                .commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public boolean showHomeAsUp() {
        return true;
    }

    @Override
    protected boolean showDefaultTitle() {
        return true;
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
