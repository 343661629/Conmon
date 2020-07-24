package com.noahedu.network.http;

/**
 * @Description: 注意，该类根据后端返回的数据结构来写
 * @Author: huangjialin
 * @CreateDate: 2019/5/23 17:49
 */
public class ApiException extends RuntimeException {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
