package com.chenxkang.android.xmongo.http.api;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 请求管理
 */

public class ApiManager {

    private static ApiManager instance;
    private ConcurrentHashMap<Object, Disposable> arrayMaps;

    /**
     * 获取请求管理实例，双重校验锁
     * @return
     */
    public static ApiManager getInstance(){
        if (instance == null){
            synchronized (ApiManager.class){
                if (instance == null){
                    instance = new ApiManager();
                }
            }
        }
        return instance;
    }

    private ApiManager(){
        arrayMaps = new ConcurrentHashMap<>();
    }

    public void add(Object tag, Disposable disposable){
        arrayMaps.put(tag, disposable);
    }

    public void remove(Object tag){
        if (!arrayMaps.isEmpty()){
            arrayMaps.remove(tag);
        }
    }

    public void removeAll(){
        if (!arrayMaps.isEmpty()){
            arrayMaps.clear();
        }
    }

    public void cancel(Object tag){
        if (arrayMaps.isEmpty())
            return;

        if (arrayMaps.get(tag) == null)
            return;

        if (!arrayMaps.get(tag).isDisposed()){
            arrayMaps.get(tag).dispose();
            arrayMaps.remove(tag);
        }
    }

    public void cancelAll(){
        if (arrayMaps.isEmpty())
            return;

        Set<Object> keys = arrayMaps.keySet();
        for (Object apiKey : keys){
            cancel(apiKey);
        }
    }
}
