package com.chenxkang.android.xmongo.util;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * author: chenxkang
 * time  : 2018/5/28
 * desc  : 时间相关工具
 */

public class TimeUtil {

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getWebsiteDateTime() {
        String time = getCurrentTime();
        try {
            URL url = new URL("http://www.ntsc.ac.cn");// 中国科学院国家授时中心
            URLConnection uc = url.openConnection();
            uc.connect();
            long ld = uc.getDate();
            Date date = new Date(ld);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            time = sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getCurrentTime() {
        return millis2String(System.currentTimeMillis(), DEFAULT_FORMAT);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayDate(String pattern) {
        return millis2String(System.currentTimeMillis(), new SimpleDateFormat(pattern));
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isToday(String time, String pattern) {
        return isToday(String2Millis(time, new SimpleDateFormat(pattern)));
    }

    private static boolean isToday(long millis) {
        long wee = getWeeOfToday();
        return millis >= wee && millis < wee + 86400000;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isYesterday(String time, String pattern) {
        return isYesterday(String2Millis(time, new SimpleDateFormat(pattern)));
    }

    private static boolean isYesterday(long millis) {
        long wee = getWeeOfToday();
        return millis >= wee - 86400000 && millis < wee;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isTheDayBeforeYesterday(String time, String pattern) {
        return isTheDayBeforeYesterday(String2Millis(time, new SimpleDateFormat(pattern)));
    }

    private static boolean isTheDayBeforeYesterday(long millis) {
        long wee = getWeeOfToday();
        return millis >= wee - 86400000 * 2 && millis < wee - 86400000;
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private static long String2Millis(String time, SimpleDateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static String millis2String(long millis, DateFormat format) {
        return format.format(new Date(millis));
    }


}
