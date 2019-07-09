package com.chenxkang.android.xmongo.http.func;

import android.support.annotation.NonNull;

import com.chenxkang.android.xmongo.http.exception.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 重试机制
 */

public class ApiRetryFunc implements Function<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private final int retryDelayMills;
    private int retryCount;

    public ApiRetryFunc(int maxRetries, int retryDelayMills) {
        this.maxRetries = maxRetries;
        this.retryDelayMills = retryDelayMills;
    }

    @Override
    public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                if (++retryCount <= maxRetries && (throwable instanceof SocketTimeoutException || throwable instanceof ConnectException)){
                    return Observable.timer(retryDelayMills, TimeUnit.MILLISECONDS);
                }

                return Observable.error(ApiException.handleException(throwable));
            }
        });
    }
}
