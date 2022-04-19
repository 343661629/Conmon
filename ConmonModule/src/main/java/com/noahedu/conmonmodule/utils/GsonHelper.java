package com.noahedu.conmonmodule.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huangjialin on 2018/4/14.
 * json数据解析帮助类
 */

public class GsonHelper {

    public static String toJson(Object src) {
        if (src == null) {
            return "";
        }
        Gson gson = new Gson();
        return gson.toJson(src);
    }


    public static String toJson(Object src, Type typeOfSrc) {
        if (src == null) {
            return "";
        }
        Gson gson = new Gson();
        return gson.toJson(src, typeOfSrc);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        try {
            T result = gson.fromJson(json, type);
            return result;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> fromJsonToList(String json, Class<T[]> clazz) {
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        try {
            T[] arr = new Gson().fromJson(json, clazz);
            if (arr == null || arr.length <= 0) {
                return new ArrayList<>();
            }
            List<T> ts = Arrays.asList(arr);
            return new ArrayList<>(ts);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
