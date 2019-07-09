package com.chenxkang.android.xmongo.http.exception;

import android.net.ParseException;

import com.chenxkang.android.xmongo.http.api.ApiCode;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  : 请求异常统一管理
 */

public class ApiException extends Exception {

    private final int code;
    private String message;

    public ApiException(Throwable throwable, int code) throws IOException {
        super(throwable);
        this.code = code;
        if (throwable instanceof HttpException){
            this.message = ((HttpException) throwable).response().errorBody().string();
        } else {
            this.message = throwable.getMessage();
        }
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ApiException setMessage(String message){
        this.message = message;
        return this;
    }

    public String getDisplayMessage(){
        return message + "(code: " + code + ")";
    }

    public static ApiException handleException(Throwable e) throws IOException {
        ApiException exception;
        if (e instanceof HttpException){
            exception = new ApiException(e, ApiCode.Request.HTTP_ERROR);
            return exception;
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException){
            exception = new ApiException(e, ApiCode.Request.PARSE_ERROR);
            exception.message = "PARSE_ERROR";
            return exception;
        } else if (e instanceof ConnectException){
            exception = new ApiException(e, ApiCode.Request.NETWORK_ERROR);
            exception.message = "NETWORK_ERROR";
            return exception;
        } else if (e instanceof SSLHandshakeException){
            exception = new ApiException(e, ApiCode.Request.SSL_ERROR);
            exception.message = "SSL_ERROR";
            return exception;
        } else if (e instanceof SocketTimeoutException){
            exception = new ApiException(e, ApiCode.Request.TIMEOUT_ERROR);
            exception.message = "TIMEOUT_ERROR";
            return exception;
        } else {
            exception = new ApiException(e, ApiCode.Request.UNKNOWN);
            exception.message = "UNKNOWN";
            return exception;
        }
    }
}
