package com.duangframework.sdk.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laotang on 2018/12/31.
 */
public class ClientRequestBaseDto {

    private Map<String, String> $duang$_headerMap = new HashMap<String, String>();

    public Map<String, String> getHeaderMap() {
        return $duang$_headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.$duang$_headerMap = headerMap;
    }
}
