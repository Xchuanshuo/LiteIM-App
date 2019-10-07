package com.legend.liteim.common.utils;

import android.util.Log;

/**
 * @author Legend
 * @data by on 19-1-9.
 * @description
 */
public class LogUtils {

    public static void logD(Object clazz, String value) {
        Log.d("TAG----->"+clazz.getClass().getName(), value);
    }
}
