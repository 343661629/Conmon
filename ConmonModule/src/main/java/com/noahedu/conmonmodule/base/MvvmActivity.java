package com.noahedu.conmonmodule.base;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @Description: activity使用的是MVVM模式开发，则需要继承该类 ，livedata + ViewModel + DataBinding  实现MVVM架构
 * @Author: huangjialin
 * @CreateDate: 2020/7/21 16:28
 */
public abstract class MvvmActivity<VM> extends ConmonActivity{
    protected VM mViewModel;

    @Override
    protected void initView() {
        super.initView();
        mViewModel = createViewModel();
    }

    protected abstract VM createViewModel();

    /**
     * 注意：这里不能使用 VM 来替代 T ，getViewModel是一个公共方法，会存在一个activity使用多个ViewModel情况
     * @param classModel
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T getViewModel(Class<T> classModel){
        return new ViewModelProvider(this).get(classModel);
    }

}
