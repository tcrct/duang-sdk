package com.duangframework.sdk.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.duangframework.sdk.annon.ApiParam;
import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.utils.DtoValueFilter;
import com.duangframework.sdk.utils.DuangId;
import com.duangframework.sdk.utils.SdkUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by laotang on 2018/12/31.
 */
public abstract class AbstractClientRequest implements SdkRequest {

    private String requestId ;
    protected String requestApi;
    protected BaseDto baseDto;
    protected boolean isRestfulApi = false;
    private Map<String, String> headerMap = new HashMap<String, String>();


    public AbstractClientRequest(BaseDto baseDto) {
        this(new DuangId().toString(), baseDto);
    }

    public AbstractClientRequest(String requestId,  BaseDto baseDto) {
        this(requestId, new HashMap<String, String>(), baseDto);
    }

    public AbstractClientRequest(Map<String,String> headerMap, BaseDto baseDto) {
        this(new DuangId().toString(), headerMap, baseDto);
    }

    public AbstractClientRequest(String requestId, Map<String,String> headerMap, BaseDto baseDto) {
        this.requestId = requestId;
        if(null != headerMap &&!headerMap.isEmpty()) {
            this.headerMap.putAll(headerMap);
        }
        this.baseDto = baseDto;
    }

    public BaseDto getBaseDto() {
        return baseDto;
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
        return Constant.CONTENT_TYPE_JSON;
    }

    protected void setRequestApi(String requestApi, BaseDto baseDto) {
        boolean isRestful = isRestful();
        if(!isRestful) {
            isRestful = requestApi.indexOf("{") > -1 && requestApi.indexOf("}") > -1;
        }
        if(isRestful) {
            this.requestApi = buildRestfulRequestApi(requestApi, baseDto);
        } else {
            this.requestApi = requestApi;
        }
    }

    private String buildRestfulRequestApi(String requestApi, BaseDto baseDto) {
        if(null == requestApi ||requestApi.isEmpty()){
            throw new NullPointerException("reqeustApi is null");
        }

        Map<String, Object> convertMap = new HashMap<>();
        DtoValueFilter dtoValueFilter = new DtoValueFilter(convertMap);
        convertMap.putAll(dtoValueFilter.getConvertMap());
        JSONObject.toJSONString(baseDto, dtoValueFilter);
        if(convertMap.isEmpty()) {
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
        String json = SdkUtils.toJsonString(baseDto);
        Map<String, Object> paramsMap = new HashMap<>();
        if(null != json && !json.isEmpty()) {
            paramsMap = SdkUtils.jsonParseObject(json, Map.class);
        }
        return paramsMap;
    }



}
