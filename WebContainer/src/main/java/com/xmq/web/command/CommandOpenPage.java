package com.xmq.web.command;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.auto.service.AutoService;
import com.xmq.basic.framework.BaseApplication;
import com.xmq.web.IWebCommand;
import com.xmq.web.command.BaseWebProcessCommand;

import java.util.Map;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:30
 */
@AutoService({IWebCommand.class})
public class CommandOpenPage extends BaseWebProcessCommand {
    @Override
    public String name() {
        return "openPage";
    }

    @Override
    public int type() {
        return 0;
    }

    @Override
    public void execute(Context context, String action, Map params) {
        if (null == params || !params.containsKey("class")) return;
        String clazzName = String.valueOf(params.get("class"));
        if (!TextUtils.isEmpty(clazzName)) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(BaseApplication.getInstance().getPackageName(), clazzName));
            context.startActivity(intent);
        }
    }
}
