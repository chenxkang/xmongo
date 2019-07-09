package com.chenxkang.android.xmongo.http.callback;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  :
 */

public interface UICallback {

    void onProgress(long currentLength, long totalLength, float percent);

    void onFail(int errCode, String errMsg);
}
