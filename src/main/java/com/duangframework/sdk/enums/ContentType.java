package com.duangframework.sdk.enums;

/**
 * Created by laotang on 2019/1/6.
 */
public enum ContentType {
    /**
     * 表单类型Content-Type
     */
    FORM ("application/x-www-form-urlencoded; charset=UTF-8"),
    /**
     * 流类型Content-Type
     */
    STREAM("application/octet-stream; charset=UTF-8"),
    /**
     * JSON类型Content-Type
     */
    JSON("application/json; charset=UTF-8"),
    /**
     * XML类型Content-Type
     */
    XML("application/xml; charset=UTF-8"),
    /**
     * 文本类型Content-Type
     */
    TEXT("application/text; charset=UTF-8");

    private String value;

    private ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
