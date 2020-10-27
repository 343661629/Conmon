package com.noahedu.conmonmodule.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.noahedu.conmonmodule.utils.klog.KLog;
import com.tencent.mmkv.MMKV;

/**
 * @Description: 使用MMKV来进行数据存储，替换使用Sp ，相比于SP，MMKV效率更高，线程安全，储存数据加密
 * @Author: huangjialin
 * @CreateDate: 2020/10/27
 */
public class MMKVUtils {
    private static String TAG = "MMKVUtils";


    /**
     * MMKV初始化     可在应用启动的时候进行初始化
     */
    public static void sInitMMKV(Context mContext) {
        String rootDir = MMKV.initialize(mContext);
        KLog.w(TAG, "MMKV 初始化成功，MMKV Root:  " + rootDir);
    }


    /**
     * 存储数据
     */
    public static void put(String key, Object object) {
        synchronized (MMKVUtils.class) {
            MMKV kv = MMKV.defaultMMKV();
            if (object instanceof String) {
                kv.encode(key, (String) object);
            } else if (object instanceof Integer) {
                kv.encode(key, (Integer) object);
            } else if (object instanceof Boolean) {
                kv.encode(key, (Boolean) object);
            } else if (object instanceof Float) {
                kv.encode(key, (Float) object);
            } else if (object instanceof Long) {
                kv.encode(key, (Long) object);
            } else if (object instanceof Double) {
                kv.encode(key, (Double) object);
            } else {
                kv.encode(key, object.toString());
            }
        }
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(String key, Object defaultObject) {
        synchronized (MMKVUtils.class) {
            MMKV kv = MMKV.defaultMMKV();
            if (defaultObject instanceof String) {
                return kv.decodeString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return kv.decodeInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return kv.decodeBool(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return kv.decodeFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return kv.decodeLong(key, (Long) defaultObject);
            } else if (defaultObject instanceof Double) {
                return kv.decodeDouble(key, (Double) defaultObject);
            }
        }
        return null;
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        synchronized (MMKVUtils.class) {
            MMKV kv = MMKV.defaultMMKV();
            kv.removeValueForKey(key);
        }
    }


    /**
     * 清除所有数据
     */
    public static void clear() {
        synchronized (MMKVUtils.class) {
            MMKV kv = MMKV.defaultMMKV();
            kv.clear();
        }
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        synchronized (MMKVUtils.class) {
            MMKV kv = MMKV.defaultMMKV();
            return kv.contains(key);
        }
    }


}
