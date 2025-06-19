package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HTMLParamEnum {

    URL("url"),

    RETRY("retry"),

    RETRY_DELAY("retryDelay"),

    SELECTOR("selector"),

    SELECTOR_TYPE("selectorType"),

    VALUE("value"),

    ATTRIBUTE("attribute"),

    ;

    private final String value;

    public String value() {
        return this.value;
    }
}
