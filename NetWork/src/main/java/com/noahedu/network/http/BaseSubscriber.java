package com.noahedu.network.http;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * Created by huangjialin
 * 订阅者基类，处理了一下请求超时或e.getMessage为空的错误以及打log
 */
public class BaseSubscriber implements Observer {
    private String errorMsg;
    private RequestResultCallBack RequestResultCallBack;
    private Disposable d;
    private String requestType;

    public BaseSubscriber(String type, RequestResultCallBack RequestResultCallBack) {
        this.RequestResultCallBack = RequestResultCallBack;
        this.requestType = type;
    }


    @Override
    public void onSubscribe(Disposable disposable) {
        if(null != d && !d.isDisposed()){
            d.dispose();
        }
        this.d = disposable;
    }

    @Override
    public void onNext(Object t) {
        if (null != t) {
            RequestResultCallBack.onRequestSuc(requestType,(String)t);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (!d.isDisposed()) {
            d.dispose();
        }
        if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) { //连接超时
            errorMsg = "连接超时";
        } else if (throwable instanceof ConnectException || throwable instanceof HttpException) { //网络异常
            errorMsg = "网络异常";
        } else if (throwable instanceof ApiException) { //请求接口时抛出错误，如密码不正确，，账号注册等等
           // errorMsg = ((ApiException) throwable).getMsg();
        } else if (throwable instanceof HttpException) {
            HttpException exception = (HttpException) throwable;
            int code = exception.response().code();
            if (code == 502 || code == 503 || code == 504) {
                //TODO 根据相应的错误码返回不同的错误信息
            }
        } else {
            errorMsg = throwable.getMessage();
        }
        RequestResultCallBack.onRequestErr(requestType,errorMsg);
    }

    @Override
    public void onComplete() {
        if (null != d && !d.isDisposed()) {
            d.dispose();
        }
    }


}
