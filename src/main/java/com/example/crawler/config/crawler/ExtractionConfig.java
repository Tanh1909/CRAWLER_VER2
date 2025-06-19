package com.example.crawler.config.crawler;

import com.example.crawler.data.enums.SelectorTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtractionConfig {

    private String fieldName;

    private SelectorTypeEnum selectorType;

    private String selector;

    private String attribute;

    private boolean multiple;

}
