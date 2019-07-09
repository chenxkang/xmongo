package com.chenxkang.android.xmongo.http.subscriber;


import android.support.annotation.NonNull;

import com.chenxkang.android.xmongo.http.callback.ApiCallback;
import com.chenxkang.android.xmongo.http.exception.ApiException;

import java.io.IOException;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 包含回调的订阅者，若订阅，则上层在不使用订阅者的情况下可获得回调
 */

public class ApiCallbackSubscriber<T> extends ApiSubscriber<T> {

    public ApiCallback<T> callback;
    public T data;

    public ApiCallbackSubscriber(ApiCallback<T> callback) {
        if (callback == null)
            throw new NullPointerException("the callback is null");

        this.callback = callback;
    }

    @Override
    protected void onApiError(ApiException e) {
        if (e == null) {
            callback.onFail(-1, "The ApiException is null.");
            return;
        }
        callback.onFail(e.getCode(), e.getMessage());

        String stackTrace = "";
        for (StackTraceElement element : e.getStackTrace()) {
            stackTrace += element.toString() + "\n";
        }

        callback.onException(stackTrace);
    }

    @Override
    protected void onApiThrowableError(Throwable throwable) throws IOException {
        callback.onThrowable(throwable);
    }

    @Override
    public void onNext(@NonNull T t) {
        this.data = t;
        callback.onSuccess(t);
    }

    @Override
    public void onComplete() {

    }

    public T getData() {
        return data;
    }
}
