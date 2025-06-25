package com.example.crawler.config.crawler;

import com.example.crawler.config.crawler.field.FieldConfig;
import com.example.crawler.data.enums.StepTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StepConfig {

    private static final Integer DEFAULT_DELAY = 1; //seconds

    private static final Integer DEFAULT_TIMEOUT = 10; //seconds

    private String name;

    private StepTypeEnum type;

    private Map<String, String> parameters;

    private List<FieldConfig> fields;

    private Integer delay;

    private Integer timeOut;

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getParameter(String key, String defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }

    public int getIntParam(String key, int defaultValue) {
        try {
            return Integer.parseInt(parameters.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Map<String, String> getParameters() {
        return parameters == null ? Collections.emptyMap() : parameters;
    }

    public List<FieldConfig> getFields() {
        return fields == null ? Collections.emptyList() : fields;
    }

    public int getDelay() {
        return delay == null ? DEFAULT_DELAY : delay;
    }

    public int getTimeOut() {
        return timeOut == null ? DEFAULT_TIMEOUT : timeOut;
    }
}
