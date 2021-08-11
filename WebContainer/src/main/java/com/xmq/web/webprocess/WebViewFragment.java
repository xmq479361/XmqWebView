package com.xmq.web.webprocess;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xmq.basic.framework.util.ProcessUtil;
import com.xmq.web.R;
import com.xmq.web.WebServerManager;
import com.xmq.web.bean.IWebViewInitialer;
import com.xmq.web.bean.WebRequestOptions;
import com.xmq.web.webprocess.chrome.XmqWebChromeClient;
import com.xmq.web.webprocess.client.XmqWebClient;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/7 18:26
 */
public class WebViewFragment extends Fragment {
    public static WebViewFragment get(Bundle extras) {
        WebViewFragment webFragment = new WebViewFragment();
        webFragment.setArguments(extras);
        return webFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebRequestOptions requestOptions = getArguments().getParcelable(WebServerManager.EXTRA_REQUEST_OPTIONS);

        mWebView = view.findViewById(R.id.web_webview);
        initialWebView(requestOptions);
        WebLog.d("WebViewFragment loadUrl: "+requestOptions.url);
        mWebView.loadUrl(requestOptions.url);
        TextView textView = view.findViewById(R.id.web_tv_url);
        textView.setText("process"+ProcessUtil.getCurrentProcessNameOnly(getContext()));
        WebLog.d("WebViewFragment setText: "+textView.getText());
    }

    @SuppressLint("JavascriptInterface")
    private void initialWebView(WebRequestOptions options) {
        createWebView(mWebView);
        if (options.webViewInitialier != null) {
            try {
                IWebViewInitialer webViewInitialer = options.webViewInitialier.newInstance();
                webViewInitialer.initialSetting(mWebView, mWebView.getSettings());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        }
        mWebView.setWebChromeClient(new XmqWebChromeClient(options));
        mWebView.setWebViewClient(new XmqWebClient(options));
//        IJSHandler jsHandler = new XmqWebJSHandler(mWebView, options);
//        XmqIPC.getServer(getContext()).register(IJSHandler.class, jsHandler);
//        mWebView.addJavascriptInterface(jsHandler, "JSHandler");
    }

    @SuppressLint("SetJavaScriptEnabled")
    public WebView createWebView(final WebView webView) {
        //api>=21时才能开启
//        WebView.setWebContentsDebuggingEnabled(true);
        //不能横向滚动
        webView.setHorizontalScrollBarEnabled(false);
        //不能纵向滚动
        webView.setVerticalScrollBarEnabled(false);
        //允许截图
        webView.setDrawingCacheEnabled(true);
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //初始化WebSettings
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
//        final String ua = settings.getUserAgentString();
//        settings.setUserAgentString(ua + "Latte");
        //隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        //禁止缩放
        settings.setSupportZoom(false);
        //文件权限
//        settings.setAllowFileAccess(true);
//        settings.setAllowFileAccessFromFileURLs(true);
//        settings.setAllowUniversalAccessFromFileURLs(true);
//        settings.setAllowContentAccess(true);
        //缓存相关
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(false);
        settings.setDatabaseEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        return webView;
    }

    WebView mWebView;

    public boolean canGoBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();   //后退
            //webview.goForward();//前进
            return true;
        }
        return false;
    }
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            return onBackHandle();
//        }
//        return false;
//    }
//
//    protected boolean onBackHandle() {
//        if (mWebView != null) {
//            if (mWebView.canGoBack()) {
//                mWebView.goBack();
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
}
