package com.noahedu.conmonmodule.frameanimation.util;

import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * @Description: 动画相关工具类
 * @Author: huangjialin
 * @CreateDate: 2020/7/22 11:48
 */
public class AnimaUtils {

    private static Method SystemPropertiesMethod_get = null;
    private static Method SystemPropertiesMethod_set = null;
    public static final String FULLSCREEN_SWITCH_PROPERTY = "persist.noah.pmc.switch.fullscreen";


    static {
        try {
            Class<?> clasz = Class.forName("android.os.SystemProperties");
            SystemPropertiesMethod_get = clasz.getMethod("get", String.class);
            SystemPropertiesMethod_get.setAccessible(true);

            SystemPropertiesMethod_set = clasz.getMethod("set", String.class, String.class);
            SystemPropertiesMethod_set.setAccessible(true);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * String转Int时,如果String为""会抛异常
     */
    public static int stringToInt(String text, int defualt) {
        if (TextUtils.isEmpty(text)) {
            return defualt;
        } else {
            return Integer.parseInt(text);
        }
    }

    public static String getSystemProperties(String name) {
        if (SystemPropertiesMethod_get != null) {
            try {
                return (String) SystemPropertiesMethod_get.invoke(null, name);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "";
    }

}
