package com.noahedu.conmonmodule.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.noahedu.conmonmodule.utils.klog.KLog;

/**
 * @Description: Toast工具类
 * @Author: huangjialin
 * @CreateDate: 2020/7/21 16:12
 */
public class GToast {
    private static Toast sToast;

    private GToast() {
        throw new AssertionError();
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId));
    }

    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getResources().getString(resId), duration);
    }

    public static void showToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int duration, Object... args) {
        showToast(context, String.format(text, args), duration);
    }

    public static void showToast(Context context, CharSequence text, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), text, duration);
        } else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

}
