package com.chenxkang.android.xmongo.cache;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 缓存接口
 */

public interface ICache {

    void put(String key, Object value);

    Object get(String key);

    boolean contains(String key);

    void remove(String key);

    void clear();
}
