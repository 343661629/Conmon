package com.noahedu.network.http;

import android.content.Context;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @Description: retrofit配置
 * @Author: huangjialin
 * @CreateDate: 2019/5/22 15:39
 * retrofit 官网 https://square.github.io/retrofit/
 */
public class RetrofitFactory {
    private static final int REQUESTTIMEOUT = 15;
    private Map<String, String> mHeadMap = new HashMap<>(); //用来存放头部信息
    private HeaderInterceptor headerInterceptor = null;
    private String baseUrl;
    private SSLSocketFactory socketFactory;
    private X509TrustManager trustManager;
    private HostnameVerifier hostnameVerifier;
    private static Context mContext;

    public static void setContext(Context context){
        mContext = context;
    }


    public RetrofitFactory(Builder builder) {
        this.mHeadMap.clear();
        this.mHeadMap = builder.map;
        this.baseUrl = builder.baseUrl;
        this.socketFactory = builder.socketFactory;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.trustManager = builder.trustManager;
        headerInterceptor = new HeaderInterceptor(mHeadMap);
    }

    /**
     * retrofit配置
     */
    private Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
        return retrofit;
    }


    private OkHttpClient getOkHttpClient() {
        OkHttpClient client = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (socketFactory == null) {
            client = builder
                    .addInterceptor(new LogInterceptor())
                    .addInterceptor(headerInterceptor)
                    .connectTimeout(REQUESTTIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUESTTIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUESTTIMEOUT, TimeUnit.SECONDS)
                    .build();
        } else {
            client =builder
                    .addInterceptor(new LogInterceptor())
                    .addInterceptor(headerInterceptor)
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(socketFactory, trustManager)
                    .connectTimeout(REQUESTTIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUESTTIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUESTTIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
        return client;
    }


    /**
     * 获取接口服务对象
     */
    public ApiService getApiService() {
        ApiService apiService = getRetrofit().create(ApiService.class);
        return apiService;
    }


    /**
     * 通过builder设计模式将头部信息传递进来
     */
    public static class Builder {
        Map<String, String> map = new HashMap<>();
        String baseUrl;
        SSLSocketFactory socketFactory;
        X509TrustManager trustManager;
        HostnameVerifier hostnameVerifier;

        public Builder setHeaders(Map<String, String> map) {
            this.map = map;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setSSLSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager, HostnameVerifier hostnameVerifier) {
            this.socketFactory = sslSocketFactory;
            this.trustManager = trustManager;
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        public RetrofitFactory build() {
            return new RetrofitFactory(this);
        }
    }


}
