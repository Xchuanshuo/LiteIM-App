package com.worldtreestd.finder.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class GlideUtil {

    public static void loadImage(Context context, int id, ImageView imageView) {
        Glide.with(context).load(id).apply(new RequestOptions().centerCrop())
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).apply(new RequestOptions().centerCrop())
                .into(imageView);
    }

    public static void loadImage(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file).apply(new RequestOptions().centerCrop())
                .into(imageView);
    }
}
