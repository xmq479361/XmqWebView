package com.xmq.web.webprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.xmq.web.mainprocess.MainProcessCommandManager;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:12
 */
public class WebProcessCommandService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        WebLog.i("WebProcessCommandService onCreate()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        WebLog.i("WebProcessCommandService onBind()");
        return WebProcessCommandManager.getInstance();
    }
}
