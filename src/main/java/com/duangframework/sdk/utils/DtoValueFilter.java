package com.duangframework.sdk.utils;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;

import java.lang.reflect.Field;
import java.util.Map;

public class DtoValueFilter implements ContextValueFilter {

    private Map<String, Object> convertMap;

    public DtoValueFilter(Map<String, Object> convertMap) {
        this.convertMap = convertMap;
    }

    public Map<String, Object> getConvertMap() {
        return convertMap;
    }

    /**
     *
     * @param context
     * @param object  对象
     * @param name  字段属性
     * @param value    字段值
     * @return
     */
    @Override
    public Object process(BeanContext context, Object object, String name, Object value) {
//         if(object != null && null != value) {
//            System.out.println(object.getClass().getSimpleName() + "           " + name + "           " + value + "      " + value.getClass());
//        }

        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            JSONField jsonField = field.getAnnotation(JSONField.class);
            if(null != jsonField && null != value)  {
                String key = jsonField.label();
                convertMap.put(key.isEmpty() ?jsonField.name() : key, value);
            }
        }
        return value;
    }
}
