package com.duangframework.sdk.utils;

import com.duangframework.sdk.exception.SdkException;
import com.duangframework.sdk.security.EncryptDto;
import com.duangframework.sdk.security.algorithm.PKCS7Algorithm;
import com.duangframework.sdk.security.algorithm.SHA1Algorithm;
import com.duangframework.sdk.security.algorithm.Sha256Algorithm;

/**
 * Created by laotang on 2019/1/1.
 */
public class SignUtils {

    public static String signSha256(EncryptDto encryptDto, String secret) {
        // 进行签名
        Sha256Algorithm algorithm = new Sha256Algorithm();
        return algorithm.encrypt(secret, encryptDto);
    }


    /**
     * SHA1方式签名与WX一致
     * @param encryptDto    加密码对象
     * @param key   appkey
     * @param secret 安全码
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param encrypt 签名字符串
     * @return
     */
    public static String signSha1(EncryptDto encryptDto, String key, String secret, String timeStamp, String nonce) {
        String replyMsg = SdkUtils.buildEncryptString(encryptDto);
        try {
            PKCS7Algorithm algorithm = new PKCS7Algorithm(key, secret, secret);
            // 加密
            String encrypt = algorithm.encrypt(nonce, replyMsg);
            return SHA1Algorithm.getSHA1(secret, timeStamp, nonce, encrypt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SdkException(SdkException.ValidateSignatureError);
        }
    }

    /**
     * 参数加密
     * @param encryptDto
     * @param key
     * @param secret
     * @param nonce
     * @return
     */
    public static String encrypt(EncryptDto encryptDto, String key, String secret,  String nonce) {
        String replyMsg = SdkUtils.buildEncryptString(encryptDto);
        PKCS7Algorithm algorithm = new PKCS7Algorithm(key, secret, secret);
        // 加密
        return algorithm.encrypt(nonce, replyMsg);
    }



}
