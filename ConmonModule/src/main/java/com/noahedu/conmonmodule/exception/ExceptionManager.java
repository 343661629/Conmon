package com.noahedu.conmonmodule.exception;


import com.noahedu.conmonmodule.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: huangjialin
 * @CreateDate: 2019/10/18 16:30
 */
public class ExceptionManager {
    private static ExceptionManager instance = null;
    private List<ExceptionListence> exceptionListences = new ArrayList<ExceptionListence>();

    public static ExceptionManager getInstance() {
        if (instance == null) {
            synchronized (ExceptionManager.class) {
                if (instance == null) {
                    instance = new ExceptionManager();
                }
            }
        }
        return instance;
    }

    /**
     * 注册监听器
     * @param exceptionListence
     */
    public void regist(ExceptionListence exceptionListence) {
        if (null != exceptionListence && !exceptionListences.contains(exceptionListence)) {
            exceptionListences.add(exceptionListence);
        }
    }

    /**
     * 移除监听器
     */
    public void unRegist(ExceptionListence exceptionListence) {
        if (null != exceptionListence && exceptionListences.contains(exceptionListence)) {
            exceptionListences.remove(exceptionListence);
        }
    }


    /**
     * 移除所有的观测者，避免内存泄露
     */
    public void unRegisterAll() {
        if (!Utils.isEmpty(exceptionListences)) {
            exceptionListences.clear();
        }
    }

    /**
     * 捕获到异常
     */
    public void notifyException() {
        if (!Utils.isEmpty(exceptionListences)) {
            for (int i = 0; i < exceptionListences.size(); i++) {
                exceptionListences.get(i).exceptionListence();
            }
        }
    }





}
