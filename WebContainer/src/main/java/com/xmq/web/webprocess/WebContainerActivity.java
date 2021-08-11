package com.xmq.web.webprocess;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xmq.web.R;
import com.xmq.web.WebServerManager;
import com.xmq.web.bean.WebRequestOptions;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/7 18:22
 */
public class WebContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity_container);

        WebLog.d("WebContainerActivity onCreate: ");
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = savedInstanceState;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.add(R.id.web_lv_fragment, mWebViewFragment = WebViewFragment.get(extras));
        fragmentTransaction.commit();

        WebRequestOptions requestOptions = extras.getParcelable(WebServerManager.EXTRA_REQUEST_OPTIONS);
        getSupportActionBar().setTitle(requestOptions.title);
        getSupportActionBar().show();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            WebLog.d("WebContainerActivity onWindowFocusChanged: "+hasFocus);
        }
    }

    WebViewFragment mWebViewFragment;
    @Override
    public void onBackPressed() {
        if (!mWebViewFragment.canGoBack()) {
            super.onBackPressed();
        }
    }
}
