package com.xmq.web.webprocess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xmq.web.IWebCommand;
import com.xmq.web.mainprocess.MainProcessCommandManager;

import java.util.Map;


/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:41
 */
public class XmqWebView extends WebView {
    public XmqWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public XmqWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XmqWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("JavascriptInterface")
    void init(){
        WebLog.i("XmqWebView init: ");
        addJavascriptInterface(this, "webview");
        // 连接主进程命令分发
        new Thread(()->WebProcessCommandManager.getInstance().initAidlBinding()).start();
//        post(()->MainProcessCommandManager.getInstance().initAidlBinding());
    }

    @Override
    protected void onAttachedToWindow() {
        WebProcessCommandDispatcher.getInstance().registerWebViewCallback((callbackName, response) -> {
            WebLog.d("XmqWebView evaluateJavascript: " + callbackName + " => " + response);
            evaluateJavascript("javascript:qjs.callback('"+callbackName+"','"+response+"')", null);
        });
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        WebProcessCommandDispatcher.getInstance().registerWebViewCallback(null);
        super.onDetachedFromWindow();
    }

    @JavascriptInterface
    public void jsCallNative(String key, String json) {
        WebLog.d("jsCallMative: "+key+", "+ json);
        WebProcessCommandDispatcher.getInstance().execute(getContext(), key, json);
    }
    Handler handler = new Handler(Looper.getMainLooper());
    @JavascriptInterface
    public void post(String key, String json) {
        WebLog.d("jsCallMative post: "+key+", "+ json);
        handler.post(() -> WebProcessCommandDispatcher.getInstance().execute(getContext(), key, json));
    }
//
//    @JavascriptInterface
//    public void jsCallNative(String key, String json, IJSHandler.JsCallback callback) {
//        WebLog.d("jsCallMative: "+json+" == ");
//        WebProcessCommandManager.getInstance().executeCommand(key, json);
//    }
}
