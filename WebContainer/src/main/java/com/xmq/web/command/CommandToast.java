package com.xmq.web.command;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.auto.service.AutoService;
import com.xmq.basic.framework.BaseApplication;
import com.xmq.basic.framework.util.ProcessUtil;
import com.xmq.web.BaseMainProcessWebCommand;
import com.xmq.web.IWebCommand;
import com.xmq.web.command.BaseWebProcessCommand;

import java.util.Map;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:30
 */
@AutoService({IWebCommand.class})
public class CommandToast extends BaseMainProcessWebCommand {
    @Override
    public String name() {
        return "showToast";
    }

    @Override
    public void execute(Context context, String action, Map params) {
        if (null == params || !params.containsKey("message")) return;
        String message = String.valueOf(params.get("message"));
        if (!TextUtils.isEmpty(message)) {
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(context, ProcessUtil.getCurrentProcessNameOnly(context)+" : "+message, Toast.LENGTH_SHORT).show());
        }
    }
}
