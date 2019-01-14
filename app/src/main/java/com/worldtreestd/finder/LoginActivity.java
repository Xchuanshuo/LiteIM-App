package com.worldtreestd.finder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.utils.LogUtils;
import com.worldtreestd.finder.common.utils.MD5Util;
import com.worldtreestd.finder.contract.userinfo.LoginContract;
import com.worldtreestd.finder.data.SharedData;
import com.worldtreestd.finder.presenter.userinfo.LoginPresenter;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;

import static com.worldtreestd.finder.common.utils.Constant.APP_ID;

/**
 * @author legend
 */

public class LoginActivity extends BaseActivity<LoginContract.Presenter>
    implements LoginContract.View {

    @BindView(R.id.btn_login)
    AppCompatButton mLoginBtn;
    private Tencent mTencent;
    private SharedData sharedData = SharedData.getInstance();
    private String openId = "";
    private AtomicBoolean isLogin = new AtomicBoolean(false);

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initEventAndData() {
        mTencent = Tencent.createInstance(APP_ID, MyApplication.getInstance());
        mLoginBtn.setOnClickListener(v -> {
            if (!mTencent.isSessionValid()) {
                mTencent.login(LoginActivity.this, "all", loginListener);
            } else {
                mTencent.login(LoginActivity.this, "all", loginListener, true);
            }
        });
    }

    /** 登录时的回调 **/
    private void tokenDeal(JSONObject o) {
        openId = sharedData.getOpenId(o);
        String accessToken = sharedData.getAccessToken(o);
        String expire = sharedData.getTokenExpire(o);
        if (!TextUtils.isEmpty(openId) && !TextUtils.isEmpty(accessToken)
                && !TextUtils.isEmpty(expire)) {
            mTencent.setAccessToken(accessToken, expire);
            mTencent.setOpenId(openId);
            UserInfo userInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
            isLogin.set(true);
            userInfo.getUserInfo(loginListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.logD(this, requestCode+"--------"+resultCode);
        if (requestCode == Constants.REQUEST_LOGIN
                || resultCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }

    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (!isLogin.get()) {
                DialogUtils.showToast(LoginActivity.this, o.toString());
                tokenDeal((JSONObject) o);
            } else {
                mPresenter.login((JSONObject) o,
                        new User(openId, MD5Util.encryptAddSalt(openId)));
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (TextUtils.isEmpty(openId)) {
                mPresenter.login(null,
                        new User(openId, MD5Util.encryptAddSalt(openId)));
            }
        }

        @Override
        public void onCancel() {

        }
    };

    public static void come(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void loginAfter() {
        MainActivity.come(LoginActivity.this, null);
        finish();
    }
}
