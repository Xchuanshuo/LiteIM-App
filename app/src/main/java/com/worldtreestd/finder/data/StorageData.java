package com.worldtreestd.finder.data;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;

import io.reactivex.Observable;

/**
 * @author Legend
 * @data by on 19-1-16.
 * @description
 */
public class StorageData {

    private static volatile StorageData instance;

    public static StorageData getInstance() {
        if (instance == null) {
            synchronized(StorageData.class) {
                if (instance == null) {
                    instance = new StorageData();
                }
            }
        }
        return instance;
    }

    public void downloadFile(String url) {
        final Context context = MyApplication.getInstance();
        Observable.just(url)
                .map(s -> Glide.with(context).asFile().load(s).submit().get())
                .map(file -> {
                    String fileName = url.substring(url.lastIndexOf("/"));
                    String targetPath = Environment.getExternalStorageDirectory()
                            .getCanonicalPath()+"/finder/files/";
                    File f = new File(targetPath);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    targetPath = targetPath + fileName;
                    FileUtils.copyFile(file.getPath(), targetPath);
                    return "保存成功 "+targetPath;
                })
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserve<String>() {
                    @Override
                    public void onSuccess(String data) {
                        DialogUtils.showToast(context, data);
                    }
                });
    }
}
