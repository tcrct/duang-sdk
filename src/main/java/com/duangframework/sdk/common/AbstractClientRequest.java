package com.duangframework.sdk.common;

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
    protected ClientRequestBaseDto requestBaseDto;
    protected boolean isRestfulApi = false;

    public AbstractClientRequest(ClientRequestBaseDto requestBaseDto) {
        this.requestId = new DuangId().toString();
        this.requestBaseDto = requestBaseDto;
    }

    protected void setRequestApi(String requestApi, ClientRequestBaseDto clientDto) {
        if(isRestful()) {
            buildRestfulRequestApi(requestApi, clientDto);
        } else {
            this.requestApi = requestApi;
        }
    }

    private void buildRestfulRequestApi(String requestApi, ClientRequestBaseDto clentDto) {

    }


    public boolean isRestful() {
        return false;
    }


    public void setRequestDto(ClientRequestBaseDto dto){
        this.requestBaseDto = dto;
    }


    @Override
    public Map<String, Object> getParamMap() {
        Class<?> requestBaseDtoClass = requestBaseDto.getClass();
        Field[] fields = requestBaseDtoClass.getFields();
        Map<String,Object> paramsMap = new HashMap<String, Object>();
        if(null == fields || fields.length <= 0) {
            return paramsMap;
        }
        for(Field field : fields) {
            if(field.getName().startsWith(ClientConfiguration.DUANG_FIELD_PREFIX)) {
                continue;
            }
            Object value = SdkUtils.getFieldValue(requestBaseDto, field);
            if(null != value) {
                paramsMap.put(field.getName(), value);
            }
        }
        return paramsMap;
    }

    @Override
    public Map<String, String> getHeaderMap() {
        Map<String,String> defaultHeadersMap = ClientConfiguration.DEFAULT_HEADERS;
        Map<String, String> headerMap = requestBaseDto.getHeaderMap();
        for(Iterator<String> it = defaultHeadersMap.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            if(!headerMap.containsKey(key)) {
                headerMap.put(key, defaultHeadersMap.get(key));
            }
        }
        return headerMap;
    }
}
