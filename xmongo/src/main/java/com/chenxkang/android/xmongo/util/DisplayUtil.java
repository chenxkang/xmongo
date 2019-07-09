package com.chenxkang.android.xmongo.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  :
 */

public class DisplayUtil {

    /**
     * 获取 显示信息
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics dm = Utils.getAppContext().getResources().getDisplayMetrics();
        return dm;
    }

    public static int dp2px(float dpValue) {
        float scale = getDisplayMetrics(Utils.getAppContext()).density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(final float spValue) {
        final float fontScale = getDisplayMetrics(Utils.getAppContext()).scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @SuppressLint("PrivateApi")
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x, sBar;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sBar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            sBar = 20;
        }

        return sBar;
    }
}
