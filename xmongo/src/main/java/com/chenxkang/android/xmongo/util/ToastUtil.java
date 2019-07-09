package com.chenxkang.android.xmongo.util;

import android.annotation.SuppressLint;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * author: chenxkang
 * time  : 2018/3/14
 * desc  : 吐司相关工具类
 */

public class ToastUtil {

    private ToastUtil() {
        throw new UnsupportedOperationException("U can't do this.");
    }

    /**
     * 显示短吐司
     *
     * @param resId 显示资源ID
     */
    public static void showShortToast(@StringRes int resId) {
        showShortToast(Utils.getAppContext().getResources().getText(resId));
    }

    /**
     * 显示短吐司
     *
     * @param text 显示内容
     */
    public static void showShortToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长吐司
     *
     * @param resId 显示资源ID
     */
    public static void showLongToast(@StringRes int resId) {
        showLongToast(Utils.getAppContext().getResources().getText(resId));
    }

    /**
     * 显示长吐司
     *
     * @param text 显示内容
     */
    public static void showLongToast(CharSequence text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示吐司
     *
     * @param resId    显示资源ID
     * @param duration 显示时长
     */
    private static void showToast(@StringRes int resId, int duration) {
        showToast(Utils.getAppContext().getResources().getText(resId), duration);
    }

    /**
     * 显示吐司
     *
     * @param text     显示内容
     * @param duration 显示时长
     */
    @SuppressLint("ShowToast")
    private static void showToast(CharSequence text, int duration) {
        if (!AppUtil.isRunningForeground(Utils.getAppContext()) || TextUtils.isEmpty(text))
            return;

        Toast.makeText(Utils.getAppContext(), text, duration).show();
    }
}
