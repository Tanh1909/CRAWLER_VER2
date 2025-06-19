package com.example.crawler.config.crawler;

import com.example.crawler.config.crawler.field.FieldConfig;
import com.example.crawler.data.enums.StepType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StepConfig {

    private String name;

    private StepType type;

    private Map<String, String> parameters = new HashMap<>();

    private List<FieldConfig> fields = new ArrayList<>();

    private int delay = 1; //second

    private int timeOut = 10; //second

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

}
