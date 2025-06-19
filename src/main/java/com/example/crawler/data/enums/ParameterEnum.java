package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ParameterEnum {

    //for click
    URL("url"),

    //for get By
    SELECTOR("selector"),
    SELECTOR_TYPE("selectorType"),
    VALUE("value"),

    //processor
    START("start"),
    END("end"),
    OLD("old"),
    NEW("new"),
    PATTERN("pattern"),
    REPLACEMENT("replacement"),
    INPUT_FORMAT("inputFormat"),
    OUTPUT_FORMAT("outputFormat"),
    ;

    private final String value;

    public String value() {
        return this.value;
    }

}
