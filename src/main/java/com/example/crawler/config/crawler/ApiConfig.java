package com.example.crawler.config.crawler;

import com.example.crawler.json_transform.model.JsonParserConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiConfig {

    private String urlPattern;

    private JsonParserConfig config;

}
