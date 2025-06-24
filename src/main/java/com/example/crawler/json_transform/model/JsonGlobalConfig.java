package com.example.crawler.json_transform.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class JsonGlobalConfig {

    private String path;

    private List<JsonFieldConfig> fields;

    public List<JsonFieldConfig> getFields() {
        return fields == null ? Collections.emptyList() : fields;
    }

}
