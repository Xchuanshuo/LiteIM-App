package com.worldtreestd.finder.common.base.mvp;

import android.app.Application;

/**
 * @author Legend
 * @data by on 2018/1/27.
 * @description 全局Application
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
