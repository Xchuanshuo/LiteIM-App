package com.worldtreestd.finder.common.utils;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * @author Legend
 * @data by on 18-5-27.
 * @description 统一异常处理
 */
public class ExceptionHandlerUtils {

    public static String handleException(Throwable e) {
        e.printStackTrace();
        String error;
        //网络超时
        if (e instanceof SocketTimeoutException) {
            Log.e("TAG", "网络连接异常: " + e.getMessage());
            error = "网络连接异常";
        //均视为网络错误
        } else if (e instanceof ConnectException) {
            Log.e("TAG", "网络连接异常: " + e.getMessage());

            error = "网络连接异常";
        //均视为解析错误
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            Log.e("TAG", "数据解析异常: " + e.getMessage());
            error = "数据解析异常";
        //服务器返回的错误信息
        } else if (e instanceof HttpException) {
            int response = ((HttpException) e).code();
            error = String.valueOf(response);
        } else if (e instanceof UnknownHostException) {
            Log.e("TAG", "网络连接异常: " + e.getMessage());
            error = "网络连接异常";
        } else if (e instanceof IllegalArgumentException) {
            Log.e("TAG", "下载文件已存在: " + e.getMessage());
            error = "下载文件已存在";
        } else {//未知错误
            try {
                Log.e("TAG", "错误: " + e.getMessage());
            } catch (Exception e1) {
                Log.e("TAG", "未知错误Debug调试 ");
            }
            error = "错误";
        }
        return error;
    }
}
