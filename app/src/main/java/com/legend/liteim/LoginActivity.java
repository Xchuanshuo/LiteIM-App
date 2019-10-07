package com.legend.liteim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.WindowManager;

import com.legend.im.client.IMClient;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.MyApplication;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.common.utils.MD5Util;
import com.legend.liteim.contract.userinfo.LoginContract;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.presenter.userinfo.LoginPresenter;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;

import static com.legend.liteim.common.utils.Constant.APP_ID;

/**
 * @author legend
 */

public class LoginActivity extends BaseActivity<LoginContract.Presenter>
    implements LoginContract.View {

    @BindView(R.id.btn_login)
    AppCompatButton mLoginBtn;
    private Tencent mTencent;
    private GlobalData globalData = GlobalData.getInstance();
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
    protected void initWindows() {
        // 全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置不可滑动
        setSwipeBackEnable(false);
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
        openId = globalData.getOpenId(o);
        String accessToken = globalData.getAccessToken(o);
        String expire = globalData.getTokenExpire(o);
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

    public static void show(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        DialogUtils.showToast(context, context.getString(R.string.login_request));
        context.startActivity(intent);
        // 如果客户端关闭 就尝试启动客户端
        if (IMClient.getInstance().isDestroyed()) {
            IMClient.getInstance().startClient();
        }
    }

    @Override
    public void loginAfter() {
        MainActivity.show(LoginActivity.this);
        finish();
    }

}
