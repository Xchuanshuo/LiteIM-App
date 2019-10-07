package com.legend.liteim.common.base.mvp;

import android.app.Application;
import android.os.SystemClock;

import com.legend.im.client.IMClient;
import com.legend.im.common.IMConfig;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.event.ClientStateListenerImpl;
import com.legend.liteim.event.MsgListenerImpl;

import org.litepal.LitePal;

import java.io.File;

/**
 * @author Legend
 * @data by on 2018/1/27.
 * @description 全局Application
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LitePal.initialize(this);
        initClient();
    }

    public void initClient() {
        IMConfig config = IMConfig.builder()
                .host(GlobalData.HOST).port(GlobalData.PORT)
                .heartbeatInterval(IMConfig.DEFAULT_HEARTBEAT_INTERVAL)
                .idleTime(IMConfig.DEFAULT_IDLE_TIME)
                .maxRetry(IMConfig.DEFAULT_MAX_RETRY)
                .build();
        // 安装相关的配置
        IMClient.getInstance().setup(config);
        // 设置连接状态监听器
        IMClient.getInstance().setStateListener(new ClientStateListenerImpl());
        // 设置消息监听器
        IMClient.getInstance().setMsgListener(new MsgListenerImpl());
    }

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 当前APP的缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * 获取声音文件的本地地址
     *
     * @param isTmp 是否是缓存文件， True，每次返回的文件地址是一样的
     * @return 录音文件的地址
     */
    public static File getAudioTmpFile(boolean isTmp) {
        File dir = new File(getCacheDirFile(), "audio");
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

        File path = new File(getCacheDirFile(), isTmp
                ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }
}
