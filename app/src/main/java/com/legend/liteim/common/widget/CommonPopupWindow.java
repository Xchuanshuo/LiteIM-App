package com.legend.liteim.common.widget;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.legend.liteim.R;
import com.legend.liteim.common.utils.ScreenUtils;

/**
 * @author Legend
 * @data by on 19-1-24.
 * @description
 */
public class CommonPopupWindow {

    private View mPWView, mAnchorView;
    private AppCompatTextView mReportTv, mDeleteTv, mShareTv;
    private int[] windowPos;
    private PopupWindow mPopupWindow;
    private ReportListener reportListener;
    private ShareListener shareListener;
    private DeleteListener deleteListener;

    private CommonPopupWindow() { }

    public CommonPopupWindow buildPopupWindow(View anchorView, int xOf, int yOf) {
        this.mAnchorView = anchorView;
        this.mPWView = LayoutInflater.from(mAnchorView.getContext()).inflate(R.layout.dynamic_item_popupwindow, null);
        this.mPopupWindow= new PopupWindow(mPWView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setElevation(10.5f);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        this.windowPos = ScreenUtils.calculatePopWindowPos(mAnchorView, mPWView);
        this.windowPos[0] += xOf;
        this.windowPos[1] += yOf;
        mReportTv = mPWView.findViewById(R.id.tv_report);
        mDeleteTv = mPWView.findViewById(R.id.tv_delete);
        mShareTv = mPWView.findViewById(R.id.tv_share);
        mReportTv.setOnClickListener(view -> {
            if (reportListener != null) {
                reportListener.onReport(view);
                mPopupWindow.dismiss();
            }
        });
        mDeleteTv.setOnClickListener(view -> {
            if (mDeleteTv != null) {
                deleteListener.onDelete(view);
                mPopupWindow.dismiss();
            }
        });
        mShareTv.setOnClickListener(view -> {
            if (shareListener != null) {
                shareListener.onShare(view);
                mPopupWindow.dismiss();
            }
        });
        return this;
    }

    public CommonPopupWindow onlyHideShare() {
        mShareTv.setVisibility(View.GONE);
        mReportTv.setVisibility(View.VISIBLE);
        mDeleteTv.setVisibility(View.VISIBLE);
        return this;
    }

    public CommonPopupWindow onlyHideReport() {
        mReportTv.setVisibility(View.GONE);
        mShareTv.setVisibility(View.VISIBLE);
        mDeleteTv.setVisibility(View.VISIBLE);
        return this;
    }

    public CommonPopupWindow onlyHideDelete() {
        mDeleteTv.setVisibility(View.GONE);
        mShareTv.setVisibility(View.VISIBLE);
        mReportTv.setVisibility(View.VISIBLE);
        return this;
    }

    public CommonPopupWindow hideShare() {
        mShareTv.setVisibility(View.GONE);
        return this;
    }

    public CommonPopupWindow hideReport() {
        mReportTv.setVisibility(View.GONE);
        return this;
    }

    public CommonPopupWindow hideDelete() {
        mDeleteTv.setVisibility(View.GONE);
        return this;
    }

    public CommonPopupWindow buildPopupWindow(View anchorView) {
        return buildPopupWindow(anchorView, 0, 0);
    }

    public void offset(int xOf, int yOf) {
        windowPos[0] += xOf;
        windowPos[1] += yOf;
    }

    public void show() {
        mPopupWindow.showAtLocation(mPWView, Gravity.TOP|Gravity.START
                , windowPos[0], windowPos[1]);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public void setReportListener(ReportListener listener) {
        this.reportListener = listener;
    }

    public void setShareListener(ShareListener listener) {
        this.shareListener = listener;
    }

    public void setDeleteListener(DeleteListener listener) {
        this.deleteListener = listener;
    }

    public static CommonPopupWindow getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CommonPopupWindow INSTANCE = new CommonPopupWindow();
    }

    public interface ReportListener {
        void onReport(View view);
    }

    public interface ShareListener {
        void onShare(View view);
    }

    public interface DeleteListener {
        void onDelete(View view);
    }
}
