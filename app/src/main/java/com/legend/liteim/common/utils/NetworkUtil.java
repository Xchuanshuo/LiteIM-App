package com.legend.liteim.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Legend
 * @data by on 18-9-26.
 * @description
 */
public class NetworkUtil {


    /**
     *  检查网络状态
     * @param context
     * @return
     */
    public static boolean checkNetState(Context context) {
        boolean netState = false;
        ConnectivityManager connectivity = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0;i < info.length;i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        netState = true;
                        break;
                    }
                }
            }
        }
        return netState;
    }
}
