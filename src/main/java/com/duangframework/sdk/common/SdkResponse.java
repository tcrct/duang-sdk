package com.duangframework.sdk.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laotang on 2019/1/1.
 */
public class SdkResponse {

    private int status;
    private String result;
    private Map<String, String> headers = new HashMap<String, String>();

    public SdkResponse() {
    }

    public SdkResponse(int status, String result, Map<String, String> headers) {
        this.status = status;
        this.result = result;
        this.headers = headers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
