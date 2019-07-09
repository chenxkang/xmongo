package android.chenxkang.com.xmongo_android;

import android.app.Application;

import com.chenxkang.android.xmongo.MongoHttp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * author: chenxkang
 * time  : 2019-07-09
 * desc  :
 */
public class MyApplication extends Application {

    public static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initNet();
    }

    /**
     * MongoHttp初始化
     */
    public void initNet() {
        MongoHttp.init(instance);
        MongoHttp.CONFIG()
                .baseUrl("https://www.baidu.com/")
                .readTimeout(30)// 读取超时时间
                .writeTimeout(30)// 写入超时时间
                .connectTimeout(30)// 链接超时时间
                .setHttpCache(false)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return false;
                    }
                });
    }
}
