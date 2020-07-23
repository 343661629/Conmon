package com.noahedu.conmonmodule.base;

/**
 * @Description: 对外提供的公共父类
 * @Author: huangjialin
 * @CreateDate: 2020/7/21 16:22
 */
public abstract class ConmonActivity extends BaseActivity{

    @Override
    protected void initView() {
        if(getLayoutId() <= 0){
            throw new NullPointerException("You must set up the XML file");
        }
        setContentView(getLayoutId());
    }

    protected abstract int getLayoutId();


}
