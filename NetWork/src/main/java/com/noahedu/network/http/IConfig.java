package com.noahedu.network.http;

import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @Description: 对外提供接口来给使用者提供配置数据
 * @Author: huangjialin
 * @CreateDate: 2019/5/24 8:45
 */
public interface IConfig {
    String addBaseUrl(); //base URL

    HashMap<String, String> addHeader(); //增加头部header

    SSLSocketFactory sslSocketFactory(); //对于请求使用SSL加密证书的，则需要实现该方法

    HostnameVerifier hostNameVerifier();

    X509TrustManager x509TrustManager();


}
