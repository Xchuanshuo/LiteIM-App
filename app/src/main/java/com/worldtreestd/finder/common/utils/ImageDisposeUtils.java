package com.worldtreestd.finder.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import byc.imagewatcher.ImageWatcher;

/**
 * @author Legend
 * @data by on 18-7-20.
 * @description
 */
public class ImageDisposeUtils {

    public static void loadImage(String url, ImageView imageView) {

    }

    public static ImageWatcher getWatcher(Context mContext) {
        ImageWatcher mImageWatcher = ImageWatcher.Helper.with((Activity) mContext)
                .setHintMode(ImageWatcher.TEXT)
                .setLoader((context, url, lc) -> Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        lc.onResourceReady(resource);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        lc.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        lc.onLoadFailed(errorDrawable);
                    }
                }))
                .create();
        return mImageWatcher;
    }
}
