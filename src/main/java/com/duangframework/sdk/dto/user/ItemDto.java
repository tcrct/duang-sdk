package com.duangframework.sdk.dto.user;

import com.alibaba.fastjson.annotation.JSONField;

public class ItemDto {

    private String id;
    @JSONField(label = "context_bady", name = "context")
    private String body;

    public ItemDto() {
    }

    public ItemDto(String id, String body) {
        this.id = id;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
