package com.example.crawler.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CrawlerTypeEnum {
    HTML("HTML"),
    API("API");

    private final String value;

    public String value() {
        return value;
    }
}
