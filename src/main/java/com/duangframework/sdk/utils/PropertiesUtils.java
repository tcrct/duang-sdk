package com.duangframework.sdk.utils;

import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.enums.ICredentials;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by laotang on 2019/1/6.
 */
public class PropertiesUtils {

    private static PropertiesUtils propertiesUtils;
    private static ICredentials credentials;

    private PropertiesUtils() {
        initProperties();
    }
    public static PropertiesUtils getInstance() {
        if(null == propertiesUtils) {
            propertiesUtils  = new PropertiesUtils();
        }
        return propertiesUtils;
    }

    public String getEndPoint() {
        return credentials.getEndPoint();
    }
    public String getAppKey() {
        return credentials.getAppKey();
    }
    public String getAppSecret() {
        return credentials.getAppSecret();
    }

    private void initProperties() {
        InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(Constant.SDK_INFO_FILE);
        Properties properties = new Properties();
        try {
            if (inputStream == null) {
                throw new IllegalArgumentException(Constant.SDK_INFO_FILE + " not found on classpath");
            }
            properties.load(inputStream);

            final String endPoint = properties.getProperty(Constant.ENDPOINT_FIELD);
            final String appKey = properties.getProperty(Constant.APPKEY_FIELD);
            final String appSecret = properties.getProperty(Constant.APPSECRET_FIELD);

            credentials = new ICredentials() {
                @Override
                public String getEndPoint() {
                    return endPoint;
                }

                @Override
                public String getAppKey() {
                    return appKey;
                }

                @Override
                public String getAppSecret() {
                    return appSecret;
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to load version information for the running SDK: " + e.getMessage());
        }
    }

}
