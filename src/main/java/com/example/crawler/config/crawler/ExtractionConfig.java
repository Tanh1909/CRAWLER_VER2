package com.example.crawler.config.crawler;

import com.example.crawler.data.enums.SelectorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtractionConfig {

    private String fieldName;

    private SelectorType selectorType;

    private String selector;

    private String attribute;

    private boolean multiple;

}
