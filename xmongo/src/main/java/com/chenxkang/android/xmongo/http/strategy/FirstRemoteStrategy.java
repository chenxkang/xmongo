package com.chenxkang.android.xmongo.http.strategy;


import com.chenxkang.android.xmongo.cache.ApiCache;
import com.chenxkang.android.xmongo.cache.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 缓存策略--优先网络
 */
public class FirstRemoteStrategy<T> extends CacheStrategy<T> {
    @Override
    public <T> Observable<CacheResult<T>> execute(ApiCache apiCache, String cacheKey, Observable<T> source, Type type) {
        Observable<CacheResult<T>> cache = loadCache(apiCache, cacheKey, type);
        cache.onErrorReturn(new Function<Throwable, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(Throwable throwable) throws Exception {
                return null;
            }
        });
        Observable<CacheResult<T>> remote = loadRemote(apiCache, cacheKey, source);
        return Observable.concat(remote, cache).filter(new Predicate<CacheResult<T>>() {
            @Override
            public boolean test(CacheResult<T> tCacheResult) throws Exception {
                return tCacheResult != null && tCacheResult.getCacheData() != null;
            }
        }).firstElement().toObservable();
    }
}
