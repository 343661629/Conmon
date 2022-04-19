package com.noahedu.conmonmodule.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.noahedu.conmonmodule.R;


/**
 * Created by huangjialin on 2018/4/15.
 */

public class WaitingDialog extends Dialog {

    private LayoutInflater factory;

    private TextView mContentTextView;

    private String mContentString;

    public WaitingDialog(Context context, String contentString) {
        this(context, R.style.dialog_style);
        mContentString = contentString;
    }

    public WaitingDialog(Context context) {
        this(context, R.style.dialog_style);
    }

    public WaitingDialog(Context context, int theme) {
        super(context, theme);
        factory = LayoutInflater.from(context);
        windowAttr();
    }

    private void windowAttr() {
        Window win = this.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = 0;// 设置x坐标
        params.y = 0;// 设置y坐标
        params.horizontalMargin = 0;
        win.setAttributes(params);
        this.setCanceledOnTouchOutside(false);
        win.setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        //win.setWindowAnimations(R.style.dialog_anim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(factory.inflate(R.layout.dialog_waiting, null));
        mContentTextView = (TextView) this.findViewById(R.id.tip);

        if (mContentString != null) {
            mContentTextView.setText(mContentString);
        }

    }

    public void doCancle() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

    public void setDialogContent(String content) {
        mContentString = content;
        if (mContentTextView != null) {
            mContentTextView.setText(content);
        }
    }


}
