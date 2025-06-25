package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessorParamEnum {

    TOPIC("topic"),
    KEY("key");

    private final String value;

    public String value() {
        return value;
    }
}
