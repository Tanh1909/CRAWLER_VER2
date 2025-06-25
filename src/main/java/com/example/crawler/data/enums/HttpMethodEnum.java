package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public enum HttpMethodEnum {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete");

    private final String value;

    public String value() {
        return value;
    }

}
