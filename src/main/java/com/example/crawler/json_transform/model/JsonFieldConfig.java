package com.example.crawler.json_transform.model;

import com.example.crawler.config.crawler.field.FieldProcessorConfig;
import com.example.crawler.data.enums.FieldTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class JsonFieldConfig {

    private String source;

    private String target;

    private FieldTypeEnum type;

    private List<FieldProcessorConfig> processors;

    public List<FieldProcessorConfig> getProcessors() {
        return processors == null ? Collections.emptyList() : processors;
    }

    public FieldTypeEnum getType() {
        return type == null ? FieldTypeEnum.TEXT : type;
    }

}
