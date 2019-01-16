package com.worldtreestd.finder.common.utils;

import android.util.ArrayMap;

import com.worldtreestd.finder.common.net.FinderApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Legend
 * @data by on 18-8-31.
 * @description
 */
public class DataUtils {

    public static List<String> totalListUrl(List<String> urlList) {
        List<String> newUrlList = new ArrayList<>();
        for (String url: urlList) {
            newUrlList.add(FinderApiService.BASE_URL + url);
        }
        return newUrlList;
    }

    public static Map<String, String> totalMapUrl(Map<String, String> urlMap) {
        Map<String, String> newUrlMap = new ArrayMap<>();
        for (Map.Entry<String, String> entry: urlMap.entrySet()) {
            newUrlMap.put(entry.getKey(), FinderApiService.BASE_URL + entry.getValue());
        }
        return newUrlMap;
    }
}
