package com.duangframework.sdk.common;

/**
 * Created by laotang on 2018/12/31.
 */
public class SdkDto implements java.io.Serializable {

    private String tokenid;

    public SdkDto(String tokenid) {
        this.tokenid = tokenid;
    }

    public SdkDto() {
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }
}
