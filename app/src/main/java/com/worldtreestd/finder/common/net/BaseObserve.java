package com.worldtreestd.finder.common.net;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * @author Legend
 * @data by on 18-5-27.
 * @description Observe封装
 */
public abstract class BaseObserve<T> extends ResourceObserver<T> {

    @Override
    public void onNext(T t) {
        onSuccess(t);
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("Retrofit Error", e.getMessage());
        e.printStackTrace();
        boolean isHttpError = false;
        String error="";
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
            isHttpError = true;
            int code = ((HttpException) e).code();
            error = "Http服务器错误";
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
        onFail(error);
//        if (!isHttpError) {
//            DialogUtils.showToast(MyApplication.getInstance(), error);
//        }
        onFinish();
    }

    @Override
    public void onComplete() {

    }

    /**
     * 服务器响应成功返回的数据
     * @param data
     */
    abstract public void onSuccess(T data);

    /**
     *  服务器响应失败返回的响应码
     * @param errorMsg
     */
    public void onFail(String errorMsg) {

    }

    public void onFinish() { }

}
