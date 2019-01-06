package com.duangframework.sdk.utils;

import com.duangframework.sdk.common.SdkDto;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 仅针对duang-sdk下的dto进行生成json字符，不依赖于第三方包
 *
 * Created by laotang on 2019/1/6.
 */
public class JsonUtils {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String toJson(Object valueObj) {
        if (null == valueObj)
            throw new NullPointerException("valueObj is null");

        if (valueObj instanceof String) {
            return "\"" + valueObj + "\"";
        }

        if (valueObj instanceof Integer) {
            return valueObj.toString();
        }

        if (valueObj instanceof Map) {
            return map2Json((Map) valueObj);
        }

        if (valueObj instanceof Collection) {
            return collection2Json((Collection) valueObj);
        }

        return null;

    }

    private static String map2Json(Map<String, Object> map) {
        if (null == map || map.isEmpty())
            return "null";
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator(); iter.hasNext();) {
            if (first)
                first = false;
            else
                sb.append(",");
            Map.Entry<String, Object> entry = iter.next();
            sb.append("\"").append(entry.getKey()).append("\":").append(toJson(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    private static String collection2Json(Collection<Object> collection) {
        if (null == collection || collection.isEmpty())
            return "null";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Iterator<Object> iter = collection.iterator(); iter.hasNext();) {
            if (first)
                first = false;
            else
                sb.append(",");
            Object obj = iter.next();
            if (null == obj) {
                sb.append("null");
            } else {
                sb.append(toJson(obj));
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
