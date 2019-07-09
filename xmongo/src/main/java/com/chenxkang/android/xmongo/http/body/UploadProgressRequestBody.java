package com.chenxkang.android.xmongo.http.body;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chenxkang.android.xmongo.http.callback.UICallback;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 上传进度实体类
 */

public class UploadProgressRequestBody extends RequestBody {

    private RequestBody requestBody;
    private UICallback callback;
    private long lastTime;

    public UploadProgressRequestBody(RequestBody requestBody, UICallback callback) {
        this.requestBody = requestBody;
        this.callback = callback;
        if (requestBody == null || callback == null) {
            throw new NullPointerException("the requestBody and the callback must not be null.");
        }
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private final class CountingSink extends ForwardingSink {

        // 当前字节长度
        private long currentLenght = 0L;
        // 总字节长度，避免多次调用contentLength()方法
        private long totalLenght = 0L;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            // 增加当前写入的字节数
            currentLenght += byteCount;
            // 记录contentLength()的值，后续不再调用
            if (totalLenght == 0) {
                totalLenght = contentLength();
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 100 || lastTime == 0 || currentLenght == totalLenght) {
                lastTime = currentTime;
                Observable.just(currentLenght).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        callback.onProgress(currentLenght, totalLenght, (100.0f * currentLenght) / totalLenght);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onFail(-1, throwable.getMessage());
                    }
                });
            }
        }
    }
}
