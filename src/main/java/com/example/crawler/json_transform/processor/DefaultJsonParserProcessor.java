package com.example.crawler.json_transform.processor;

import com.example.crawler.config.crawler.field.FieldProcessorConfig;
import com.example.crawler.data.enums.FieldTypeEnum;
import com.example.crawler.json_transform.model.JsonFieldConfig;
import com.example.crawler.json_transform.model.JsonParserConfig;
import com.example.crawler.service.field.FieldProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class DefaultJsonParserProcessor implements JsonParserProcessor {

    private final FieldProcessor fieldProcessor;

    private final ObjectMapper objectMapper;

    @Override
    public JsonNode process(JsonNode rawData, JsonParserConfig config) {
        List<JsonFieldConfig> fieldConfigs = config.getFields();
        List<JsonParserConfig> children = config.getChildren();
        String path = config.getPath();
        JsonNode node = rawData.at(path);
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (node.isArray()) {
            for (JsonNode jsonNode : node) {
                for (JsonFieldConfig fieldConfig : fieldConfigs) {

                }
            }
        }
        return node;
    }

    private List<Map<String, Object>> processConfigRecursively(JsonNode node, JsonParserConfig config) {
        List<Map<String, Object>> mapObjects = new ArrayList<>();

        List<JsonFieldConfig> fieldConfigs = config.getFields();
        List<JsonParserConfig> children = config.getChildren();
        String path = config.getPath();


        JsonNode currentNode = node.at(path);
        if (currentNode.isArray()) {
            for (JsonNode jsonNode : currentNode) {
                Map<String, Object> objectFromJsonNode = getObjectFromJsonNode(fieldConfigs, jsonNode);
                mapObjects.add(objectFromJsonNode);
            }
            return mapObjects;
        }
        Map<String, Object> mapObject = getObjectFromJsonNode(fieldConfigs, currentNode);
        mapObjects.add(mapObject);
        if (!CollectionUtils.isEmpty(children)) {
            for (JsonParserConfig jsonParserConfig : children) {

            }
        }
        return mapObjects;
    }

    private Map<String, Object> getObjectFromJsonNode(List<JsonFieldConfig> fieldConfigs, JsonNode node) {
        Map<String, Object> mapObject = new HashMap<>();
        for (JsonFieldConfig fieldConfig : fieldConfigs) {
            String source = fieldConfig.getSource();
            String target = fieldConfig.getTarget();
            FieldTypeEnum type = fieldConfig.getType();
            List<FieldProcessorConfig> processorConfigs = fieldConfig.getProcessors();

            JsonNode jsonNode = node.get(source);
            Object finalValue = getValue(jsonNode, processorConfigs, type);
            mapObject.put(target, finalValue);
        }
        return mapObject;
    }

    private Object getValue(JsonNode jsonNode, List<FieldProcessorConfig> processorConfigs, FieldTypeEnum type) {
        Object finalValue = null;
        if (jsonNode != null && !jsonNode.isEmpty()) {
            Object rawValue = jsonNode.asText();
            for (FieldProcessorConfig processorConfig : processorConfigs) {
                rawValue = fieldProcessor.process(rawValue, processorConfig);
            }
            finalValue = fieldProcessor.convertFieldType(rawValue, type);
        }
        return finalValue;
    }

}
