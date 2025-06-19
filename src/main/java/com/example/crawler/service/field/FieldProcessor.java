package com.example.crawler.service.field;

import com.example.crawler.config.crawler.field.FieldConfig;
import com.example.crawler.config.crawler.field.FieldProcessorConfig;

public interface FieldProcessor {

    Object process(Object rawValue, FieldProcessorConfig config);

    Object convertFieldType(Object rawValue, FieldConfig fieldConfig);
}
