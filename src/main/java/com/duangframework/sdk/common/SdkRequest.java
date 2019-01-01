package com.duangframework.sdk.common;

import java.util.Map;

/**
 * Created by laotang on 2019/1/1.
 */
public interface SdkRequest {

    boolean isRestful();
    String getRequestApi();
    HttpMethod getMethod();
    Map<String,Object> getParamMap();
    Map<String,String> getHeaderMap();
}
