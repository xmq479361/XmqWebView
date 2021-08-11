package com.xmq.web.command;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.xmq.web.IWebCommand;
import com.xmq.web.WebConstants;
import com.xmq.web.webprocess.WebLog;
import com.xmq.web.webprocess.WebProcessCommandDispatcher;

import java.util.List;
import java.util.Map;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/10 21:30
 */
@AutoService({IWebCommand.class})
public class CommandShowDialog extends BaseWebProcessCommand {
    @Override
    public String name() {
        return "showDialog";
    }

    @Override
    public int type() {
        return 0;
    }

    @Override
    public void execute(Context context, String action, Map params) {
        if (null == params || !params.containsKey("title")) return;
        String title = String.valueOf(params.get("title"));
        String content = String.valueOf(params.get("content"));
        WebLog.w(name()+ " execute: "+title+", "+content);
        List<Map<String, String>> buttons = (List<Map<String, String>>) params.get("buttons");
        WebLog.w(name() + " buttons: "+new Gson().toJson(buttons));
        int canceledOutside = 1;
        if (params.get("canceledOutside") != null) {
            canceledOutside = (int) (double) params.get("canceledOutside");
        }
        final String callbackName = (String) params.get(WebConstants.WEB2NATIVE_CALLBACk);
        if (!TextUtils.isEmpty(content)) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(content)
                    .create();
            dialog.setCanceledOnTouchOutside(canceledOutside == 1 ? true : false);
            if (buttons != null && !buttons.isEmpty()) {
                for (int i = 0; i < buttons.size(); i++) {
                    final Map<String, String> button = buttons.get(i);
                    int buttonWhich = getDialogButtonWhich(i);
                    if (buttonWhich == 0) return;
                    dialog.setButton(buttonWhich, button.get("title"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            button.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName);
//                            resultBack.onResult(WebConstants.SUCCESS, name(), button);
                            WebProcessCommandDispatcher.getInstance().response(callbackName, button);
                        }
                    });
                }
            }
            dialog.show();
        }
    }
    private int getDialogButtonWhich(int index) {
        switch (index) {
            case 0:
                return DialogInterface.BUTTON_POSITIVE;
            case 1:
                return DialogInterface.BUTTON_NEGATIVE;
            case 2:
                return DialogInterface.BUTTON_NEUTRAL;
        }
        return 0;
    }
    class ButtonModel {
        String text;
        String id;
    }
}
