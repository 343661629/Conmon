package com.noahedu.conmonmodule.utils;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: huangjialin
 * @CreateDate: 2020/7/22 11:42
 */
public class Utils {


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


}
