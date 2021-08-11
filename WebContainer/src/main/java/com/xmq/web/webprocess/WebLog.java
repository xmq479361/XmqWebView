package com.xmq.web.webprocess;

import android.util.Log;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:42
 */
public final class WebLog {
    final static String TAG = "Web";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
