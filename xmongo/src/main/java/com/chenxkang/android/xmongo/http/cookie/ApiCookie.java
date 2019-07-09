package com.chenxkang.android.xmongo.http.cookie;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : Cookie
 */

public class ApiCookie implements CookieJar {

    private CookiesStore cookiesStore;

    public ApiCookie(Context context) {
        if (cookiesStore == null){
            cookiesStore = new CookiesStore(context);
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies.size() > 0){
            for (Cookie cookie : cookies){
                cookiesStore.add(url, cookie);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookiesStore.get(url);
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }
}
