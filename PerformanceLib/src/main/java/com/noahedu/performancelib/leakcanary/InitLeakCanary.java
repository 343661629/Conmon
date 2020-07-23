package com.noahedu.performancelib.leakcanary;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * @Description: leakcanary 初始化
 * @Author: huangjialin
 * @CreateDate: 2020/7/23 16:46
 */
public class InitLeakCanary {

    public static void start(Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            return;
        }
        LeakCanary.install(application);
    }


}
