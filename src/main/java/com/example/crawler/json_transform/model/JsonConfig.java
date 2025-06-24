package com.example.crawler.json_transform.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class JsonConfig {

    private String path;

    private String fieldName;

    private List<JsonFieldConfig> fields;

    private List<JsonConfig> children;

    public List<JsonConfig> getChildren() {
        return children == null ? Collections.emptyList() : children;
    }

    public List<JsonFieldConfig> getFields() {
        return fields == null ? Collections.emptyList() : fields;
    }
}
