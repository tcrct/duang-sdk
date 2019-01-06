package com.duangframework.sdk.common;

import java.util.Map;

/**
 * Created by laotang on 2019/1/6.
 */
public class KvModle {

    private Map<String,Object> restfulApiMap;
    private Map<String,Object> dtoMap;

    public KvModle(Map<String, Object> restfulApiMap, Map<String, Object> dtoMap) {
        this.restfulApiMap = restfulApiMap;
        this.dtoMap = dtoMap;
    }

    public Map<String, Object> getRestfulApiMap() {
        return restfulApiMap;
    }

    public Map<String, Object> getDtoMap() {
        return dtoMap;
    }
}
