package com.legend.liteim.common.net;

import android.util.ArrayMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Legend
 * @data by on 19-1-13.
 * @description 表单形式的参数提交
 */
public class FormHelper {

    private ArrayMap<String, RequestBody> parameterMap = new ArrayMap<>();

    public FormHelper addParameter(String key, Object o) {
        RequestBody body = null;
        if (o instanceof String) {
            body = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), String.valueOf(o));
        }
        parameterMap.put(key, body);
        return this;
    }

    public FormHelper addParameter(String key, List<File> files) {
        for (int i = 0;i < files.size();i++) {
            RequestBody body =
                    RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), files.get(i));
            parameterMap.put(key+"\"; filename=\""+ files.get(i).getName(), body);
        }
        return this;
    }

    public Map<String, RequestBody> builder() {
        return parameterMap;
    }

    public void clear() {
        parameterMap.clear();
    }

}
