package com.chenxkang.android.xmongo;

import android.content.Context;
import android.widget.Toast;

import com.chenxkang.android.xmongo.cache.ApiCache;
import com.chenxkang.android.xmongo.http.HttpConfig;
import com.chenxkang.android.xmongo.http.api.ApiManager;
import com.chenxkang.android.xmongo.http.callback.UICallback;
import com.chenxkang.android.xmongo.http.request.BaseHttpRequest;
import com.chenxkang.android.xmongo.http.request.DeleteRequest;
import com.chenxkang.android.xmongo.http.request.GetRequest;
import com.chenxkang.android.xmongo.http.request.HeadRequest;
import com.chenxkang.android.xmongo.http.request.OptionsRequest;
import com.chenxkang.android.xmongo.http.request.PatchRequest;
import com.chenxkang.android.xmongo.http.request.PostRequest;
import com.chenxkang.android.xmongo.http.request.PutRequest;
import com.chenxkang.android.xmongo.http.request.RetrofitRequest;
import com.chenxkang.android.xmongo.http.request.UploadRequest;
import com.chenxkang.android.xmongo.util.NetWork;

import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * author: chenxkang
 * time  : 17/9/22
 * desc  : Mongoose snake 愣头青獴哥
 */

public class MongoHttp {

    private static Context context;
    private static OkHttpClient.Builder okHttpBuilder;
    private static Retrofit.Builder retrofitBuilder;
    private static ApiCache.Builder apiCacheBuilder;
    private static OkHttpClient okHttpClient;
    private static ApiCache apiCache;

    private static final HttpConfig NET_GLOBAL_CONFIG = HttpConfig.getInstance();

    public static HttpConfig CONFIG() {
        return NET_GLOBAL_CONFIG;
    }

    public static void init(Context appContext) {
        if (context == null && appContext != null) {
            context = appContext.getApplicationContext();
            okHttpBuilder = new OkHttpClient.Builder();
            retrofitBuilder = new Retrofit.Builder();
            apiCacheBuilder = new ApiCache.Builder(context);
        }
    }

    public static Context getContext() {
        if (context == null) {
            throw new IllegalStateException("Please call ViseHttp.init(this) in Application to initialize!");
        }
        return context;
    }

    public static OkHttpClient.Builder getOkHttpBuilder() {
        if (okHttpBuilder == null) {
            throw new IllegalStateException("Please call ViseHttp.init(this) in Application to initialize!");
        }
        return okHttpBuilder;
    }

    public static Retrofit.Builder getRetrofitBuilder() {
        if (retrofitBuilder == null) {
            throw new IllegalStateException("Please call ViseHttp.init(this) in Application to initialize!");
        }
        return retrofitBuilder;
    }

    public static ApiCache.Builder getApiCacheBuilder() {
        if (apiCacheBuilder == null) {
            throw new IllegalStateException("Please call ViseHttp.init(this) in Application to initialize!");
        }
        return apiCacheBuilder;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = getOkHttpBuilder().build();
        }
        return okHttpClient;
    }

    public static ApiCache getApiCache() {
        if (apiCache == null || apiCache.isClosed()) {
            apiCache = getApiCacheBuilder().build();
        }
        return apiCache;
    }

    /**
     * 通用请求，可传入自定义请求
     * @param request
     * @return
     */
    public static BaseHttpRequest BASE(BaseHttpRequest request) {
        if (request != null) {
            return request;
        } else {
            return new GetRequest("");
        }
    }

    /**
     * 可传入自定义Retrofit接口服务的请求类型
     * @return
     */
    public static <T> RetrofitRequest RETROFIT() {
        return new RetrofitRequest();
    }

    /**
     * GET请求
     * @param suffixUrl
     * @return
     */
    public static GetRequest GET(String suffixUrl) {
        if (!NetWork.isAvailable(context))
            Toast.makeText(context, "当前网络不可用，请检查后再重试", Toast.LENGTH_SHORT).show();

        return new GetRequest(suffixUrl);
    }

    /**
     * POST请求
     * @param suffixUrl
     * @return
     */
    public static PostRequest POST(String suffixUrl) {
        if (!NetWork.isAvailable(context))
            Toast.makeText(context, "当前网络不可用，请检查后再重试", Toast.LENGTH_SHORT).show();

        return new PostRequest(suffixUrl);
    }

    /**
     * HEAD请求
     * @param suffixUrl
     * @return
     */
    public static HeadRequest HEAD(String suffixUrl) {
        return new HeadRequest(suffixUrl);
    }

    /**
     * PUT请求
     * @param suffixUrl
     * @return
     */
    public static PutRequest PUT(String suffixUrl) {
        return new PutRequest(suffixUrl);
    }

    /**
     * PATCH请求
     * @param suffixUrl
     * @return
     */
    public static PatchRequest PATCH(String suffixUrl) {
        return new PatchRequest(suffixUrl);
    }

    /**
     * OPTIONS请求
     * @param suffixUrl
     * @return
     */
    public static OptionsRequest OPTIONS(String suffixUrl) {
        return new OptionsRequest(suffixUrl);
    }

    /**
     * DELETE请求
     * @param suffixUrl
     * @return
     */
    public static DeleteRequest DELETE(String suffixUrl) {
        return new DeleteRequest(suffixUrl);
    }

    /**
     * 上传
     * @param suffixUrl
     * @return
     */
    public static UploadRequest UPLOAD(String suffixUrl) {
        return new UploadRequest(suffixUrl);
    }

    /**
     * 上传（包含上传进度回调）
     * @param suffixUrl
     * @return
     */
    public static UploadRequest UPLOAD(String suffixUrl, UICallback uCallback) {
        return new UploadRequest(suffixUrl, uCallback);
    }

    /**
     * 添加请求订阅者
     * @param tag
     * @param disposable
     */
    public static void addDisposable(Object tag, Disposable disposable) {
        ApiManager.getInstance().add(tag, disposable);
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(Object tag) {
        ApiManager.getInstance().cancel(tag);
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll() {
        ApiManager.getInstance().cancelAll();
    }

    /**
     * 清除对应Key的缓存
     * @param key
     */
    public static void removeCache(String key) {
        getApiCache().remove(key);
    }

    /**
     * 清楚所有缓存并关闭缓存
     * @return
     */
    public static Disposable clearCache() {
        return getApiCache().clear();
    }

}
