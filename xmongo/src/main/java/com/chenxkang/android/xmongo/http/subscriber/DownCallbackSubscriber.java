package com.chenxkang.android.xmongo.http.subscriber;


import com.chenxkang.android.xmongo.http.callback.ApiCallback;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 包含下载进度回调的订阅者
 */

public class DownCallbackSubscriber<T> extends ApiCallbackSubscriber<T> {

    public DownCallbackSubscriber(ApiCallback<T> callback) {
        super(callback);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        callback.onSuccess(super.data);
    }
}
