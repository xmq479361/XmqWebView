package com.xmq.webdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xmq.basic.framework.util.ProcessUtil;
import com.xmq.web.mainprocess.MainProcessCommandDispatcher;
import com.xmq.web.webprocess.WebProcessCommandDispatcher;

/**
 * @author xmqyeah
 */
public class MainProcessOtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_process_other);
        TextView tvDesc = findViewById(R.id.tv_main_other_desc);
        tvDesc.setText(ProcessUtil.getCurrentProcessNameOnly(this));
    }

    public void response2Web(View view) {
        Log.i("MainOtherWeb", "resposne2Web");
//        finish();
    }
}