package com.noahedu.conmon;

import android.app.Application;

import com.noahedu.conmonmodule.utils.MMKVUtils;
import com.noahedu.network.http.RetrofitFactory;
import com.noahedu.performancelib.blockcanary.InitBlockCanary;
import com.noahedu.performancelib.leakcanary.InitLeakCanary;
import com.noahedu.performancelib.strictmode.InitStrictMode;
import com.noahedu.performancelib.watchdog.InitWatchDog;

/**
 * @Description: java类作用描述
 * @Author: huangjialin
 * @CreateDate: 2020/7/23 15:11
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*InitWatchDog.createInstance()
                .setIgnoreDebugger(true)
                .start();*/

        /*InitBlockCanary.createInstance(this)
                .setBlockThreshold(500)
                .start();*/
        /*InitLeakCanary.start(this);*/

        /*RetrofitFactory.setContext(this);*/
//        InitStrictMode.createStrictMode().start();

        MMKVUtils.sInitMMKV(this);

    }


}
