package com.example.crawler.config.crawler;

import com.example.crawler.data.enums.HttpMethodEnum;
import com.example.crawler.json_transform.model.JsonParserConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiConfig {

    private String path;

    private String method = HttpMethodEnum.GET.value();

    private JsonParserConfig config;

}
