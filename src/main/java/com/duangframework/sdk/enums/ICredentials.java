package com.duangframework.sdk.enums;

/**
 *
 * Created by laotang on 2019/1/6.
 */
public interface ICredentials {
    /**
     * endpoint
     * @return
     */
    String getEndPoint();

    /**
     *  key
     * @return
     */
    String getAppKey();

    /**
     * secret
     * @return
     */
    String getAppSecret();
}
