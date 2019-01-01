package com.duangframework.sdk.utils;

import com.duangframework.sdk.security.EncryptDto;
/**
 * Created by laotang on 2019/1/1.
 */
public class SignUtils {

    public static String sign(EncryptDto encryptDto, String secret) {
        // 进行签名
        Sha256Algorithm algorithm = new Sha256Algorithm();
        return algorithm.encrypt(secret, encryptDto);
    }


}
