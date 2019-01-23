package com.worldtreestd.finder.common.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.ui.dynamic.DynamicDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_PICTURE;
import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_VIDEO;
import static com.worldtreestd.finder.common.utils.Constant.LOOK_DYNAMIC;

/**
 * @author Legend
 * @data by on 18-7-20.
 * @description
 */
public class IntentUtils {

    public static Intent createDynamicIntent(Context context, Dynamic dynamic) {
        Intent intent = new Intent();
        intent.putExtra(LOOK_DYNAMIC, dynamic);
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(dynamic.getUrls())) {
            switch (dynamic.getType()) {
                case DYNAMIC_ITEM_WORD_PICTURE:
                    List<String> urlList = gson.fromJson(dynamic.getUrls(), List.class);
                    intent.putStringArrayListExtra(context.getString(R.string.dynamic_picture_urlList)
                            , (ArrayList<String>) DataUtils.totalListUrl(urlList));
                    break;
                case DYNAMIC_ITEM_WORD_VIDEO:
                    Map<String, String> urlMap =
                            DataUtils.totalMapUrl(gson.fromJson(dynamic.getUrls(), Map.class));
                    intent.putExtra(context.getString(R.string.dynamic_video_url), urlMap.get("url"));
                    intent.putExtra(context.getString(R.string.dynamic_video_image_url),urlMap.get("coverPath"));
                    break;
                default: break;
            }
        }
        intent.setClass(context, DynamicDetailActivity.class);
        return intent;
    }
}
