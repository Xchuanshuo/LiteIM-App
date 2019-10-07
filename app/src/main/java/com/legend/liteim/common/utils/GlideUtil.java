package com.legend.liteim.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.legend.liteim.R;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class GlideUtil {

    public static void loadGifImage(Context context, String url, ImageView imageView) {
        Glide.with(context).asGif().load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter())
                .into(imageView);
    }

    public static void loadImage(Context context, int id, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(id).apply(options).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.status_empty)
                .error(R.drawable.status_empty)
                .override(300, 240);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadMsgImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.status_empty)
                .error(R.drawable.status_empty);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        //获取原图的宽高
                        int width = resource.getWidth();
                        int height = resource.getHeight();

                        //获取imageView的宽
                        int imageViewWidth = imageView.getWidth();

                        //计算缩放比例
                        float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);

                        //计算图片等比例放大后的高
                        int imageViewHeight = (int) (height * sy);
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        params.width = (int) (width * sy);
                        params.height = imageViewHeight;

                        imageView.setLayoutParams(params);

//                        options.override(imageViewWidth, imageViewHeight);
                        Glide.with(context)
                                .load(resource)
                                .apply(options)
                                .into(imageView);
                    }
                });
    }

    public static void loadImage(Context context, File file, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(file).apply(options).into(imageView);
    }

    public static void loadImageByBlur(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .transform(new BlurTransformation(25, 3));
        Glide.with(context).load(url).apply(options).into(imageView);
    }
}
