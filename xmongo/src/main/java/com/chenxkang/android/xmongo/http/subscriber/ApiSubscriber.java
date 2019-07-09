package com.chenxkang.android.xmongo.http.subscriber;


import android.support.annotation.NonNull;

import com.chenxkang.android.xmongo.http.exception.ApiException;

import java.io.IOException;

import io.reactivex.observers.DisposableObserver;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : API统一订阅者
 */

abstract class ApiSubscriber<T> extends DisposableObserver<T> {

    public ApiSubscriber() {
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof ApiException) {
            onApiError((ApiException) e);
        } else {
            try {
                onApiThrowableError(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    protected abstract void onApiError(ApiException e);
    protected abstract void onApiThrowableError(Throwable throwable) throws IOException;
}
