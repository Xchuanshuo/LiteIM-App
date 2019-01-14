package com.worldtreestd.finder.common.net;

import android.app.Dialog;
import android.content.Context;

import com.worldtreestd.finder.common.utils.DialogUtils;

import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * @author Legend
 * @data by on 18-5-27.
 * @description
 */
public class ProgressTransformer {

    public static <T> ObservableTransformer<T, T> applyProgressBar(
            @NonNull final Context context, String msg) {
        final Dialog dialog = DialogUtils.showLoadingDialog(context, msg);
        dialog.show();

        return upstream -> upstream.doOnSubscribe(disposable -> {

        }).doOnTerminate(() -> {
//            dialog.dismiss();
        }).doOnSubscribe(disposable -> {
//            dialog.dismiss();
        });
    }

    public static <T> ObservableTransformer<T, T> applyProgressBar(
            @NonNull final Context context) {

        return applyProgressBar(context, "正在加载中...");
    }
}
