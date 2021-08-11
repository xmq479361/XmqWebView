package com.xmq.web.webprocess;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.xmq.basic.framework.BaseApplication;
import com.xmq.web.IMainProcess2WebProcessInterface;
import com.xmq.web.IWebCommand;
import com.xmq.web.IWebProcess2MainProcessInterface;
import com.xmq.web.mainprocess.MainProcessCommandDispatcher;
import com.xmq.web.mainprocess.MainProcessCommandManager;
import com.xmq.web.mainprocess.MainProcessCommandService;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * web进程命令处理 -- 连接主进程Service，向主进程出通信
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:13
 */
public class WebProcessCommandManager extends IMainProcess2WebProcessInterface.Stub implements ServiceConnection {

    volatile static WebProcessCommandManager INSTANCE;

    public static WebProcessCommandManager getInstance() {
        if (INSTANCE == null){
            synchronized (WebProcessCommandManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WebProcessCommandManager();
                }
            }
        }
        return INSTANCE;
    }

    Gson mGson;
    private WebProcessCommandManager(){
        mGson = new Gson();
        WebLog.i("WebProcessCommandManager init: ");
    }
    public void initAidlBinding() {
        WebLog.i("WebProcessCommandManager initAidlBinding: ");
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(BaseApplication.getInstance(), MainProcessCommandService.class);
        BaseApplication.getInstance().bindService(intent, this, Context.BIND_AUTO_CREATE);
        WebLog.d("WebProcessCommandManager await: ");
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebLog.i("WebProcessCommandManager await: completed");
    }

    IWebProcess2MainProcessInterface mIWebProcess2MainProcessInterface;
    CountDownLatch mConnectBinderPoolCountDownLatch;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        WebLog.i("XmqWebView onServiceConnected: " + mConnectBinderPoolCountDownLatch.getCount());
        mIWebProcess2MainProcessInterface = IWebProcess2MainProcessInterface.Stub.asInterface(service);
        try {
            mIWebProcess2MainProcessInterface.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mConnectBinderPoolCountDownLatch.countDown();
    }

    DeathRecipient mBinderPoolDeathRecipient = new DeathRecipient() {
        @Override
        public void binderDied() {
            WebLog.i("XmqWebView binderDied: " + mConnectBinderPoolCountDownLatch.getCount());
            mIWebProcess2MainProcessInterface = null;
            initAidlBinding();
        }
    };
    @Override
    public void onServiceDisconnected(ComponentName name) {
        WebLog.i("XmqWebView onServiceDisconnected: " + mConnectBinderPoolCountDownLatch.getCount());
    }

//    @Override
//    public void handleMainProcessCommand(String action, String jsonParams) throws RemoteException {
//        WebProcessCommandDispatcher.getInstance().execute(BaseApplication.getInstance(), action, jsonParams);
//    }


    public void executeCommand(String commandName, String paramsJson) {
        WebLog.i("WebProcessCommandManager executeCommand: "+commandName+", "+paramsJson);
        if (mIWebProcess2MainProcessInterface != null) {
            try {
                mIWebProcess2MainProcessInterface.handleWebProcessCommand(commandName, paramsJson);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void response(String actionName, String response) throws RemoteException {
        WebProcessCommandDispatcher.getInstance().response(actionName, response);
    }

}
