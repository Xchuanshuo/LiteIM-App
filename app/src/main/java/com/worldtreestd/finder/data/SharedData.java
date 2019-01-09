package com.worldtreestd.finder.data;

import com.tencent.connect.common.Constants;
import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.utils.Constant;
import com.worldtreestd.finder.common.utils.SharedPreferenceUtils;

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
    }

    public String getJWT() {
        return shared.get(Constant.JWT);
    }

    public String getOpenId(JSONObject o) {
        String openId = shared.get(Constant.OPEN_ID);
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
