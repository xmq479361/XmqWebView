package com.xmq.web;

import android.content.Context;

import java.util.Map;

/**
 * html/webView触发命令
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:14
 */
public interface IWebCommand {
    /**
     * 命令关键字
     * @return
     */
    String name();
    /**
     * 是否主进程执行
     * @return
     */
    boolean runMainProcess();
    int type();

    /**
     * 执行
     * @param action
     * @param params
     */
    void execute(Context context, String action, Map params);
}
