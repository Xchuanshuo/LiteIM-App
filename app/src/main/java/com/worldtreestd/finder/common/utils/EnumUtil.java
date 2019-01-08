package com.worldtreestd.finder.common.utils;

/**
 * @author Legend
 * @data by on 18-7-17.
 * @description
 */
public enum EnumUtil {

    CONFESSION_WALL(100, "表白墙"),
    SCHOOL_NEWS(101, "校园资讯"),
    COURSE_QUERY(102, "课程查询"),
    LOST_FOUND(103, "失物招领"),
    ASSOCIATION(104, "社团"),
    DYNAMIC_INFO(105, "动态讯息");

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
