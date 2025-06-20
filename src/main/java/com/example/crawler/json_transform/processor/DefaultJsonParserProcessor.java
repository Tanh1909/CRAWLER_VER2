package com.example.crawler.json_transform.processor;

import com.example.crawler.config.crawler.field.FieldProcessorConfig;
import com.example.crawler.data.enums.FieldTypeEnum;
import com.example.crawler.json_transform.model.JsonFieldConfig;
import com.example.crawler.json_transform.model.JsonParserConfig;
import com.example.crawler.service.field.FieldProcessor;
import com.example.crawler.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class DefaultJsonParserProcessor implements JsonParserProcessor {

    private final FieldProcessor fieldProcessor;

    private final ObjectMapper objectMapper;

    private static final ThreadLocal<Integer> RECURSION_DEPTH = ThreadLocal.withInitial(() -> 0);


    @Override
    public JsonNode process(JsonNode rawData, JsonParserConfig config) {
        try {
            log.debug("Start process rawData: {}", rawData.toPrettyString());
            if (JsonUtils.isEmpty(rawData)) {
                log.debug("RawData is empty");
                return rawData;
            }
            List<ObjectNode> objectNodes = processConfigRecursively(rawData, config);
            log.debug("End process");
            return objectMapper.valueToTree(objectNodes);
        } catch (Exception e) {
            log.error("Error process rawData: {}", e.getMessage());
            return null;
        } finally {
            RECURSION_DEPTH.remove();
        }

    }

    private List<ObjectNode> processConfigRecursively(JsonNode rootNode, JsonParserConfig config) {
        try {
            int currentDepth = RECURSION_DEPTH.get();
            List<JsonFieldConfig> fieldConfigs = config.getFields();
            List<JsonParserConfig> childrenConfig = config.getChildren();
            String path = config.getPath();
            log.debug("[DEPTH: {}] Processing config at path: {}", currentDepth, path);
            RECURSION_DEPTH.set(currentDepth + 1);
            JsonNode currentNode = JsonUtils.getNodeAtPath(rootNode, path);
            if (currentNode.isArray()) {
                List<ObjectNode> objects = new ArrayList<>();
                for (JsonNode jsonNode : currentNode) {
                    objects.add(handleSingleJsonNode(rootNode, jsonNode, fieldConfigs, childrenConfig));
                }
                return objects;
            }

            ObjectNode objectNode = handleSingleJsonNode(rootNode, currentNode, fieldConfigs, childrenConfig);
            return List.of(objectNode);
        } catch (Exception e) {
            log.error("Error processing config at path: {}. Because: {}", config.getPath(), e.getMessage());
            return Collections.emptyList();
        }
    }

    private ObjectNode handleSingleJsonNode(JsonNode rootNode, JsonNode currentNode, List<JsonFieldConfig> fieldConfigs, List<JsonParserConfig> childrenConfig) {
        ObjectNode objectNode = getObjectFromJsonNode(currentNode, fieldConfigs);
        if (!CollectionUtils.isEmpty(childrenConfig)) {
            for (JsonParserConfig childConfig : childrenConfig) {
                List<ObjectNode> childObjectNodes = this.processConfigRecursively(rootNode, childConfig);
                JsonUtils.mergeJson(objectNode, childObjectNodes);
            }
        }
        return objectNode;
    }

    private ObjectNode getObjectFromJsonNode(JsonNode currentNode, List<JsonFieldConfig> fieldConfigs) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (JsonFieldConfig fieldConfig : fieldConfigs) {
            String source = fieldConfig.getSource();
            String target = fieldConfig.getTarget();
            FieldTypeEnum type = fieldConfig.getType();
            List<FieldProcessorConfig> processorConfigs = fieldConfig.getProcessors();

            JsonNode jsonNode = currentNode.get(source);
            ObjectNode finalValue = processRawValue(jsonNode, processorConfigs, type);
            objectNode.set(target, finalValue);
        }
        return objectNode;
    }

    private ObjectNode processRawValue(JsonNode jsonNode, List<FieldProcessorConfig> processorConfigs, FieldTypeEnum type) {
        Object finalValue = null;
        if (!JsonUtils.isEmpty(jsonNode)) {
            Object rawValue = jsonNode.asText();
            for (FieldProcessorConfig processorConfig : processorConfigs) {
                rawValue = fieldProcessor.process(rawValue, processorConfig);
            }
            finalValue = fieldProcessor.convertFieldType(rawValue, type);
        }
        return objectMapper.valueToTree(finalValue);
    }


}
