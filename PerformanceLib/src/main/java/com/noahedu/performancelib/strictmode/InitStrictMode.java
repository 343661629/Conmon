package com.noahedu.performancelib.strictmode;


import android.os.StrictMode;

/**
 * @Description: StrictMode  严苛模式，检测开发过程中发现不合理的代码
 * @Author: huangjialin
 * @CreateDate: 2020/7/27 11:37
 */
public class InitStrictMode {

    public static InitStrictMode createStrictMode() {
        return new InitStrictMode();
    }


    /**
     * 日志过滤使用关键字“StrictMode”
     */
    public void start() {
        //开启Thread策略模式
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectNetwork()//监测主线程使用网络io
//                .detectCustomSlowCalls()//监测自定义运行缓慢函数
//                .detectDiskReads() // 检测在UI线程读磁盘操作
//                .detectDiskWrites() // 检测在UI线程写磁盘操作
                .detectAll()
                .penaltyLog() //写入日志
                .penaltyDialog()//监测到上述状况时弹出对话框
                .build());

        //开启VM策略模式
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()//监测sqlite泄露
//                .detectLeakedClosableObjects()//监测没有关闭IO对象
//                .setClassInstanceLimit(MainActivity.class, 1) // 设置某个类的同时处于内存中的实例上限，可以协助检查内存泄露
//                .detectActivityLeaks()
                .detectAll()
                .penaltyLog()//写入日志
                .build());

    }

}
