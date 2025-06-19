package com.example.crawler.config.crawler;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HtmlConfig {

    private List<ExtractionConfig> extractions = new ArrayList<>();

    private boolean takeScreenshot;

}
