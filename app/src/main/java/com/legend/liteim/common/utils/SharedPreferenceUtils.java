package com.legend.liteim.common.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * @author Legend
 * @data by on 2018/1/3.
 * @description sharedPreference工具类
 */

public class SharedPreferenceUtils {

    public static final String COOKIE = "cookie";
    public static final String OPENID = "open_id";
    public static final String ACCESSTOKEN = "access_token";
    public static final String JWT = "account_jwt";
    public static final String EXPIRES = "expires";
    // jwt默认有效期限为7天
    public static final String JWT_EXPIRES = "jwt_expires";

    private SharedPreferences sharedPreferences;
    private static final String[] week = {"一", "二", "三", "四", "五", "六", "日"};
    public static final ArrayMap<String, Integer> weekMap = new ArrayMap<>();
    public static final ArrayMap<String, Integer> weekTHMap = new ArrayMap<>();

    static {
        for (int i=0;i<week.length;i++) {
            weekMap.put("星期"+week[i], i);
        }
        for (int i=1;i<20;i++) {
            weekTHMap.put("第"+i+"周", i-1);
        }
    }

    /**
     *  初始化
     * @param application
     * @param fileName
     */
    public SharedPreferenceUtils(Application application, String fileName) {
        sharedPreferences =
                application.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     *  初始化
     * @param context
     * @param fileName
     */
    public SharedPreferenceUtils(Context context, String fileName) {
        sharedPreferences =
                context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     *  保存键值对
     * @param key
     * @param value
     */
    public void save(String key, String value) {
        sharedPreferences.edit().putString(key,value).apply();
    }

    /**
     *  读取键值对
     * @param key
     * @return
     */
    public String get(String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     *  保存对象
     * @param key
     * @param object
     */
    public void save(String key, Object object) {
        String value = new Gson().toJson(object);
        save(key,value);
    }

    /**
     *  读取对象
     * @param key
     * @param cls
     * @return
     */
    public Object get(String key, Class cls) {
        String value = get(key);
        try {
            if (value != null) {
                Object object = new Gson().fromJson(value, cls);
                return object;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
