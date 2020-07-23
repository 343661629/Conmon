package com.noahedu.conmon.mvvm.view;

import android.util.Log;
import android.widget.Toast;


import com.noahedu.conmon.CallBackManager;
import com.noahedu.conmon.ICallBack;
import com.noahedu.conmon.R;
import com.noahedu.conmon.mvvm.viewmodel.TestViewModel;
import com.noahedu.conmonmodule.base.MvvmActivity;
import com.noahedu.conmonmodule.utils.GToast;
import com.noahedu.conmonmodule.utils.klog.KLog;

import androidx.lifecycle.Observer;

/**
 * @Description: 使用livedata + ViewModel + DataBinding  实现MVVM架构
 * @Author: huangjialin
 * @CreateDate: 2020/6/10 14:10
 */
public class TestMVVMActivity extends MvvmActivity<TestViewModel> implements ICallBack {


    private Observer<String> getTestObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            /**
             * 如果在这里直接使用DataBinding来将数据和控件绑定，那么一个完整的MVVM就形成了
             */
            //Toast.makeText(TestMVVMActivity.this, s, Toast.LENGTH_SHORT).show();
            KLog.e("TestViewModel","---回调---> " + Thread.currentThread().getName());
            GToast.showToast(TestMVVMActivity.this,"--------------> ");
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.app_activity_test_mvvm;
    }

    @Override
    protected TestViewModel createViewModel() {
        return getViewModel(TestViewModel.class);
    }

    @Override
    protected void initData() {
        mViewModel.requestNetWokData().observe(this,getTestObserver);
        CallBackManager.addCallBack(this);
    }

    @Override
    public void dpOperate() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallBackManager.removeCallBack(this);
    }
}
