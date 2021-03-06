package com.chenxkang.android.xmongo.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  :
 */

public class AppUtil {

    public static final String SEMICOLON = ";";
    public static final String SourceType = "Android";

    public static void relaunchApp() {
        PackageManager packageManager = Utils.getAppContext().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(Utils.getAppContext().getPackageName());
        if (intent == null) return;
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        Utils.getAppContext().startActivity(mainIntent);
        System.exit(0);
    }

    /**
     * 调用系统分享
     */
    public static void shareToOtherApp(Context context, String title, String content, String dialogTitle) {
        Intent intentItem = new Intent(Intent.ACTION_SEND);
        intentItem.setType("text/plain");
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
        intentItem.putExtra(Intent.EXTRA_TEXT, content);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intentItem, dialogTitle));
    }

    /**
     * 调用系统分享，指定分享
     */
    public static void shareToTargetApp(Context context, String packageName, String title, String content) {
        Intent intentItem = new Intent(Intent.ACTION_SEND);
        intentItem.setType("text/plain");
        intentItem.setPackage(packageName);
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
        intentItem.putExtra(Intent.EXTRA_TEXT, content);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentItem);
    }

    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取App包 信息版本号
     *
     * @param context
     * @return
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 判断APK包是否已经安装
     *
     * @param context     上下文，一般为Activity
     * @param packageName 包名
     * @return 包存在则返回true，否则返回false
     */
    public static boolean isPackageExists(Context context, String packageName) {
        if (null == packageName || "".equals(packageName)) {
            throw new IllegalArgumentException("Package name cannot be null or empty !");
        }
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return null != info;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * @return 当前程序的版本名称
     */
    public static String getVersionName(Context context) {
        String version;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            version = "";
        }
        return version;
    }

    /**
     * 方法: getVersionCode
     * 描述: 获取客户端版本号
     *
     * @return int    版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            versionCode = 999;
        }
        return versionCode;
    }

    public static int getVersionCode(Context context, String packageName) {
        int versionCode;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            versionCode = 999;
        }
        return versionCode;
    }

    /**
     * 获取进程名字
     *
     * @param cxt
     * @param pid
     * @return
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * 获取userAgent
     *
     * @param context 上下文信息
     * @return 系统相关的信息
     */
    public static String getUserAgent(Context context, String appId) {
        StringBuffer userAgent = new StringBuffer();
        // ==============================================================
        // User-Agent
        // 格式：
        // 应用名称;应用版本;平台;OS版本;OS版本名称;厂商;机型;分辨率(宽*高);安装渠道;网络;
        // 示例：
        // HET;2.2.0;Android;4.2.2;N7100XXUEMI6BYTuifei;samsung;GT-I9300;480*800;360;WIFI;
        userAgent.append(appId);// 应用名称
        userAgent.append(SEMICOLON);
        userAgent.append(AppUtil.getVersionName(context)); // App版本
        userAgent.append(SEMICOLON);
        userAgent.append(SourceType);// 平台
        userAgent.append(SEMICOLON);
        userAgent.append(AppUtil.getOSVersionName()); // OS版本
        userAgent.append(SEMICOLON);
        userAgent.append(AppUtil.getOSVersionDisplayName()); // OS显示版本
        userAgent.append(SEMICOLON);
        userAgent.append(AppUtil.getBrandName()); // 品牌厂商
        userAgent.append(SEMICOLON);
        userAgent.append(AppUtil.getModelName()); // 设备
        userAgent.append(SEMICOLON);
        userAgent.append(ViewUtil.getScreenWidth(context) + "*" + ViewUtil.getScreenHeight(context)); // 分辨率
        userAgent.append(SEMICOLON);
        userAgent.append(AppUtil.getImei(context)); // IMEI
        userAgent.append(SEMICOLON);
        userAgent.append(AppUtil.getNetType(context)); // 网络类型
        userAgent.append(SEMICOLON);
        return userAgent.toString();
    }

    @SuppressLint("MissingPermission")
    private static String getImei(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
    }

    /**
     * 获取渠道，用于打包
     *
     * @param context
     * @param metaName
     * @return
     */
    public static String getAppSource(Context context, String metaName) {
        String result = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                result = appInfo.metaData.getString(metaName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static String getNetType(final Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        // networkInfo.getDetailedState();//获取详细状态。
        // networkInfo.getExtraInfo();//获取附加信息。
        // networkInfo.getReason();//获取连接失败的原因。
        // networkInfo.getType();//获取网络类型(一般为移动或Wi-Fi)。
        // networkInfo.getTypeName();//获取网络类型名称(一般取值“WIFI”或“MOBILE”)。
        // networkInfo.isAvailable();//判断该网络是否可用。
        // networkInfo.isConnected();//判断是否已经连接。
        // networkInfo.isConnectedOrConnecting();//：判断是否已经连接或正在连接。
        // networkInfo.isFailover();//：判断是否连接失败。
        // networkInfo.isRoaming();//：判断是否漫游
        return networkInfo.getTypeName();
    }

    /**
     * 获取设备制造商名称.
     *
     * @return 设备制造商名称
     */
    public static String getManufacturerName() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取设备名称.
     *
     * @return 设备名称
     */
    public static String getModelName() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取设备名称.
     *
     * @return 设备名称
     */
    public static String getModelId(){
        return Settings.System.getString(Utils.getAppContext().getContentResolver(), Settings.System.ANDROID_ID);
    }

    /**
     * 获取产品名称.
     *
     * @return 产品名称
     */
    public static String getProductName() {
        return android.os.Build.PRODUCT;
    }

    /**
     * 获取品牌名称.
     *
     * @return 品牌名称
     */
    public static String getBrandName() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取操作系统版本号.
     *
     * @return 操作系统版本号
     */
    public static int getOSVersionCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取操作系统版本名.
     *
     * @return 操作系统版本名
     */
    public static String getOSVersionName() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取操作系统版本显示名.
     *
     * @return 操作系统版本显示名
     */
    public static String getOSVersionDisplayName() {
        return android.os.Build.DISPLAY;
    }

    /**
     * 获取主机地址.
     *
     * @return 主机地址
     */
    public static String getHost() {
        return android.os.Build.HOST;
    }

    /**
     * 获取SHA1
     *
     * @param context 上下文环境
     * @return
     */
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
