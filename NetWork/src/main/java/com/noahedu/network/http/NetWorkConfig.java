package com.noahedu.network.http;

import android.content.Context;

import com.socks.library.KLog;

import java.io.File;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * @Author: huangjialin
 * @CreateDate: 2019/5/22 16:56
 */
public class NetWorkConfig {
    private static NetWorkConfig instance = null;

    public static NetWorkConfig getInstance() {
        if (instance == null) {
            synchronized (NetWorkConfig.class) {
                if (instance == null) {
                    instance = new NetWorkConfig();
                }
            }
        }
        return instance;
    }


//===================提供对外调用===================================开始=======================================================

    /**
     * get请求模式
     */
    public void get(String url, HashMap<String, String> param, String requestType, RequestResultCallBack requestResultCallBack, IConfig config) {
        getRetrofitFactory(config)
                .getApiService().get(url, param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber(requestType, requestResultCallBack));
    }

    /**
     * post请求模式
     */
    public void post(String url, HashMap<String, String> param, String requestType, RequestResultCallBack requestResultCallBack, IConfig config) {
        getRetrofitFactory(config)
                .getApiService().post(url, param)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber(requestType, requestResultCallBack));
    }

    /**
     * post请求模式  参数body形式
     */
    public void post(String url, Object body, String requestType, RequestResultCallBack requestResultCallBack, IConfig config) {
        getRetrofitFactory(config)
                .getApiService().postBody(url, body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber(requestType, requestResultCallBack));
    }

    /**
     * post请求模式  表单形式
     */
    public void postField(Context mContext, String url, HashMap<String, String> fieldMap, String requestType, RequestResultCallBack requestResultCallBack, IConfig config) {
        getRetrofitFactory(config)
                .getApiService().postField(url, fieldMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber(requestType, requestResultCallBack));
    }

    /**
     * 图片上传   单图上传  未测试
     */
    public void upLoadFile(Context mContext, String url, String filePath, String requestType, RequestResultCallBack requestResultCallBack, IConfig config) {
        getRetrofitFactory(config)
                .getApiService().upLoadFile(url, getMultipartBodyPart(filePath))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber(requestType, requestResultCallBack));
    }

    /**
     * 图片下载  未测试
     */
    public void downloadFile(Context mContext, String url, String requestType, RequestResultCallBack requestResultCallBack, IConfig config) {
        getRetrofitFactory(config)
                .getApiService().downloadFile(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber(requestType, requestResultCallBack));
    }

//====================================================结束=========================================================


    /**
     * 文件上传
     */
    private MultipartBody.Part getMultipartBodyPart(String mPath) {
        File file = new File(mPath);
        //multipart/form-data 格式
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //file - 为上传参数的 键名
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return body;
    }

    /**
     * 文件上传
     */
    private MultipartBody.Part getMultipartBodyPart(File file) {
        //multipart/form-data 格式
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //file - 为上传参数的 键名
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return body;
    }

    /**
     * 获取RetrofitFactory,通过获取到RetrofitFactory，从而获取到apiservice对象
     */
    private RetrofitFactory getRetrofitFactory(IConfig config) {
        String baseUrl = config.addBaseUrl();
        HashMap<String, String> headerMap = config.addHeader();
        SSLSocketFactory sslSocketFactory = config.sslSocketFactory();
        X509TrustManager trustManager = config.x509TrustManager();
        HostnameVerifier hostnameVerifier = config.hostNameVerifier();
        KLog.e("baseUrl--->" + baseUrl + "    " + "headerMap--->" + headerMap.size());
        RetrofitFactory factory = new RetrofitFactory.Builder()
                .setHeaders(headerMap)
                .setBaseUrl(baseUrl)
                .setSSLSocketFactory(sslSocketFactory, trustManager, hostnameVerifier)
                .build();
        return factory;
    }


}
