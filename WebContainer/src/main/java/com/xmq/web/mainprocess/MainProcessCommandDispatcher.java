package com.xmq.web.mainprocess;

import android.os.RemoteException;

import com.google.gson.Gson;
import com.xmq.basic.framework.BaseApplication;
import com.xmq.web.IWebCommand;
import com.xmq.web.webprocess.WebLog;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:05
 */
public class MainProcessCommandDispatcher {
    volatile static MainProcessCommandDispatcher INSTANCE;

    public static MainProcessCommandDispatcher getInstance() {
        if (INSTANCE == null) {
            synchronized (MainProcessCommandDispatcher.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainProcessCommandDispatcher();
                }
            }
        }
        return INSTANCE;
    }

    private ConcurrentHashMap<String, IWebCommand> commands = new ConcurrentHashMap<String, IWebCommand>();
    private Gson mGson;

    private MainProcessCommandDispatcher() {
        mGson = new Gson();
        Iterator<IWebCommand> iterator = ServiceLoader.load(IWebCommand.class).iterator();
        while (iterator.hasNext()) {
            IWebCommand command = iterator.next();
            commands.put(command.name(), command);
        }
        WebLog.d("MainProcessCommandDispatcher init: " + mGson.toJson(commands.keySet()));
    }

    public void execute(String action, String jsonParams) throws RemoteException {
        IWebCommand webCommand = commands.get(action);
        WebLog.d("MainProcessCommandDispatcher handelWebCommand: " + action + " => " + webCommand + ", " + mGson.toJson(commands.keySet()));
        WebLog.d("MainProcessCommandDispatcher jsonParams: " + jsonParams);
        if (null != webCommand) {
            Map params = mGson.fromJson(jsonParams, Map.class);
            webCommand.execute(BaseApplication.getInstance(), action, params);
        }
    }
}
