package com.worldtreestd.finder.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.worldtreestd.finder.R;


/**
 * @author Legend
 * @data by on 2017/12/13.
 * @description Toast工具类
 */

public final class DialogUtils {


    public static void showToast(Context context, String info) {
        Toast.makeText(context,info, Toast.LENGTH_SHORT).show();
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
}
