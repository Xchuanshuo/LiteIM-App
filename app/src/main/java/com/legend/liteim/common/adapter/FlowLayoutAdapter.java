package com.legend.liteim.common.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;
import com.legend.liteim.R;
import com.legend.liteim.common.bean.IFlowDataBean;
import com.legend.liteim.common.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Legend
 * @data by on 18-7-17.
 * @description
 */
public class FlowLayoutAdapter<T extends IFlowDataBean> {

    private List<T> mData = new ArrayList<>();
    private FlexboxLayout mFlexBoxLayout;
    private Context mContext;
    private FlowClickListener mClickListener;

    public FlowLayoutAdapter(List<T> mData, FlexboxLayout flexBoxLayout, Context mContext) {
        this.mData = mData;
        this.mFlexBoxLayout = flexBoxLayout;
        this.mContext = mContext;
        autoLayout();
    }

    public void autoLayout() {
        mFlexBoxLayout.removeAllViews();
        for (int i=0;i<mData.size();i++) {
            AppCompatTextView mKeywordTv = (AppCompatTextView) LayoutInflater.from(mContext)
                    .inflate(R.layout.item_keyword, null);
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(ScreenUtils.dip2px(mContext,10), ScreenUtils.dip2px(mContext,10), 6, 0);
            Random random = new Random();
            Integer[] colors = new Integer[]{R.color.colorAccent,R.color.red,
                    R.color.colorPrimary,R.color.green};
            mKeywordTv.setTextColor(mContext.getResources().getColor(colors[random.nextInt(colors.length)]));
            Log.d("cddcdsvfvfv", mKeywordTv.getText().toString());
            mKeywordTv.setText(mData.get(i).getName());
            mKeywordTv.setLayoutParams(layoutParams);
            mFlexBoxLayout.addView(mKeywordTv);
            final int finalI = i;
            mKeywordTv.setOnClickListener(v -> {
                Log.d("keywords keywocsrds",mKeywordTv.getText().toString());
                if (mClickListener != null) {
                    mClickListener.onItemClickListener(finalI, mData.get(finalI));
                }
            });
        }
    }

    public void setItemClickListener(FlowClickListener flowClickListener) {
        this.mClickListener = flowClickListener;
    }

    public interface FlowClickListener<T> {
        /**
         * 设置子Item点击事件
         * @param position
         * @param data
         */
        void onItemClickListener(int position, T data);
    }
}
