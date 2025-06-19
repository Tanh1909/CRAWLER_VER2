package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FieldProcessorParamEnum {
    START("start"),
    END("end"),
    OLD("old"),
    NEW("new"),
    PATTERN("pattern"),
    GROUP("group"),
    REPLACEMENT("replacement"),
    INPUT_FORMAT("inputFormat"),
    OUTPUT_FORMAT("outputFormat");

    private final String value;

    public String value() {
        return this.value;
    }
}
