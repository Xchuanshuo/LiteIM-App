package com.worldtreestd.finder.common.base.mvp;

import android.app.Application;

import org.litepal.LitePal;

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
        LitePal.initialize(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
