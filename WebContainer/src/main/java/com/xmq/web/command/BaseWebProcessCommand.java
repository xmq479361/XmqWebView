package com.xmq.web.command;

import com.xmq.web.IWebCommand;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/11 22:28
 */
public abstract class BaseWebProcessCommand implements IWebCommand {

    @Override
    public final boolean runMainProcess() {
        return false;
    }

    @Override
    public int type() {
        return 0;
    }

}
