package com.worldtreestd.finder;

import android.content.Intent;

import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;

/**
 * @author legend
 */

public class LoginActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        Intent intent = new Intent();
        intent.setClass(this, this.getClass());
        return R.layout.activity_login;
    }
}
