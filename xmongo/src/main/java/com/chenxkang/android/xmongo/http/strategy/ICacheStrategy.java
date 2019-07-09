package com.chenxkang.android.xmongo.http.strategy;


import com.chenxkang.android.xmongo.cache.ApiCache;
import com.chenxkang.android.xmongo.cache.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 缓存策略接口
 */

public interface ICacheStrategy<T> {

    <T> Observable<CacheResult<T>> execute(ApiCache apiCache, String cacheKey, Observable<T> source, Type type);

}
