package com.chenxkang.android.xmongo.http.func;

import android.support.annotation.NonNull;

import com.chenxkang.android.xmongo.util.GsonUtil;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : ResponseBody转T
 */

public class ApiFunc<T> implements Function<ResponseBody, T> {

    private Type type;

    public ApiFunc(Type type) {
        this.type = type;
    }

    @Override
    public T apply(@NonNull ResponseBody responseBody) throws Exception {
        String json;
        try {
            json = responseBody.string();
            if (type.equals(String.class)) {
                return (T) json;
            } else {
                return GsonUtil.gson().fromJson(json, type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            responseBody.close();
        }
        return null;
    }
}
