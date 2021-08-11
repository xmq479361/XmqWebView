package com.xmq.web.mainprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.xmq.web.webprocess.WebLog;
import com.xmq.web.webprocess.WebProcessCommandManager;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:07
 */
public class MainProcessCommandService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        WebLog.i("MainProcessCommandService onCreate()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        WebLog.i("MainProcessCommandService onBind()");
        return MainProcessCommandManager.getInstance();
    }
}
