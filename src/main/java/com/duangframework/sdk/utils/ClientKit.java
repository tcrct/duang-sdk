package com.duangframework.sdk.utils;

import com.duangframework.sdk.common.*;
import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.enums.ContentType;
import com.duangframework.sdk.enums.HttpMethod;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by laotang on 2019/1/6.
 */
public class ClientKit {

    private static final ClientKit clientKit = new ClientKit();
    private static SdkClient sdkClient;
    private String token;
    private String uri;
    private SdkDto sdkDto;
    private Map<String,String> headers;
    private HttpMethod httpMethod;
    private ContentType contentType;
    private boolean isRestfulApi;

    private ClientKit() {
        sdkClient = new SdkClient(SdkUtils.getEndPoint(), SdkUtils.getAppKey(), SdkUtils.getAppSecret());
    }

    public static ClientKit duang() {
        return clientKit;
    }

    public ClientKit token(String token){
        this.token = token;
        return this;
    }

    public ClientKit uri(String uri){
        this.uri = uri;
        return this;
    }

    public ClientKit headers(Map<String,String> headers) {
        this.headers = headers;
        return this;
    }

    public ClientKit params(SdkDto dto) {
        this.sdkDto = dto;
        return this;
    }

    public ClientKit method(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public ClientKit conetntType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public ClientKit restful(boolean isRestful) {
        this.isRestfulApi = isRestful;
        return this;
    }

    public SdkResponse send() {
        SdkResponse response = sdkClient.execute(new AbstractSdkRequest(token, headers, sdkDto){
            @Override
            public String getRequestApi() {
                if(null == uri) {
                    try {
                        Field field = sdkDto.getClass().getDeclaredField(Constant.REQUEST_API);
                        uri = SdkUtils.getFieldValue(sdkDto, field)+"";
                    } catch (Exception e) {}
                }
                setRequestApi(uri, sdkDto);
                return requestApi;
            }
            @Override
            public HttpMethod getMethod() {
                return (null == httpMethod) ? HttpMethod.POST : httpMethod;
            }
            @Override
            public String getContentType() {
                return (null == contentType) ? ContentType.JSON.getValue() : contentType.getValue();
            }
            @Override
            public boolean isRestful() {
                return isRestfulApi;
            }
        });

        return response;
    }




}
