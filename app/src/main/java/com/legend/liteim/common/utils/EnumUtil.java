package com.legend.liteim.common.utils;

/**
 * @author Legend
 * @data by on 18-7-17.
 * @description
 */
public enum EnumUtil {

    CONFESSION_WALL(100, "用户"),
    SCHOOL_NEWS(101, "群组"),
    COURSE_QUERY(102, "动态");

    int code;
    String text;

    EnumUtil(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
