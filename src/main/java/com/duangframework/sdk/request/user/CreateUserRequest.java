package com.duangframework.sdk.request.user;

import com.duangframework.sdk.common.AbstractSdkRequest;
import com.duangframework.sdk.enums.ContentType;
import com.duangframework.sdk.enums.HttpMethod;
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
public class CreateUserRequest extends AbstractSdkRequest {

    public CreateUserRequest(CreateUserDto dto) {
        super(dto);
    }

    public CreateUserRequest(Map<String,String> headerMap, CreateUserDto dto) {
        super(headerMap, dto);
    }


    /**
     * 请求的API地址
     */
    private static final String API = "/signet-api/Account/create/{USERNAME}/{context_bady}";

    @Override
    public String getRequestApi() {
        setRequestApi(API, sdkDto);
        return requestApi;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }


    @Override
    public String getContentType() {
        return ContentType.JSON.getValue();
    }

    @Override
    public boolean isRestful() {
        return true;
    }
}
