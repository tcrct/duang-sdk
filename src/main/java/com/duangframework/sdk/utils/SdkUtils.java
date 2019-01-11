/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.duangframework.sdk.utils;

import com.duangframework.encrypt.core.EncryptDto;
import com.duangframework.sdk.annon.ApiParam;
import com.duangframework.sdk.common.HttpHeaderNames;
import com.duangframework.sdk.common.KvModle;
import com.duangframework.sdk.common.SdkDto;

import java.lang.reflect.Field;
import java.util.*;

public class SdkUtils {

    private static final String USER_AGENT_PREFIX = "duang-sdk-java";
    private static final String FRAMEWORK_OWNER = "duang";
    private static final String RANDOM_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static String version = "1.0.0";

    private static String defaultUserAgent = null;

    public static String getVersion() {
        return version;
    }

    public static String getDefaultUserAgent() {
        if (defaultUserAgent == null) {
            defaultUserAgent = USER_AGENT_PREFIX + "/" + getVersion() + "(" + System.getProperty("os.name") + "/"
                    + System.getProperty("os.version") + "/" + System.getProperty("os.arch") + ";"
                    + System.getProperty("java.version") + ")";
        }
        return defaultUserAgent;
    }

    public static String getEndPoint() {
        return PropertiesUtils.getInstance().getEndPoint();
    }

    public static String getAppKey() {
        return PropertiesUtils.getInstance().getAppKey();
    }

    public static String getAppSecret() {
        return PropertiesUtils.getInstance().getAppSecret();
    }

    /**
     * 获取成员变量
     * @param  obj 对象
     * @aram field  变量字段
     */
    public static Object getFieldValue(Object obj, Field field) {
        Object propertyValue = null;
        try {
            field.setAccessible(true);
            propertyValue = field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return propertyValue;
    }


    /**
     * 按uri(\n)->header(\n)->params顺序合成一个字符串，每一个以换行符\n分隔
     * @param encryptDto
     * @return
     */
    public static String buildEncryptString(EncryptDto encryptDto) {
        if(null == encryptDto) {
            throw new NullPointerException("encryptDto is null");
        }
        StringBuilder signStr = new StringBuilder();
        String lb = "\n";
        signStr.append(encryptDto.getUri()).append(lb);
        Map<String,String> headerParams = encryptDto.getHeaders();
        //如果有@"Accept"头，这个头需要参与签名
        if (headerParams.containsKey(HttpHeaderNames.ACCEPT)) {
            signStr.append(headerParams.get(HttpHeaderNames.ACCEPT)).append(lb);
        }
        //如果有@"Content-MD5"头，这个头需要参与签名
        if (headerParams.containsKey(HttpHeaderNames.CONTENT_MD5)) {
            signStr.append(headerParams.get(HttpHeaderNames.CONTENT_MD5)).append(lb);
        }
        //如果有@"Content-Type"头，这个头需要参与签名
        if (headerParams.containsKey(HttpHeaderNames.CONTENT_TYPE)) {
            signStr.append(headerParams.get(HttpHeaderNames.CONTENT_TYPE)).append(lb);
        }
        //签名优先读取HTTP_CA_HEADER_DATE，因为通过浏览器过来的请求不允许自定义Date（会被浏览器认为是篡改攻击）
        if (headerParams.containsKey(HttpHeaderNames.DATE)) {
            signStr.append(headerParams.get(HttpHeaderNames.DATE)).append(lb);
        }

        // Header部份
        Map<String,String> headerParamItemMap = new TreeMap<String,String>(headerParams);
        for(Iterator<Map.Entry<String,String>> iterator = headerParamItemMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String,String> entry = iterator.next();
            if(entry.getKey().startsWith(FRAMEWORK_OWNER)) {
                signStr.append(entry.getValue()).append(lb);
            }
        }

        // Param部份
        Map<String, Object> paramsMap = encryptDto.getParams();
        if(null != paramsMap && paramsMap.isEmpty()) {
            Map<String, Object> parameters = new TreeMap<String, Object>(paramsMap);
            for(Iterator<Map.Entry<String,Object>> iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<String,Object> entry = iterator.next();
                Object value = entry.getValue();
                if(null != value) {
                    signStr.append(entry.getKey()).append("=").append(value).append("&");
                }
            }
            if (signStr.toString().endsWith("&")) {
                signStr.deleteCharAt(signStr.length() - 1);
            }
        }
        return signStr.toString();
    }

    // 随机生成16位字符串
    public static String getRandomStr() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(RANDOM_STR.length());
            sb.append(RANDOM_STR.charAt(number));
        }
        return sb.toString();
    }

    // 生成4个字节的网络字节序
    public static byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    public static String getRequestHeaderTimeStamp(Map<String,String> valueMap){
        String value = valueMap.get(HttpHeaderNames.DATE);
        if(null == value || value.isEmpty()) {
            value = Long.toString(System.currentTimeMillis());
            valueMap.put(HttpHeaderNames.DATE, value);
        }
        return value;
    }

    public static String getRequestHeaderNonce(Map<String,String> valueMap){
        String value = valueMap.get(HttpHeaderNames.NONCE);
        if(null == value || value.isEmpty()) {
            value = getRandomStr();
            valueMap.put(HttpHeaderNames.NONCE, value);
        }
        return value;
    }

    @SuppressWarnings("rawtypes")
    public static String convertObjectToString(Object obj, String separator) {
        if(null == obj) return "";
        if(obj instanceof Collection)
            return join(separator,(Collection)obj).trim();
        return obj+"";
    }

    private static  <T> String join(String separator, T... elements) {
        Object[] array = (Object[])elements;
        if (array == null) {
            return null;
        } else {
            int startIndex = 0;
            int endIndex = array.length;
            int noOfItems = endIndex - startIndex;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);
                for(int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }
                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }
                return buf.toString();
            }
        }
    }

    public static KvModle dto2KvModle(SdkDto sdkDto) {
        Map<String,Object> restfulApiMap = new HashMap<>();
        Map<String,Object> dtoMap = new HashMap<>();
        dto2Map(sdkDto, restfulApiMap, dtoMap);
        return new KvModle(restfulApiMap, dtoMap);
    }

    private static void dto2Map(SdkDto sdkDto, Map<String,Object> restfulApiMap, Map<String,Object> dtoMap) {
        Field[] fields = sdkDto.getClass().getDeclaredFields();
        if(null != fields) {
            for(Field field : fields) {
                Object value = getFieldValue(sdkDto, field);
                if(null != value) {
                    if(value instanceof SdkDto) {
                        Map<String, Object> itemMap = new HashMap<>();
                        dto2Map((SdkDto) value, restfulApiMap, itemMap);
                        dtoMap.put(field.getName(), itemMap);
                    } else {
                        ApiParam apiParam = field.getAnnotation(ApiParam.class);
                        String key = field.getName();
                        if(null != apiParam) {
                            key = apiParam.name();
                            String label = apiParam.label();
                            if(!label.isEmpty()) {
                                restfulApiMap.put(label, value);
                            }
                        }
                        dtoMap.put(key, value);
                    }
                }
            }
        }
    }
}
