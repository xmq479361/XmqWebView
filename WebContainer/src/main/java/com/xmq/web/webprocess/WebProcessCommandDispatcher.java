package com.xmq.web.webprocess;

import android.content.Context;

import com.google.gson.Gson;
import com.xmq.web.IWebCommand;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 22:52
 */
public class WebProcessCommandDispatcher {

    volatile static WebProcessCommandDispatcher INSTANCE;

    public static WebProcessCommandDispatcher getInstance() {
        if (INSTANCE == null) {
            synchronized (WebProcessCommandDispatcher.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WebProcessCommandDispatcher();
                }
            }
        }
        return INSTANCE;
    }

    ConcurrentHashMap<String, IWebCommand> commands = new ConcurrentHashMap<String, IWebCommand>();
    Gson mGson;

    private WebProcessCommandDispatcher() {
        mGson = new Gson();
        Iterator<IWebCommand> iterator = ServiceLoader.load(IWebCommand.class).iterator();
        while (iterator.hasNext()) {
            IWebCommand command = iterator.next();
            commands.put(command.name(), command);
        }
        WebLog.i("WebProcessCommandDispatcher init: ");
    }

    IWebViewCallback mWebViewCallback;

    void registerWebViewCallback(IWebViewCallback webViewCallback) {
        mWebViewCallback = webViewCallback;
    }

    public void execute(Context context, String action, String jsonParams) {
        IWebCommand webCommand = commands.get(action);
        WebLog.d("WebProcessCommandDispatcher execute: " + action + " => " + webCommand + ", " +
                mGson.toJson(commands.keySet()));
        WebLog.d("WebProcessCommandDispatcher jsonParams: " + jsonParams);
        if (null != webCommand) {
            if (webCommand.runMainProcess()) {
                // 传递主进程执行
                WebProcessCommandManager.getInstance().executeCommand(action, jsonParams);
            } else {
                Map params = mGson.fromJson(jsonParams, Map.class);
                webCommand.execute(context, action, params);
            }
        }
    }

    public void response(String callbackName, Object response) {
        WebLog.d("WebProcessCommandDispatcher response: " + callbackName + " => " + response);
        if (null != mWebViewCallback) {
            mWebViewCallback.response(callbackName, mGson.toJson(response));
        }
    }

}
