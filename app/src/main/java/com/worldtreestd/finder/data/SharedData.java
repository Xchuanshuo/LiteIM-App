package com.worldtreestd.finder.data;

import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.utils.Constant;
import com.worldtreestd.finder.common.utils.SharedPreferenceUtils;

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

    public void saveOpenId(String value) {
        shared.save(Constant.OPEN_ID, value);
    }

    public String opendId() {
        return shared.get(Constant.OPEN_ID);
    }

}
