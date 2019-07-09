package com.chenxkang.android.xmongo.util;


import com.google.gson.Gson;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : Gson 相关工具
 */

public class GsonUtil {

    private static Gson gson;

    public static Gson gson(){
        if (gson == null){
            synchronized (Gson.class){
                if (gson == null){
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}
