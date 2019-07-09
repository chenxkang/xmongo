package com.chenxkang.android.xmongo.util;


/**
 * author: chenxkang
 * time  : 17/9/26
 * desc  :
 */

public enum CipherType {

    SHA("SHA"),
    MD5("MD5"),
    DES("DES"),
    RSA("RSA");

    private String type;

    CipherType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
