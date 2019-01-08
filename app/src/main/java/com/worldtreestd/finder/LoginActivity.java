package com.worldtreestd.finder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;

import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;

import butterknife.BindView;

/**
 * @author legend
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_login)
    AppCompatButton mLoginBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {
        mLoginBtn.setOnClickListener(v -> MainActivity.come(this));
        finish();
    }

    public static void come(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
