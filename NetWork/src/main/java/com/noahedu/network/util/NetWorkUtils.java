package com.noahedu.network.util;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: huangjialin
 * @CreateDate: 2019/11/25 10:02
 */
public class NetWorkUtils {
    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(List list) {
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断字符串是否是json格式
     *
     * @param json
     * @return
     */
    public static boolean isJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        } catch (JsonParseException e) {
            return false;
        }
    }



}
