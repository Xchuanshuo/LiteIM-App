package com.worldtreestd.finder.common.utils;

/**
 * @author Legend
 * @data by on 19-1-9.
 * @description
 */
public class TimeUtils {

    /**
     * 时间装换为毫秒
     * @return
     */
    public static long timeToMs(int n) {
        return n * 24 * 60 * 60 * 1000;
    }
}
