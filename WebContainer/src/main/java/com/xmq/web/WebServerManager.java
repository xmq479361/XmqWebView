package com.xmq.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xmq.web.bean.WebRequestOptions;
import com.xmq.web.webprocess.WebContainerActivity;
import com.xmq.web.webprocess.WebLog;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/7 18:20
 */
public class WebServerManager implements IWebServer{
    public final static String EXTRA_REQUEST_OPTIONS = "extra_req_options";
    private WebServerManager(){}

    public static WebServerManager get() {
        return new WebServerManager();
    }

    @Override
    public void openWebView(Context context, WebRequestOptions requestOptions) {
        WebLog.d("openWebView: "+requestOptions.url);
        Intent intent = new Intent(context, WebContainerActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_REQUEST_OPTIONS, requestOptions);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
