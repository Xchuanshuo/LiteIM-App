package com.worldtreestd.finder.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * @author Legend
 * @data by on 18-5-26.
 * @description 获取屏幕信息工具类
 */
public class ScreenSizeUtils {

    private WindowManager manager;
    private DisplayMetrics dm;
    private static ScreenSizeUtils instance = null;
    private int screenWidth, screenHeight;
    private WeakReference<Context> contextWeakReference;

    public static ScreenSizeUtils getInstance(Context mContext) {
        if (instance == null) {
            synchronized (ScreenSizeUtils.class) {
                if (instance == null) {
                    instance = new ScreenSizeUtils(mContext);
                }
            }
        }
        return instance;
    }

    private ScreenSizeUtils(Context mContext) {
        contextWeakReference = new WeakReference<>(mContext);
        Context context = contextWeakReference.get();
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    //像素单位转换
    public static int dip2px(Context context, int dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }


    public static int calcStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
