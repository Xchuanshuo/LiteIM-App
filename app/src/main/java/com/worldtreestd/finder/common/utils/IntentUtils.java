package com.worldtreestd.finder.common.utils;

import android.content.Context;
import android.content.Intent;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.bean.DynamicBean;
import com.worldtreestd.finder.ui.dynamic.DynamicDetailActivity;

import java.util.ArrayList;

/**
 * @author Legend
 * @data by on 18-7-20.
 * @description
 */
public class IntentUtils {

    public static Intent createDynamicIntent(Context context, DynamicBean dynamicBean) {
        Intent intent = new Intent();
        intent.putExtra(context.getString(R.string.portrait), dynamicBean.getPortraitUrl());
        intent.putExtra(context.getString(R.string.publisher_name), dynamicBean.getName());
        intent.putExtra(context.getString(R.string.publish_time), dynamicBean.getTime());
        intent.putExtra(context.getString(R.string.dynamic_content), dynamicBean.getContent());
        if (dynamicBean.getVideoUrl() == null) {
            intent.putStringArrayListExtra(context.getString(R.string.dynamic_picture_urlList), (ArrayList<String>) dynamicBean.getImageUrlList());
        } else {
            intent.putExtra(context.getString(R.string.dynamic_video_url), dynamicBean.getVideoUrl());
            intent.putExtra(context.getString(R.string.dynamic_video_image_url), dynamicBean.getVideoUrl());
        }
        intent.setClass(context, DynamicDetailActivity.class);
        return intent;
    }
}
