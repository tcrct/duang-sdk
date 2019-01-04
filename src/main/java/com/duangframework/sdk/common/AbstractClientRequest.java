package com.duangframework.sdk.common;

import com.duangframework.sdk.annon.ApiParam;
import com.duangframework.sdk.utils.DuangId;
import com.duangframework.sdk.utils.SdkUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
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
        Field[] fields = baseDto.getClass().getDeclaredFields();
        if(null == fields || fields.length == 0) return requestApi;
        for(Field field : fields) {
            ApiParam apiParamAnnon = field.getAnnotation(ApiParam.class);
            if(null != apiParamAnnon) {
                Object value = SdkUtils.getFieldValue(baseDto, field);
                String key = (null != apiParamAnnon.name()) ? "{"+ apiParamAnnon.name() +"}": "{"+field.getName()+"}";
                if(null != value) {
                    requestApi = requestApi.replace(key.trim(),  SdkUtils.convertObjectToString(value, ","));
                } else {
                    requestApi = requestApi.replace(key.trim(), "");
                }
            }
        }
        return requestApi;
    }


    public boolean isRestful() {
        return isRestfulApi;
    }

    @Override
    public Map<String, Object> getParamMap() {
        Class<?> requestBaseDtoClass = baseDto.getClass();
        Field[] fields = requestBaseDtoClass.getDeclaredFields();
        Map<String,Object> paramsMap = new HashMap<String, Object>();
        if(null == fields || fields.length <= 0) {
            return paramsMap;
        }
        for(Field field : fields) {
            if(field.getName().startsWith(ClientConfiguration.DUANG_FIELD_PREFIX)) {
                continue;
            }
            Object value = SdkUtils.getFieldValue(baseDto, field);
            if(null != value) {
                paramsMap.put(field.getName(), value);
            }
        }
        return paramsMap;
    }
}
