package com.duangframework.sdk.common;

import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.enums.ContentType;
import com.duangframework.sdk.exception.SdkException;
import com.duangframework.sdk.utils.DuangId;
import com.duangframework.sdk.utils.SdkUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author laotang
 * @date 2018/12/31.
 */
public abstract class AbstractSdkRequest implements SdkRequest {

    private String token ;
    protected String requestApi;
    protected SdkDto sdkDto;
    protected boolean isRestfulApi = false;
    protected Map<String, String> headerMap = new HashMap<String, String>();
    protected Map<String, Object> paramsMap = new HashMap<String,Object>();
    private KvModle kvModle;
    protected String callbackUrl;

    public AbstractSdkRequest(){

    }

    public AbstractSdkRequest(Object object) {
        this("", object);
    }

    public AbstractSdkRequest(String token, Object object) {
        this(token, new HashMap<String, String>(), object);
    }

    public AbstractSdkRequest(Map<String,String> headerMap, Object object) {
        this("", headerMap, object);
    }

    public AbstractSdkRequest(String token, Map<String,String> headerMap, Object object) {
        this.token = (null ==token || token.isEmpty()) ? new DuangId().toString() : token;
        if(null != headerMap &&!headerMap.isEmpty()) {
            this.headerMap.putAll(headerMap);
        }
        if(object instanceof  SdkDto) {
            this.sdkDto = (SdkDto) object;
            this.kvModle = SdkUtils.dto2KvModle(sdkDto);
        } else if(object instanceof List) {

        }

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

    protected void setRequestApi(String requestApi, Map<String,Object> convertMap) {
        boolean isRestful = isRestful();
        if(!isRestful) {
            isRestful = requestApi.indexOf("{") > -1 && requestApi.indexOf("}") > -1;
        }
        if(isRestful) {
            this.requestApi = buildRestfulRequestApi(requestApi, convertMap);
        } else {
            this.requestApi = requestApi;
        }
    }

    protected void setRequestApi(String requestApi, SdkDto sdkDto) {
        Map<String, Object> convertMap = kvModle.getRestfulApiMap();
        setRequestApi(requestApi, convertMap);
    }

    private String buildRestfulRequestApi(String requestApi, Map<String, Object> convertMap) {
        if(null == requestApi ||requestApi.isEmpty()){
            throw new NullPointerException("reqeustApi is null");
        }
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
        if(paramsMap.isEmpty()) {
            return kvModle.getDtoMap();
        } else {
            return paramsMap;
        }
    }
}
