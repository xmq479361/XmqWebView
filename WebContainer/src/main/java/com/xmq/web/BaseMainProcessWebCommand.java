package com.xmq.web;

/**
 * @author xmqyeah
 * @CreateDate 2021/8/11 22:27
 */
public abstract class BaseMainProcessWebCommand implements IWebCommand {

    @Override
    public final boolean runMainProcess() {
        return true;
    }

    @Override
    public int type() {
        return 0;
    }
}
