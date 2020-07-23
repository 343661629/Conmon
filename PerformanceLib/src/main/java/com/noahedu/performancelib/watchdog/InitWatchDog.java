package com.noahedu.performancelib.watchdog;

import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;

/**
 * @Description: 检测ANR 使用第三方框架
 * @Author: huangjialin
 * @CreateDate: 2020/7/23 14:35
 */
public class InitWatchDog {
    private boolean mIgnoreDebugger = true;
    private static InitWatchDog sInitWatchDog;
    private boolean mReportMainThreadOnly;



    public static InitWatchDog createInstance() {
        sInitWatchDog = new InitWatchDog();
        return sInitWatchDog;
    }

    /**
     * 断点调试是否忽略  默认忽略
     * @param ignoreDebugger
     * @return
     */
    public InitWatchDog setIgnoreDebugger(boolean ignoreDebugger){
        this.mIgnoreDebugger = ignoreDebugger;
        return this;
    }


    /**
     * 设置是否只监控主线程
     * @return
     */
    public InitWatchDog setReportMainThreadOnly(boolean reportMainThreadOnly) {
        this.mReportMainThreadOnly = reportMainThreadOnly;
        return this;
    }

    /**
     * 启动
     */
    public void start(){
        ANRWatchDog watchDog = new ANRWatchDog();
        watchDog.setIgnoreDebugger(mIgnoreDebugger);
        watchDog.setANRListener(new ANRWatchDog.ANRListener() {
            @Override
            public void onAppNotResponding(ANRError error) {
                error.printStackTrace();
            }
        });
        if(mReportMainThreadOnly){
            watchDog.setReportMainThreadOnly();
        }
        watchDog.start();
    }
}
