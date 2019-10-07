package com.legend.liteim.data;

import android.text.TextUtils;

import com.legend.im.client.IMClient;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.MyApplication;
import com.legend.liteim.common.utils.Constant;
import com.legend.liteim.common.utils.DateUtils;
import com.legend.liteim.common.utils.MD5Util;
import com.legend.liteim.common.utils.SharedPreferenceUtils;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.presenter.userinfo.LoginPresenter;
import com.tencent.connect.common.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

/**
 * @author Legend
 * @data by on 19-1-8.
 * @description 全局数据
 */
public class GlobalData {

    private SharedPreferenceUtils shared
            = new SharedPreferenceUtils(MyApplication.getInstance(), "shared");
    private static GlobalData instance;
//    public static final String HOST = "120.78.141.59";
    public static final int PORT = 8888;
    public static final String HOST = "192.168.43.21";

    private boolean isFirstIn = false;

    private GlobalData() {}

    public static GlobalData getInstance() {
        if (instance == null) {
            synchronized (GlobalData.class) {
                if (instance == null) {
                    instance = new GlobalData();
                }
            }
        }
        return instance;
    }

    public boolean isFirstIn() {
        return isFirstIn;
    }

    public void setFirstIn(boolean val) {
        isFirstIn = val;
    }

    public void saveJWT(String value) {
        shared.save(Constant.JWT, value);
        long expire = System.currentTimeMillis() +
                7*24*60*60*1000;
        shared.save(Constant.JWT_EXPIRE, expire);
    }

    public String getJWT() {
        // 上次登录时间
        String lastTimeStr = shared.get(Constant.LAST_LOGIN_TIME);
        long lastLoginTime = 0;
        if (!TextUtils.isEmpty(lastTimeStr)) {
            lastLoginTime = Long.parseLong(lastTimeStr);
        }
        // 登录时间超过4天 小于7天时 去请求更新jwt的值
        long diff = System.currentTimeMillis() - lastLoginTime;
        if (diff > DateUtils.timeToMs(4) && diff<DateUtils.timeToMs(7)) {
            User user = new User(getOpenId(), MD5Util.encryptAddSalt(getOpenId()));
            new LoginPresenter(null).login(null, user);
        }
        String jwtExpireStr = shared.get(Constant.JWT_EXPIRE);
        if (TextUtils.isEmpty(jwtExpireStr)) {
            return null;
        }
        // jwt过期时需要重新请求登录
        long expire = Long.parseLong(jwtExpireStr);
        if (System.currentTimeMillis() >= expire) {
            return null;
        }
        // 保存此次登录时间
        shared.save(Constant.LAST_LOGIN_TIME, System.currentTimeMillis());
        return shared.get(Constant.JWT);
    }

    public String getOpenId() {
        return shared.get(Constant.OPEN_ID);
    }

    public String getOpenId(JSONObject o) {
        String openId = getOpenId();
        if (openId == null) {
            try {
                openId = o.getString(Constants.PARAM_OPEN_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        shared.save(Constant.OPEN_ID, openId);
        return openId;
    }

    public String getAccessToken(JSONObject o) {
        String accessToken = shared.get(Constant.ACCESS_TOKEN);
        if (accessToken == null) {
            try {
                accessToken = o.getString(Constants.PARAM_ACCESS_TOKEN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        shared.save(Constant.ACCESS_TOKEN, accessToken);
        return accessToken;
    }

    public String getTokenExpire(JSONObject o) {
        String expire = shared.get(Constant.TOKEN_EXPIRE);
        if (expire == null) {
            try {
                expire = o.getString(Constants.PARAM_EXPIRES_IN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        shared.save(Constant.TOKEN_EXPIRE, expire);
        return expire;
    }

    public String getCurrentUserId() {
        return shared.get(Constant.LOGIN_USER_ID);
    }

    public void saveLoginUserId(String userId) {
        shared.save(Constant.LOGIN_USER_ID, userId);
    }

    public void logout() {
        LitePal.deleteDatabase("liteim");
        UserHelper.getInstance().onChanged();
        shared.clear();
        SharedPreferenceUtils tokenInfo = new SharedPreferenceUtils(MyApplication.getInstance(),
                "token_info_file");
        tokenInfo.clear();
        isFirstIn = true;
        IMClient.getInstance().destroy();
    }

}
