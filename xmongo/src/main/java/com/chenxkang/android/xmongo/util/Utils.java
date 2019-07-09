package com.chenxkang.android.xmongo.util;

import android.content.Context;

/**
 * author: chenxkang
 * time  : 2018/3/14
 * desc  : 工具类，统一管理上下文环境，一定要在Application中初始化
 */

public class Utils {

     private static Context context;

     public Utils(){
         throw new UnsupportedOperationException("U can't instantiate me.");
     }

     public static void init(Context context){
         Utils.context = context;
     }

     public static Context getAppContext(){
         if (context != null)
             return context;

         throw new NullPointerException("Utils should init first");
     }

}
