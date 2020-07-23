package com.noahedu.conmon.mvvm.model;

import android.os.Handler;
import android.util.Log;

import com.noahedu.conmon.mvvm.CallBack;

/**
 * @Description: 模拟从网络获取数据
 * @Author: huangjialin
 * @CreateDate: 2020/6/10 14:49
 */
public class TestmvvpModel {

    public void getNetWorkData(final CallBack callBack){
        Log.e("TestmvvpModel","开始请求数据");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callBack.successCallBack("我是从服务器中获取的数据");
            }
        },10);

    }


}
