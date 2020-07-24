package com.noahedu.network.http;

/**
 * @Description: 网络请求结果回调
 * @Author: huangjialin
 * @CreateDate: 2019/6/14 14:37
 */
public interface RequestResultCallBack {
    void onRequestSuc(String type, String str);

    void onRequestErr(String type, String msg);
}
