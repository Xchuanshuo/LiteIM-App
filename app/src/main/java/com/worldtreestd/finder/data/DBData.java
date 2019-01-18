package com.worldtreestd.finder.data;

import android.text.TextUtils;

import com.worldtreestd.finder.bean.User;

import org.litepal.LitePal;

/**
 * @author Legend
 * @data by on 19-1-18.
 * @description
 */
public class DBData {

    private SharedData sharedData = SharedData.getInstance();
    private static volatile DBData instance;

    public static DBData getInstance() {
        if (instance == null) {
            synchronized (DBData.class) {
                if (instance == null) {
                    instance = new DBData();
                }
            }
        }
        return instance;
    }

    public User getCurrentUser() {
        // 获取用户信息
        User user = null;
        String userId = sharedData.getCurrentUserId();
        if (!TextUtils.isEmpty(userId)) {
            user = LitePal.where("userid=?", userId)
                    .find(User.class).get(0);
        }
        return user;
    }
}
