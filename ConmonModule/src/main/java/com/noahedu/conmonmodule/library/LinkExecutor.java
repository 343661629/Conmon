package com.noahedu.conmonmodule.library;


import androidx.annotation.Nullable;

import com.noahedu.conmonmodule.utils.Utils;

import java.util.List;


/**
 * 按顺序执行程序，包括异步任务  注意这里不属于另外开启线程，需要和线程同步区分
 */
public class LinkExecutor implements Notifier {
    private AfterExecutor afterExecutor;
    private List<Executor> executors;
    private int len;
    private int index;

    public interface BeforeExecutor {
        void preHandle();
    }

    public interface AfterExecutor {
        void onFinishAll();
    }

    public interface Executor {
        void execute(Notifier notifier);
    }

    public void execute(@Nullable BeforeExecutor beforeExecutor, @Nullable AfterExecutor afterExecutor, List<Executor> executors) {
        if (beforeExecutor != null) {
            beforeExecutor.preHandle();
        }
        //this.len = executors.length;
        this.len = executors.size();
        this.index = 0;
        this.afterExecutor = afterExecutor;
        this.executors = executors;
        executeNextTask(len, index, executors);
    }

    public void executeNextTask(final int len, final int index, List<Executor> executors) {
        Executor executor = null;
        if (index < len) {
            if (!Utils.isEmpty(executors)) {
                executor = executors.get(index);
            }
        }
        if (executor != null) {
            executor.execute(this);
        }
    }

    @Override
    public void notifyTaskFinish() {
        index += 1;
        if (index == len) {
            if (afterExecutor != null) {
                afterExecutor.onFinishAll();
            }
        } else {
            executeNextTask(len, index, executors);
        }
    }

    public void backExecutor(int index) {

        if (!Utils.isEmpty(executors) && index >= 0 && index < executors.size()) {

            this.index = index;
        }
    }

    public int getIndex() {
        return this.index;
    }

    /**
     * 退出
     */
    public void exitExecutor() {
        if (null != afterExecutor) {
            afterExecutor = null;
        }
        if (!Utils.isEmpty(executors)) {
            executors.clear();
            executors = null;
        }
    }
}
