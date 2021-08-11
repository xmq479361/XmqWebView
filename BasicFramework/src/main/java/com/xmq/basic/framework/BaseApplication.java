package com.xmq.basic.framework;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.DebugUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xmq.basic.framework.util.ProcessUtil;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:23
 */
public class BaseApplication extends Application {
    private static BaseApplication INSTANCE;

    public final static BaseApplication getInstance() {
        return INSTANCE;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        //判断当前进程是否为主进程，那么初始化主进程
        String processName = ProcessUtil.getCurrentProcessNameOnly(this);
        if (TextUtils.isEmpty(processName)) {
            initMainProcess();
        } else {
            initOtherProcess(processName);
        }
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                Log.e("ApplicationWeb", processName+" onActivityCreated: "+activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
    protected void initMainProcess() {
        Log.e("ApplicationWeb", "initMainProcess");

    }
    protected void initOtherProcess(String processName) {
        Log.e("ApplicationWeb", "initOtherProcess: "+processName);
    }
}
