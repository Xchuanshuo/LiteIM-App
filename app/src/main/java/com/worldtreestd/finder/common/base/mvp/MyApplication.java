package com.worldtreestd.finder.common.base.mvp;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.guoxiaoxing.phoenix.picker.Phoenix;

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
        Phoenix.config()
                .imageLoader((context, imageView, imagePath, type) -> Glide.with(context)
                        .load(imagePath)
                        .into(imageView));
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
