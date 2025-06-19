package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SelectorTypeEnum {
    ID("ID"),
    TAG("TAG"),
    CSS("CSS"),
    NAME("NAME"),
    XPATH("XPATH"),
    CLASS("CLASS");

    private final String value;

    public String value() {
        return value;
    }

    public static SelectorTypeEnum from(String value) {
        for (SelectorTypeEnum locatorTypeEnum : SelectorTypeEnum.values()) {
            if (locatorTypeEnum.value.equalsIgnoreCase(value)) {
                return locatorTypeEnum;
            }
        }
        throw new IllegalArgumentException("Invalid selector type: " + value);
    }
}
