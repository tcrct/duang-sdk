package com.duangframework.sdk.common;

import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.enums.ContentType;
import com.duangframework.sdk.exception.SdkException;
import com.duangframework.sdk.utils.DuangId;
import com.duangframework.sdk.utils.SdkUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by laotang on 2018/12/31.
 */
public abstract class AbstractSdkRequest implements SdkRequest {

    private String token ;
    protected String requestApi;
    protected SdkDto sdkDto;
    protected boolean isRestfulApi = false;
    private Map<String, String> headerMap = new HashMap<String, String>();
    private KvModle kvModle;
    protected String callbackUrl;

    public AbstractSdkRequest(SdkDto sdkDto) {
        this("", sdkDto);
    }

    public AbstractSdkRequest(String token, SdkDto sdkDto) {
        this(token, new HashMap<String, String>(), sdkDto);
    }

    public AbstractSdkRequest(Map<String,String> headerMap, SdkDto sdkDto) {
        this("", headerMap, sdkDto);
    }

    public AbstractSdkRequest(String token, Map<String,String> headerMap, SdkDto sdkDto) {
        this.token = (null ==token || token.isEmpty()) ? new DuangId().toString() : token;
        if(null != headerMap &&!headerMap.isEmpty()) {
            this.headerMap.putAll(headerMap);
        }
        this.sdkDto = sdkDto;
        this.kvModle = SdkUtils.dto2KvModle(sdkDto);
    }

    public SdkDto getSdkDto() {
        return sdkDto;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        if(null != headerMap && !headerMap.isEmpty()) {
            this.headerMap.clear();
            this.headerMap.putAll(headerMap);
        }
    }

    @Override
    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public String getContentType() {
        return ContentType.JSON.getValue();
    }

    protected void setRequestApi(String requestApi, SdkDto sdkDto) {
        boolean isRestful = isRestful();
        if(!isRestful) {
            isRestful = requestApi.indexOf("{") > -1 && requestApi.indexOf("}") > -1;
        }
        if(isRestful) {
            this.requestApi = buildRestfulRequestApi(requestApi, sdkDto);
        } else {
            this.requestApi = requestApi;
        }
    }

    private String buildRestfulRequestApi(String requestApi, SdkDto sdkDto) {
        if(null == requestApi ||requestApi.isEmpty()){
            throw new NullPointerException("reqeustApi is null");
        }
        Map<String, Object> convertMap = kvModle.getRestfulApiMap();
        if(null == convertMap || convertMap.isEmpty()) {
            return requestApi;
        }
        for(Iterator<Map.Entry<String,Object>> iterator = convertMap.entrySet().iterator(); iterator.hasNext();){
            Map.Entry<String,Object> entry = iterator.next();
            String key = "{"+ entry.getKey() +"}";
            requestApi = requestApi.replace(key.trim(),  SdkUtils.convertObjectToString(entry.getValue(), ","));
        }
        return requestApi;
    }


    public boolean isRestful() {
        return isRestfulApi;
    }

    @Override
    public Map<String, Object> getParamMap() {
        return kvModle.getDtoMap();
    }
}
