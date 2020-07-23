package com.noahedu.conmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.noahedu.conmon.mvvm.view.TestMVVMActivity;

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

                Intent intent = new Intent(MainActivity.this, TestMVVMActivity.class);
                startActivity(intent);
            }
        });
    }
}
