package com.worldtreestd.finder.data;

import android.text.TextUtils;

import com.tencent.connect.common.Constants;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.utils.Constant;
import com.worldtreestd.finder.common.utils.MD5Util;
import com.worldtreestd.finder.common.utils.SharedPreferenceUtils;
import com.worldtreestd.finder.common.utils.TimeUtils;
import com.worldtreestd.finder.presenter.userinfo.LoginPresenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Legend
 * @data by on 19-1-8.
 * @description SharedPreference数据
 */
public class SharedData {

    private SharedPreferenceUtils shared
            = new SharedPreferenceUtils(MyApplication.getInstance(), "shared");
    private static SharedData instance;

    private SharedData() {}

    public static SharedData getInstance() {
        if (instance == null) {
            synchronized (SharedData.class) {
                if (instance == null) {
                    instance = new SharedData();
                }
            }
        }
        return instance;
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
        if (diff>TimeUtils.timeToMs(4) && diff<TimeUtils.timeToMs(7)) {
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

}
