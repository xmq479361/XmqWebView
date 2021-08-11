package com.xmq.web.mainprocess;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.xmq.basic.framework.BaseApplication;
import com.xmq.web.IMainProcess2WebProcessInterface;
import com.xmq.web.IWebCommand;
import com.xmq.web.IWebProcess2MainProcessInterface;
import com.xmq.web.webprocess.WebLog;
import com.xmq.web.webprocess.WebProcessCommandService;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 住进程命令通信处理 -- 连接Web进程Service，向Web出通信
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:11
 */
public class MainProcessCommandManager extends IWebProcess2MainProcessInterface.Stub implements ServiceConnection {
    volatile static MainProcessCommandManager INSTANCE;

    public static MainProcessCommandManager getInstance() {
        if (INSTANCE == null){
            synchronized (MainProcessCommandManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainProcessCommandManager();
                }
            }
        }
        return INSTANCE;
    }
    private MainProcessCommandManager(){
    }

    public void initAidlBinding() {
        WebLog.i("MainProcessCommandManager initAidlBinding: ");
        mBindCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(BaseApplication.getInstance(), WebProcessCommandService.class);
        BaseApplication.getInstance().bindService(intent, this, Context.BIND_AUTO_CREATE);
        WebLog.i("MainProcessCommandManager await: ");
        try {
            mBindCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebLog.i("MainProcessCommandManager await: completed");
    }

    private IMainProcess2WebProcessInterface mMainProcess2WebProcessInterface;
    private CountDownLatch mBindCountDownLatch;
    private IBinder.DeathRecipient mWebProcessDeatchRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            initAidlBinding();
        }
    };
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        WebLog.i("MainProcessCommandDispatcher onServiceConnected: ");
        mMainProcess2WebProcessInterface = IMainProcess2WebProcessInterface.Stub.asInterface(service);
        try {
            mMainProcess2WebProcessInterface.asBinder().linkToDeath(mWebProcessDeatchRecipient, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBindCountDownLatch.countDown();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        WebLog.i("MainProcessCommandDispatcher onServiceDisconnected: ");

    }

    @Override
    public void handleWebProcessCommand(String action, String jsonParams) throws RemoteException {
        MainProcessCommandDispatcher.getInstance().execute(action, jsonParams);
    }
    public void response(String action, String jsonParams) throws RemoteException {
        if (mMainProcess2WebProcessInterface != null) {
            mMainProcess2WebProcessInterface.response(action, jsonParams);
        }
    }
}
