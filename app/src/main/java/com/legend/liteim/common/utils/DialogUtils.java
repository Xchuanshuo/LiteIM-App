package com.legend.liteim.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legend.liteim.R;
import com.legend.liteim.common.base.mvp.MyApplication;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;


/**
 * @author Legend
 * @data by on 2017/12/13.
 * @description Toast工具类
 */

public final class DialogUtils {

    public static void showToast(String info) {
        showToast(MyApplication.getInstance(), info);
    }

    public static void showLongToast(String info) {
        showLongToast(MyApplication.getInstance(), info);
    }

    public static void showToast(Context context, String info) {
        if (context != null) {
            Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLongToast(Context context, String info) {
        if (context != null) {
            Toast.makeText(context, info, Toast.LENGTH_LONG).show();
        }
    }

    public static void showSnackbar(Activity activity, String msg) {
        Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView(),msg,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /**
     *  自定义ProgressDialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog showLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);
        LinearLayout layout = v.findViewById(R.id.dialog_view);
        // main.xml中的ImageView
        ImageView spaceshipImage = v.findViewById(R.id.img);
        // 提示文字
        TextView tipTextView = v.findViewById(R.id.tipTextView);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        // 设置加载信息
        tipTextView.setText(msg);
        // 创建自定义样式dialog
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        // 不可以用“返回键”取消
        loadingDialog.setCancelable(false);
        // 设置布局
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return loadingDialog;

    }

    public static void showLoading(Context context, ImageView targetImg) {
        int minSize = (int) Ui.dipToPx(context.getResources(), 22);
        int maxSize = (int) Ui.dipToPx(context.getResources(), 30);
        // 初始化一个圆形的动画的Drawable
        LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
        drawable.setBackgroundColor(0);

        int[] color = new int[]{UiCompat.getColor(context.getResources(), R.color.white_alpha_208)};
        drawable.setForegroundColor(color);
        // 设置进去
        targetImg.setImageDrawable(drawable);
        // 启动动画
        drawable.start();
    }
}
