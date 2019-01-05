package com.duangframework.sdk;

import com.duangframework.sdk.common.AbstractClientRequest;
import com.duangframework.sdk.common.HttpHeaderNames;
import com.duangframework.sdk.common.SdkClient;
import com.duangframework.sdk.utils.DuangId;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class SignetClient  extends SdkClient {

    public SignetClient(String endpoint, String appKey, String appSecret) {
        super(endpoint, appKey, appSecret);
    }

    public <T> T execute(AbstractClientRequest request) {
        // 设置请求头信息
        request.setHeaderMap(builderHeader(request));
        // 执行请求(同步)
        return (T)super.execute(request);
    }

    /**
     * 添加公共header头信息
     * @return
     */
    private Map<String, String> builderHeader(AbstractClientRequest request) {
        Map<String, String> headers = request.getHeaderMap();
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(HttpHeaderNames.AUTHORIZATION, new DuangId().toString());
        headerMap.put(HttpHeaderNames.DATE, Long.toString(System.currentTimeMillis()));
        headerMap.put(HttpHeaderNames.USER_AGENT, getConfiguration().DEFAULT_USER_AGENT);
        headerMap.put(HttpHeaderNames.CONTENT_TYPE, request.getContentType());
        if(null != headers && !headers.isEmpty()) {
            for(Iterator<Map.Entry<String,String>> iterator = headers.entrySet().iterator(); iterator.hasNext();){
                Map.Entry<String,String> entry = iterator.next();
                String value = entry.getValue();
                if(null != value && !value.isEmpty()) {
                    headerMap.put(entry.getKey(), value);
                }
            }
        }
        return headerMap;
    }
}
