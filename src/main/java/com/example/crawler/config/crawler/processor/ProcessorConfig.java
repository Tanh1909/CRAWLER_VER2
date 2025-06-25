package com.example.crawler.config.crawler.processor;

import com.example.crawler.data.enums.ProcessorTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

@Getter
@Setter
public class ProcessorConfig {

    private ProcessorTypeEnum type = ProcessorTypeEnum.KAFKA;

    private Map<String, String> parameters;

    public String getParam(String key) {
        return parameters.get(key);
    }

    public String getParam(String key, String defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }

    public Map<String, String> getParameters() {
        return parameters == null ? Collections.emptyMap() : parameters;
    }
}
