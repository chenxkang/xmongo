package com.chenxkang.android.xmongo.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * author: chenxkang
 * time  : 2018/3/14
 * desc  : SharedPreferences相关工具类
 */

public class SpUtil {

    private static final String SHARED_NAME = "SHARED_DATA";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_NAME, Activity.MODE_PRIVATE);
    }

    public static void saveString(String key, String value) {
        saveString(Utils.getAppContext(), key, value);
    }

    public static void saveString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getString(Utils.getAppContext(), key);
    }

    public static String getString(String key, String defValue) {
        return getString(Utils.getAppContext(), key, defValue);
    }

    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public static String getString(Context context, String key, String defValue) {
        return getSharedPreferences(context).getString(key, defValue);
    }

    public static void saveInt(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSharedPreferences(context).getInt(key, defValue);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static void saveFloat(Context context, String key, float value) {
        getSharedPreferences(context).edit().putFloat(key, value).apply();
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getSharedPreferences(context).getFloat(key, defValue);
    }

    public static void saveLong(Context context, String key, long value) {
        getSharedPreferences(context).edit().putLong(key, value).apply();
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSharedPreferences(context).getLong(key, defValue);
    }
}
