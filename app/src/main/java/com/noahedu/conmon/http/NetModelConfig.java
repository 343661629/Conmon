package com.noahedu.conmon.http;

import com.noahedu.conmonmodule.utils.klog.KLog;
import com.noahedu.network.http.IConfig;
import com.noahedu.network.http.NetWorkConfig;
import com.noahedu.network.http.RequestResultCallBack;

import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @Description: java类作用描述
 * @Author: huangjialin
 * @CreateDate: 2020/7/24 14:34
 * Google表示，为保证用户数据和设备的安全，针对下一代 Android 系统(Android P) 的应用程序，将要求默认使用加密连接，这意味着 Android P 将禁止 App
 * 使用所有未加密的连接，因此运行 Android P 系统的安卓设备无论是接收或者发送流量，未来都不能明码传输，需要使用下一代(Transport Layer Security)传输层安全协议，
 * 而 Android Nougat 和 Oreo 则不受影响。
 *
 * 解决方案：1、更换为https请求方式   2、将targetSdkVersion降为 到27以下，
 * 3、在res下创建一个文件
 * <?xml version="1.0" encoding="utf-8"?>
 * <network-security-config>
 *     <base-config cleartextTrafficPermitted="true" />
 * </network-security-config>
 *
 *在然后在APP的AndroidManifest.xml文件下的application标签增加以下属性   android:networkSecurityConfig="@xml/network_security_config"
 *
 */
public class NetModelConfig implements IConfig {
    private static NetModelConfig sInstance = null;
    private HashMap<String,String> headParam = new HashMap<String,String>();


    public static NetModelConfig getInstance(){
        if(null == sInstance){
            synchronized (NetModelConfig.class){
                if(null == sInstance){
                    sInstance = new NetModelConfig();
                }
            }
        }
        return sInstance;
    }


    public void test(RequestResultCallBack requestResultCallBack) {
        HashMap<String, String> param = new HashMap<String, String>();
        NetWorkConfig.getInstance().get("http://aaa", param, NetWorkConstants.TEST, requestResultCallBack, this);
    }


    @Override
    public String addBaseUrl() {
        return "https://www.baidu.com/";
    }

    @Override
    public HashMap<String, String> addHeader() {
        KLog.e("huangjisjnfsu","--------------");;
        return headParam;
    }

    @Override
    public SSLSocketFactory sslSocketFactory() {
        return null;
    }

    @Override
    public HostnameVerifier hostNameVerifier() {
        return null;
    }

    @Override
    public X509TrustManager x509TrustManager() {
        return null;
    }
}
