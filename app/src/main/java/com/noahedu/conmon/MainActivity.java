package com.noahedu.conmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.noahedu.conmon.http.NetModelConfig;
import com.noahedu.conmon.mvvm.view.TestMVVMActivity;
import com.noahedu.conmonmodule.utils.MMKVUtils;
import com.noahedu.conmonmodule.utils.klog.KLog;
import com.noahedu.network.http.RequestResultCallBack;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* try {
                    Thread.sleep(8 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                /*Intent intent = new Intent(MainActivity.this, TestMVVMActivity.class);
                startActivity(intent);*/

               /* NetModelConfig.getInstance().test(new RequestResultCallBack() {
                    @Override
                    public void onRequestSuc(String type, String str) {
                        Log.e("huangjisjnfsu","-------成功----------> " + str);
                    }

                    @Override
                    public void onRequestErr(String type, String msg) {
                        Log.e("huangjisjnfsu","--------失败---------> " + msg);
                    }
                });*/

                MMKVUtils.put("huangjialin","hello work");

                String value = (String) MMKVUtils.get("huangjialin","m默认值");
                KLog.e("sdhfhasdhfb","-----> " + value);




            }
        });
    }
}
