package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProcessorTypeEnum {

    KAFKA("kafka");

    private final String value;

    public String value() {
        return value;
    }

    public static ProcessorTypeEnum from(String value) {
        for (ProcessorTypeEnum processorTypeEnum : ProcessorTypeEnum.values()) {
            if (processorTypeEnum.value.equalsIgnoreCase(value)) {
                return processorTypeEnum;
            }
        }
        throw new IllegalArgumentException("Not found processor type: " + value);
    }

}
