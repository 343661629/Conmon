package com.noahedu.network.http;

import android.text.TextUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description: 头部拦截器
 * @Author: huangjialin
 * @CreateDate: 2019/5/22 15:54
 */
public class HeaderInterceptor implements Interceptor {
    private Map<String, String> headMap;

    public HeaderInterceptor(Map<String, String> headMap) {
        this.headMap = headMap;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Response response = null;
        synchronized (this) {
            Request.Builder builder = chain.request().newBuilder();
            if (headMap != null && headMap.size() > 0) {
                Set<String> keys = headMap.keySet();
                for (String headerKey : keys) {
                    builder.addHeader(headerKey, headMap.get(headerKey)).build();
                }
                try {
                    response = chain.proceed(builder.build());
                } catch (SocketTimeoutException e) {
                    e.getLocalizedMessage();
                }
            } else {
                response = chain.proceed(builder.build());
            }
        }
        return response;
    }
}
