package com.xmq.web;

import android.content.Context;

import com.xmq.web.bean.WebRequestOptions;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/7 18:11
 */
public interface IWebServer {

    void openWebView(Context context, WebRequestOptions requestOptions);

}
