package com.worldtreestd.finder.common.net;

import android.app.Activity;
import android.app.Dialog;

import com.worldtreestd.finder.common.utils.DialogUtils;

import java.lang.ref.WeakReference;

import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * @author Legend
 * @data by on 18-5-27.
 * @description
 */
public class ProgressTransformer {

    public static <T> ObservableTransformer<T, T> applyProgressBar(
            @NonNull final Activity activity, String msg) {
        final WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        final Dialog dialog = DialogUtils.showLoadingDialog(activityWeakReference.get(),msg);
        dialog.show();
        return upstream -> upstream.doOnSubscribe(disposable -> {

        }).doOnTerminate(() -> {
            Activity context;
            if ((context = activityWeakReference.get()) != null
                    && !context.isFinishing()) {
                dialog.dismiss();
            }
        }).doOnSubscribe(disposable -> {
            Activity context;
            if ((context = activityWeakReference.get()) != null
                    && !context.isFinishing()) {
                dialog.dismiss();
            }
        });
    }

    public static <T> ObservableTransformer<T, T> appProgressBar(
            @NonNull final Activity activity) {

        return applyProgressBar(activity, "正在加载中...");
    }
}
