package com.legend.liteim.data;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.legend.liteim.common.base.mvp.MyApplication;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.utils.MD5Util;
import com.legend.liteim.common.utils.StreamUtil;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;

/**
 * @author Legend
 * @data by on 19-1-16.
 * @description
 */
public class StorageData<Holder> {

    private DownloadCallback<Holder> mCallback;
    private String baseDir;
    private String ext;
    public static final String DEFAULT = "DEFAULT";

    public StorageData(String baseDir, String ext, DownloadCallback<Holder> callback) {
        this.baseDir = baseDir;
        this.ext = ext;
        this.mCallback = callback;
    }

    /**
     * 构建一个网络文件的本地路径
     * @param url
     */
    private File buildCacheFile(String url) {
        String key = MD5Util.encrypt(url);
        if (!TextUtils.isEmpty(ext) && DEFAULT.equals(ext)) {
            // 默认取URL文件对应的后缀
            this.ext = url.substring(url.lastIndexOf("."));
        }
        // 同一个URL对应同一个本地路径
        return new File(baseDir, key + "." + ext);
    }

    public void downloadFile(Holder holder, String path) {
        final Context context = MyApplication.getInstance();
        Observable.just(path)
                .map(s -> {
                    if (!TextUtils.isEmpty(s) && !s.startsWith("http")) {
                        // 说明是本地文件,直接返回即可
                        return new File(path);
                    }
                    // 使用Glide下载文件
                    return Glide.with(context).asFile().load(s).submit().get();
                })
                .map(file -> {
                    // 将文件复制到指定目录
                    File targetFile = buildCacheFile(path);
                    boolean isSuccess = StreamUtil.copy(file, targetFile);
                    if (isSuccess) return targetFile;
                    return null;
                })
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserver<File>() {
                    @Override
                    public void onSuccess(File data) {
                        if (data == null) {
                            mCallback.downloadFailure(holder);
                            return;
                        }
                        if (mCallback != null) {
                            mCallback.downloadSuccess(holder, data);
                        }
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        super.onFail(errorMsg);
                        if (mCallback != null) {
                            mCallback.downloadFailure(holder);
                        }
                    }
                });
    }

    public static String getExternalStoragePath() {
        try {
            return Environment.getExternalStorageDirectory().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCachePath() {
        try {
            return MyApplication.getCacheDirFile().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public void downloadFile(String path) {
//        final Context context = MyApplication.getInstance();
//        Observable.just(path)
//                .map(s -> {
//                    if (TextUtils.isEmpty(s) && !s.startsWith("http")) {
//                        // 说明是本地文件,直接返回即可
//                        return new File(path);
//                    }
//                    return Glide.with(context).asFile().load(s).submit().get();
//                })
//                .map(file -> {
//                    String fileName = path.substring(path.lastIndexOf("/"));
//                    if (!fileName.contains(".")) {
//                        fileName += ".jpg";
//                    }
//                    String targetPath = Environment.getExternalStorageDirectory()
//                            .getCanonicalPath()+"/Finder/files/";
//                    File f = new File(targetPath);
//                    if (!f.exists()) {
//                        f.mkdirs();
//                    }
//                    targetPath = targetPath + fileName;
//                    FileUtils.copyFile(file.getPath(), targetPath);
//                    return "保存成功 "+targetPath;
//                })
//                .compose(new NetworkService.ThreadTransformer<>())
//                .subscribe(new BaseObserver<String>() {
//                    @Override
//                    public void onSuccess(String data) {
//                        DialogUtils.showLongToast(context, data);
//                    }
//                });
//    }

    public void setCallback(DownloadCallback<Holder> mCallback) {
        this.mCallback = mCallback;
    }

    public interface DownloadCallback<Holder> {

        /**
         * 下载成功
         * @param file
         */
        void downloadSuccess(Holder holder, File file);

        /**
         * 下载失败
         * @param holder
         */
        void downloadFailure(Holder holder);
    }
}
