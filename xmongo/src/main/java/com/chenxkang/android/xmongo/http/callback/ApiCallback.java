package com.chenxkang.android.xmongo.http.callback;

import java.io.IOException;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 请求接口的回调
 */

public abstract class ApiCallback<T> {

    public abstract void onSuccess(T data);

    public abstract void onFail(int errCode, String errMsg);

    public abstract void onException(String cause);

    public abstract void onThrowable(Throwable throwable) throws IOException;
}
