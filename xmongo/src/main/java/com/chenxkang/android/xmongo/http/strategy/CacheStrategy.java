package com.chenxkang.android.xmongo.http.strategy;


import com.chenxkang.android.xmongo.cache.ApiCache;
import com.chenxkang.android.xmongo.cache.CacheResult;
import com.chenxkang.android.xmongo.util.GsonUtil;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 缓存策略
 */

abstract class CacheStrategy<T> implements ICacheStrategy<T> {

    <T> Observable<CacheResult<T>> loadCache(final ApiCache apiCache, final String key, final Type type) {
        return apiCache.<T>get(key).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return s != null;
            }
        }).map(new Function<String, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(String s) throws Exception {
                T t = GsonUtil.gson().fromJson(s, type);
                return new CacheResult<>(true, t);
            }
        });
    }

    <T> Observable<CacheResult<T>> loadRemote(final ApiCache apiCache, final String key, Observable<T> source) {
        return source.map(new Function<T, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(T t) throws Exception {
                apiCache.put(key, t).subscribeOn(Schedulers.io()).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean status) throws Exception {

                    }
                });
                return new CacheResult<>(false, t);
            }
        });
    }
}
