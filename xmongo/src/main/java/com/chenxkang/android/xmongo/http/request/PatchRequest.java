package com.chenxkang.android.xmongo.http.request;


import com.chenxkang.android.xmongo.MongoHttp;
import com.chenxkang.android.xmongo.cache.CacheResult;
import com.chenxkang.android.xmongo.http.api.ApiManager;
import com.chenxkang.android.xmongo.http.callback.ApiCallback;
import com.chenxkang.android.xmongo.http.subscriber.ApiCallbackSubscriber;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * author: chenxkang
 * time  : 17/9/28
 * desc  : Patch请求
 */
public class PatchRequest extends BaseHttpRequest<PatchRequest> {

    public PatchRequest(String suffixUrl) {
        super(suffixUrl);
    }

    @Override
    protected <T> Observable<T> execute(Type type) {
        return apiService.patch(suffixUrl, params).compose(this.<T>norTransformer(type));
    }

    @Override
    protected <T> Observable<CacheResult<T>> cacheExecute(Type type) {
        return this.<T>execute(type).compose(MongoHttp.getApiCache().<T>transformer(cacheMode, type));
    }

    @Override
    protected <T> void execute(ApiCallback<T> callback) {
        DisposableObserver disposableObserver = new ApiCallbackSubscriber(callback);
        if (super.tag != null) {
            ApiManager.getInstance().add(super.tag, disposableObserver);
        }
        if (isLocalCache) {
            this.cacheExecute(getSubType(callback)).subscribe(disposableObserver);
        } else {
            this.execute(getType(callback)).subscribe(disposableObserver);
        }
    }
}
