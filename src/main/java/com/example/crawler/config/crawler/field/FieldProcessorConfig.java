package com.example.crawler.config.crawler.field;

import com.example.crawler.data.enums.ProcessorTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FieldProcessorConfig {

    private ProcessorTypeEnum type = ProcessorTypeEnum.NONE;

    private Map<String, String> parameters = new HashMap<>();

    public String getParam(String key) {
        return parameters.get(key);
    }

    public String getParam(String key, String defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }

    public Integer getIntParam(String key, Integer defaultValue) {
        String stringParam = parameters.get(key);
        return stringParam == null ? defaultValue : Integer.parseInt(stringParam, defaultValue);
    }
}
