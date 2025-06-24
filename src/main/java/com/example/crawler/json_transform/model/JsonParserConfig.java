package com.example.crawler.json_transform.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "parser")
public class JsonParserConfig {

    private JsonConfig json;

    private List<JsonGlobalConfig> globals;

    public List<JsonGlobalConfig> getGlobals() {
        return globals == null ? Collections.emptyList() : globals;
    }
}
