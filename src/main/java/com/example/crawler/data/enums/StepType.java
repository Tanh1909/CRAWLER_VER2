package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StepType {
    GET_URL("GET_URL"),
    SEND_KEY("SEND_KEY"),
    CLICK("CLICK"),
    WAIT("WAIT"),
    SCROLL("SCROLL"),
    HOVER("HOVER"),
    EXTRACT_DATA("EXTRACT_DATA");

    private final String value;

    public String value() {
        return this.value;
    }

    public static StepType from(String value) {
        for (StepType stepType : StepType.values()) {
            if (stepType.value.equalsIgnoreCase(value)) {
                return stepType;
            }
        }
        throw new IllegalArgumentException("not found step type " + value);
    }
}
