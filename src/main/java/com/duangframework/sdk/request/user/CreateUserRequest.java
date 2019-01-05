package com.duangframework.sdk.request.user;

import com.duangframework.sdk.common.AbstractClientRequest;
import com.duangframework.sdk.common.HttpMethod;
import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.dto.user.CreateUserDto;

import java.util.Map;


/**
 *  创建用户请求
 *
 * @author laotang
 * @since 1.0
 * @date 2019-01-04
 *
 */
public class CreateUserRequest extends AbstractClientRequest {

    public CreateUserRequest(CreateUserDto dto) {
        super(dto);
    }

    public CreateUserRequest(Map<String,String> headerMap, CreateUserDto dto) {
        super(headerMap, dto);
    }


    /**
     * 请求的API地址
     */
    private static final String API = "/signet-api/Account/create/{username}/{context_bady}";

    @Override
    public String getRequestApi() {
        setRequestApi(API, baseDto);
        return requestApi;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }


    @Override
    public String getContentType() {
        return Constant.CONTENT_TYPE_JSON;
    }

    @Override
    public boolean isRestful() {
        return true;
    }
}
