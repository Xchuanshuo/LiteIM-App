package com.legend.liteim.common.widget.multipicture;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.legend.liteim.R;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description 动态页多图控件
 */
public class MultiPictureLayout extends FrameLayout {

    /**
     *  最多可见数量
     */
    private int mVisibilityCount;
    /**
     *  是否显示全部图片
     */
    private boolean isShowAll;
    private static final int MAX_DISPLAY_COUNT = 9;
    private final FrameLayout.LayoutParams lpChildImage = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    private final int mSingleMaxSize;
    private final int mSpace;
    private int mLineCount;
    private final List<ImageView> pictureList = new ArrayList<>();
    private final List<ImageView> mVisiblePictureList = new ArrayList<>();
    /**
     *  剩余未显示的图片数量
     */
    private TextView mOverflowCount;
    private Context mContext;
    private Callback mCallback;
    private boolean isInit;
    private List<String> mDataList;
    private List<String> mThumbDataList;

    public MultiPictureLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiPictureLayout);
        mVisibilityCount = typedArray.getInteger(R.styleable.MultiPictureLayout_img_visibility_count,6);
        isShowAll = typedArray.getBoolean(R.styleable.MultiPictureLayout_img_show_all, false);
        mLineCount = typedArray.getInteger(R.styleable.MultiPictureLayout_img_line_count, 3);
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        mSingleMaxSize = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 216, mDisplayMetrics) + 0.5f);
        mSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mDisplayMetrics) + 0.5f);
        this.mContext = context;
        if (isShowAll) {
            mVisibilityCount = MAX_DISPLAY_COUNT;
        }
        for (int i = 0; i < mVisibilityCount; i++) {
            ImageView squareImageView = new SquareImageView(mContext);
            squareImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            squareImageView.setVisibility(View.GONE);
            int finalI = i;
            squareImageView.setOnClickListener(v -> mCallback.onImageClickListener(finalI, mDataList));
            addView(squareImageView);
            pictureList.add(squareImageView);
        }

        mOverflowCount = new TextView(mContext);
        mOverflowCount.setTextColor(0xFFFFFFFF);
        mOverflowCount.setTextSize(24);
        mOverflowCount.setGravity(Gravity.CENTER);
        mOverflowCount.setBackgroundColor(0x66000000);
        mOverflowCount.setVisibility(View.GONE);
        addView(mOverflowCount);

    }

    public void set(List<String> urlThumbList, List<String> urlList) {
        mThumbDataList = urlThumbList;
        mDataList = urlList;
        if (isInit) {
            notifyDataChanged();
        }
    }

    private void notifyDataChanged() {
        final List<String> dataList = mThumbDataList;
        final int urlListSize = dataList != null ? mThumbDataList.size() : 0;
//        if (mDataList == null || mDataList.size() != urlListSize) {
//            throw new IllegalArgumentException("dataList.size != thumbDataList.size");
//        }
        int column = mLineCount;
        if (urlListSize == 1) {
            column = 1;
        } else if (urlListSize == column+1) {
            column = 2;
        }
        int row = 0;
        if (urlListSize>mVisibilityCount) {
            int temp = mVisibilityCount/mLineCount;
            row = mVisibilityCount%mLineCount==0?temp:temp+1;
        } else {
            int temp= urlListSize/mLineCount;
            row = urlListSize%mLineCount==0?temp:temp+1;
        }
//        if (urlListSize > 6) {
//            row = 3;
//        } else if (urlListSize > 3) {
//            row = 2;
//        } else if (urlListSize > 0) {
//            row = 1;
//        }

        final int imageSize = urlListSize == 1 ? mSingleMaxSize :
                (int) ((getWidth() * 1f - mSpace * (column - 1)) / column);

        lpChildImage.width = imageSize;
        lpChildImage.height = lpChildImage.width;

        mOverflowCount.setVisibility(urlListSize > mVisibilityCount ? View.VISIBLE : View.GONE);
        LogUtils.logD(mContext, urlListSize - mVisibilityCount + "---"+urlListSize + ": "+mVisibilityCount);
        mOverflowCount.setText("+ " + (urlListSize - mVisibilityCount));
        mOverflowCount.setLayoutParams(lpChildImage);

        mVisiblePictureList.clear();
        for (int i = 0; i < pictureList.size(); i++) {
            final ImageView picture = pictureList.get(i);
            if (i < urlListSize) {
                picture.setVisibility(View.VISIBLE);
                mVisiblePictureList.add(picture);
                picture.setLayoutParams(lpChildImage);
                picture.setBackgroundResource(R.drawable.defacult_picture);
                String url = mDataList.get(i);
                if (!url.endsWith(".gif")) {
                    GlideUtil.loadImage(mContext, mDataList.get(i), picture);
                } else {
                    Glide.with(mContext).asGif().apply(new RequestOptions()
                            .priority(Priority.HIGH)
                            .fitCenter()).load(url).into(picture);
                }
                picture.setTranslationX((i % column) * (imageSize + mSpace));
                picture.setTranslationY((i / column) * (imageSize + mSpace));
            } else {
                picture.setVisibility(View.GONE);
            }
            if (i == mVisibilityCount - 1) {
                mOverflowCount.setTranslationX((i % column) * (imageSize + mSpace));
                mOverflowCount.setTranslationY((i / column) * (imageSize + mSpace));
            }
        }
        getLayoutParams().height = imageSize * row + mSpace * (row - 1);
    }

    public interface Callback {
        /**
         *  单个图片的点击事件
         * @param position
         * @param urlList
         */
        void onImageClickListener(int position, List<String> urlList);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        isInit = true;
        notifyDataChanged();
    }
}

