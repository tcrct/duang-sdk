package com.duangframework.sdk.security;

import com.duangframework.sdk.utils.SdkUtils;

import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

/**
 * 加密Dto
 */
public class EncryptDto implements java.io.Serializable {

    /**
     * 头部信息
     */
    private Map<String, String> headers = new TreeMap<String,String>();
    /**
     * 参数信息
     */
    private Map<String, Object> params = new TreeMap<String,Object>();
    /**
     * 请求URI
     */
    private String uri;
    /**
     *  时间戳
     */
    private String timestamp;
    /**
     * 随机字符串
     */
    private String nonce;


    public EncryptDto(String uri) {
        this.uri = uri;
    }

    public EncryptDto(String uri, Map<String, String> headers, Map<String, Object> params) {
        this.headers = headers;
        this.params = params;
        this.uri = uri;
        this.timestamp = System.currentTimeMillis()+"";
        this.nonce = SdkUtils.getRandomStr();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
