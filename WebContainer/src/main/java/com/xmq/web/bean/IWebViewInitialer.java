package com.xmq.web.bean;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/7 22:06
 */
public interface IWebViewInitialer {
    void initialSetting(WebView webView, WebSettings settings);
}
