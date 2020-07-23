package com.noahedu.conmonmodule.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @Description: Activity的父类
 * @Author: huangjialin
 * @CreateDate: 2020/7/21 16:12
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
        initData();
    }


    protected abstract void initView();
    protected abstract void initData();





}
