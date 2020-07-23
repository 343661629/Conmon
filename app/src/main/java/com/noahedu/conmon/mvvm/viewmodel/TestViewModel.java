package com.noahedu.conmon.mvvm.viewmodel;

import android.util.Log;

import com.noahedu.conmon.mvvm.BaseViewModel;
import com.noahedu.conmon.mvvm.CallBack;
import com.noahedu.conmon.mvvm.model.TestmvvpModel;
import com.noahedu.conmonmodule.utils.klog.KLog;

import androidx.lifecycle.MutableLiveData;

/**
 * @Description: 作用和MVP中的presenter类似
 * @Author: huangjialin
 * @CreateDate: 2020/6/10 14:10
 */
public class TestViewModel extends BaseViewModel {
    private MutableLiveData<String> testMutableLivdData = new MutableLiveData<>();
    private TestmvvpModel mModel;

    public TestViewModel() {
        Log.e("TestViewModel","TestViewModel被初始化了");
        mModel = new TestmvvpModel();
    }




    public MutableLiveData<String> requestNetWokData(){
        if(null == testMutableLivdData){
            testMutableLivdData = new MutableLiveData<>();
        }
        mModel.getNetWorkData(new CallBack() {
            @Override
            public void successCallBack(Object data) {
                KLog.e("TestViewModel","---成功---> " + Thread.currentThread().getName());
                testMutableLivdData.postValue((String) data);
            }

            @Override
            public void faildCallBack() {
                KLog.e("TestViewModel","---失败---> " + Thread.currentThread().getName());
            }
        });
        return testMutableLivdData;
    }








}
