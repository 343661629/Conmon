package com.noahedu.network.http;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @Description: 定义接口
 * @Author: huangjialin
 * @CreateDate: 2019/5/22 16:17
 */
public interface ApiService {

    /**
     * 以get方式的网络请求接口
     * @param url 可传全路径或者只传baseUrl之后的路径
     * @param map 键值对参数
     */
    @GET("")
    Observable<String> get(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 以post方式的网络请求接口
     * @param url 可传全路径或者只传baseUrl之后的路径
     * @param map 键值对参数
     */
    @POST("")
    Observable<String> post(@Url String url, @QueryMap Map<String, String> map);

    /**
     * post实体
     */
    @POST("")
    Observable<String> postBody(@Url String url, @Body Object body);

    /**
     * post表单
     */
    @FormUrlEncoded
    @POST("")
    Observable<String> postField(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 单图上传
     */
    @Multipart
    @POST("")
    Observable<String> upLoadFile(@Url String url, @Part MultipartBody.Part file);

    /**
     * 多图上传
     */
    @Multipart
    @POST("")
    Observable<String> uploadFiles(@Url String url, @Part("filename") String description, @PartMap() Map<String, RequestBody> maps);

    /**
     * 下载
     */
    @Streaming
    @GET
    Observable<String> downloadFile(@Url String fileUrl);

}
