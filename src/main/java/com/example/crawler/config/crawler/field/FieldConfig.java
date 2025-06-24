package com.example.crawler.config.crawler.field;

import com.example.crawler.data.enums.FieldTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class FieldConfig {

    private String fieldName;

    private FieldTypeEnum fieldType = FieldTypeEnum.TEXT;

    private Map<String, String> parameters = new HashMap<>();

    private List<FieldProcessorConfig> processors = new ArrayList<>();

    private boolean isArray = false;

    private boolean isGlobal = false;

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getParameter(String key, String defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }

    public List<FieldProcessorConfig> getProcessors() {
        return processors == null ? Collections.emptyList() : processors;
    }


}
