package com.xmq.webdemo;

import android.app.Application;

import com.xmq.basic.framework.BaseApplication;
import com.xmq.web.mainprocess.MainProcessCommandDispatcher;
import com.xmq.web.mainprocess.MainProcessCommandManager;
import com.xmq.web.webprocess.WebProcessCommandManager;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/7 23:20
 */
public class MainApp extends BaseApplication {
//     static MainApp INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
//        INSTANCE = this;
    }
//    public static MainApp getInstance() {
//        return INSTANCE;
//    }


    @Override
    protected void initMainProcess() {
        super.initMainProcess();
        // 提前初始化web进程服务， 减少web进程Application初始化时间
        new Thread(()->{
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainProcessCommandManager.getInstance().initAidlBinding();
            MainProcessCommandDispatcher.getInstance();
        }).start();
//        WebProcessCommandManager.getInstance().initAidlBinding();
    }
}
