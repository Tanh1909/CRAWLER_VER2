package com.example.crawler.json_transform.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class JsonParserConfig {

    private String path;

    private List<JsonFieldConfig> fields;

    private List<JsonParserConfig> children;

    public List<JsonParserConfig> getChildren() {
        return children == null ? Collections.emptyList() : children;
    }
}
