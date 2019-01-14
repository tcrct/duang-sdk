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

import com.duangframework.sdk.annon.ApiParam;
import com.duangframework.sdk.common.KvModle;
import com.duangframework.sdk.common.SdkDto;
import com.duangframework.sdk.constant.Constant;

import java.lang.reflect.Field;
import java.util.*;

public class SdkUtils {



    private static String defaultUserAgent = null;

    public static String getVersion() {
        return Constant.VERSION;
    }

    public static String getDefaultUserAgent() {
        if (defaultUserAgent == null) {
            defaultUserAgent = Constant.USER_AGENT_PREFIX + "/" + getVersion() + "(" + System.getProperty("os.name") + "/"
                    + System.getProperty("os.version") + "/" + System.getProperty("os.arch") + ";"
                    + System.getProperty("java.version") + ")";
        }
        return defaultUserAgent;
    }


    /**
     * 获取成员变量
     * @param  obj 对象
     * @param field  变量字段
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
