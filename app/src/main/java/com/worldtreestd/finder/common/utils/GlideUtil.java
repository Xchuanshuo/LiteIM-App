package com.worldtreestd.finder.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class GlideUtil {

    public static void loadImage(Context context, int id, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(id).apply(options)
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options)
                .into(imageView);
    }

    public static void loadImage(Context context, File file, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(file).apply(options)
                .into(imageView);
    }

    public static void loadImageByBlur(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.transform(new BlurTransformation());
        Glide.with(context).load(url).apply(options).into(imageView);
    }
}
