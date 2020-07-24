package com.noahedu.network.http;

import com.noahedu.network.util.NetWorkUtils;
import com.socks.library.KLog;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


/**
 * @Description: 日志拦截器
 * @Author: huangjialin
 * @CreateDate: 2019/5/22 15:49
 */
public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        Headers headers = request.headers();
        Iterator<String> it = headers.names().iterator();
        StringBuffer headerBuffer = new StringBuffer();
        try {
            while (it.hasNext()) {
                String key = it.next();
                String value = headers.get(key) + ", ";
                headerBuffer.append(key + ":" + value);
            }
            KLog.e(String.format("[url = %s ] 请求header：[%s]", url, headerBuffer.toString() + ""));
            KLog.e("http","---请求的url为--->  " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long t1 = System.nanoTime();
        KLog.e(String.format(Locale.getDefault(), "[url = %s]", url));
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            StringBuilder sb = new StringBuilder("[url = " + url + "] 请求Body[");
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            if (isPlaintext(buffer)) {
                String body = buffer.readString(charset);
                sb.append(body);
            } else {
                sb.append(" (Content-Type = ").append(contentType.toString())
                        .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
            }
            sb.append("]");
//            KLog.e(String.format(Locale.getDefault(), "%s", sb.toString()));
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        ResponseBody body = response.body();
        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = Charset.defaultCharset();
        MediaType contentType = body.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        String bodyString = buffer.clone().readString(charset);
        if (NetWorkUtils.isJson(bodyString)) {
            //KLog.e(url);
            KLog.json(bodyString);
        } else {
            KLog.e( String.format("[url = %s ] 返回body：[%s]", url, bodyString + ""));
        }
        return response;
    }

    boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }


}
