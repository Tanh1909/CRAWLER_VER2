package com.example.crawler.service.field;

import com.example.crawler.config.crawler.field.FieldProcessorConfig;
import com.example.crawler.data.enums.FieldTypeEnum;

public interface FieldProcessor {

    Object process(Object rawValue, FieldProcessorConfig config);

    Object convertFieldType(Object rawValue, FieldTypeEnum fieldTypeEnum);
}
