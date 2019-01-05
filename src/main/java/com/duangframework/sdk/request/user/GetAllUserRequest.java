package com.duangframework.sdk.request.user;

import com.duangframework.sdk.common.AbstractClientRequest;
import com.duangframework.sdk.common.HttpMethod;
import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.dto.user.GetAllUserDto;


/**
 * 取所有用户
 *
 * @author laotang
 * @since 1.0
 * @date 2019-01-04
 *
 */
public class GetAllUserRequest extends AbstractClientRequest {

    public GetAllUserRequest(GetAllUserDto getAllUserDto) {
        super(getAllUserDto);
    }

    /**
     * 请求的API地址
     */
    private static final String API = "/signet-api/Account/getUser";


    @Override
    public String getRequestApi() {
        setRequestApi(API, baseDto);
        return requestApi;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getContentType() {
        return Constant.CONTENT_TYPE_JSON;
    }


}
