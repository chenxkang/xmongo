package com.chenxkang.android.xmongo.http.interceptor;

import android.support.annotation.NonNull;

import com.chenxkang.android.xmongo.http.body.UploadProgressRequestBody;
import com.chenxkang.android.xmongo.http.callback.UICallback;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: chenxkang
 * time  : 17/9/27
 * desc  : 上传进度拦截
 */

public class UploadProgressInterceptor implements Interceptor {

    private UICallback callback;

    public UploadProgressInterceptor(UICallback callback) {
        this.callback = callback;
        if (callback == null) {
            throw new NullPointerException("this callback must not null.");
        }
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null) {
            return chain.proceed(originalRequest);
        }
        Request progressRequest = originalRequest.newBuilder()
                .method(originalRequest.method(),
                        new UploadProgressRequestBody(originalRequest.body(), callback))
                .build();
        return chain.proceed(progressRequest);
    }
}
