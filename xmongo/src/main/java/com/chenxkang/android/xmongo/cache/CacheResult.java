package com.chenxkang.android.xmongo.cache;

import java.io.Serializable;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 设置缓存后的数据返回结果，主要增加是否是缓存数据的区分
 */

public class CacheResult<T> implements Serializable {

    private boolean isCache;
    private T cacheData;

    public CacheResult(boolean isCache, T cacheData) {
        this.isCache = isCache;
        this.cacheData = cacheData;
    }

    public boolean isCache() {
        return isCache;
    }

    public CacheResult setCache(boolean cache) {
        isCache = cache;
        return this;
    }

    public T getCacheData() {
        return cacheData;
    }

    public CacheResult setCacheData(T cacheData) {
        this.cacheData = cacheData;
        return this;
    }

    @Override
    public String toString() {
        return "CacheResult{" +
                "isCache=" + isCache +
                ", cacheData=" + cacheData +
                '}';
    }
}
