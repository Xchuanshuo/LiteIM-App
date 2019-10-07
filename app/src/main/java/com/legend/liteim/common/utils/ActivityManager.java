package com.legend.liteim.common.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

/**
 * @author Legend
 * @data by on 18-5-26.
 * @description Activity管理类
 */
public class ActivityManager {

    private static Stack<Activity> activities;
    private static volatile ActivityManager instance;

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     * @param activity
     */
    public synchronized void addActivity(Activity activity) {
        if (activities == null) {
            activities = new Stack<>();
        }
        activities.add(activity);
        Log.i("TAG","ActivityManager添加了："+activity.getClass().getName());
    }

    /**
     *  获取当前Activity
     * @return
     */
    public Activity currentActivity() {
        return activities.lastElement();
    }

    /**
     *  结束当前Activity
     */
    public void finishActivity() {
        finishActivity(currentActivity());
    }

    /**
     *  移除最后一个Activity
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
            Log.i("TAG", "ActivityManager移除了："+activity.getClass().getName());
        }
    }

    /**
     * 结束指定的Activity
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity == null) {
            activities.remove(activity);
            activity.finish();
            Log.i("TAG", "ActivityManager关闭了：" + activity.getClass().getName());
        }
    }

    /**
     *  结束指定类名的Activity
     * @param cla
     */
    public void finishActivity(Class<?> cla) {
        for (int i=0;i < activities.size();i++) {
            if (activities.get(i).getClass().equals(cla)) {
                finishActivity(activities.get(i));
                removeActivity(activities.get(i));
                break;
            }
        }
    }

    /**
     *  结束所有的Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
