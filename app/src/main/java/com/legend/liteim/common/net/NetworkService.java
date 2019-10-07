package com.legend.liteim.common.net;

import android.util.Log;

import com.legend.liteim.common.base.mvp.MyApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author Legend
 * @data by on 2018/3/8.
 * @description
 */

public class NetworkService  {

    private volatile static NetworkService instance;
    private final FinderApiService mService;

    private NetworkService() {
        // 指定缓存路径和大小
        final Cache cache = new Cache(new File(MyApplication.getInstance().getCacheDir(), "HttpCache"),
                1024*1024*1024);

        final OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(chain -> {
                    // 拿到请求
                    Request original = chain.request();
                    // 重新进行build
                    Request.Builder builder = original.newBuilder();
                    builder.addHeader("Content-Type","application/json");
                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                })
                .addInterceptor(getLoggerInterceptor())
                .writeTimeout(30*1000, TimeUnit.MILLISECONDS)
                .readTimeout(20*1000, TimeUnit.MILLISECONDS)
                .connectTimeout(6*1000, TimeUnit.MILLISECONDS)
                // 请求失败重试
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(FinderApiService.BASE_URL)
                .build();
        mService = retrofit.create(FinderApiService.class);
    }

    public static FinderApiService getInstance() {

        if (instance == null) {
            synchronized(FinderApiService.class) {
                if (instance == null) {
                    instance = new NetworkService();
                }
            }
        }

        return instance.mService;
    }

    public static final class ThreadTransformer<T> implements ObservableTransformer<T,T> {

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
    }

    public final static HttpLoggingInterceptor getLoggerInterceptor() {
        // 日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.HEADERS;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            Log.d("ApiUrl","----->"+message);
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
