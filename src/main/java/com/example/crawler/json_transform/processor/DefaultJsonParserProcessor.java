package com.example.crawler.json_transform.processor;

import com.example.crawler.config.crawler.field.FieldProcessorConfig;
import com.example.crawler.data.enums.FieldTypeEnum;
import com.example.crawler.json_transform.context.JsonParseContextHolder;
import com.example.crawler.json_transform.model.JsonConfig;
import com.example.crawler.json_transform.model.JsonFieldConfig;
import com.example.crawler.json_transform.model.JsonGlobalConfig;
import com.example.crawler.json_transform.model.JsonParserConfig;
import com.example.crawler.service.field.FieldProcessor;
import com.example.crawler.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class DefaultJsonParserProcessor implements JsonParserProcessor {

    private final FieldProcessor fieldProcessor;

    private final ObjectMapper objectMapper;


    @Override
    public JsonNode process(JsonNode rawData, JsonParserConfig config) {
        try {
            log.debug("Start process rawData: {}", rawData.toPrettyString());
            if (JsonUtils.isEmpty(rawData)) {
                log.debug("RawData is empty");
                return rawData;
            }
            //Handle Global
            this.processGlobalConfig(rawData, config.getGlobals());
            //Handle rawData
            int recursionDepth = 0;
            JsonNode processData = processConfigRecursively(rawData, config.getJson(), recursionDepth);
            //Merge Global with data
            JsonNode result = JsonParseContextHolder.getResult(processData);
            log.debug("Result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error process rawData: {}", e.getMessage());
            return null;
        } finally {
            JsonParseContextHolder.clearContext();
            log.debug("End process");
        }

    }

    private void processGlobalConfig(JsonNode node, List<JsonGlobalConfig> globalConfigs) {
        if (CollectionUtils.isEmpty(globalConfigs)) {
            log.debug("Not process global config because configs is empty");
        }
        for (JsonGlobalConfig globalConfig : globalConfigs) {
            String path = globalConfig.getPath();
            List<JsonFieldConfig> fieldConfigs = globalConfig.getFields();
            JsonNode jsonNode = JsonUtils.atPath(node, path);
            addGlobalField(jsonNode, fieldConfigs);
        }
    }

    private void addGlobalField(JsonNode node, List<JsonFieldConfig> fieldConfigs) {
        for (JsonFieldConfig fieldConfig : fieldConfigs) {
            String target = fieldConfig.getTarget();
            String source = fieldConfig.getSource();
            FieldTypeEnum type = fieldConfig.getType();
            List<FieldProcessorConfig> processorConfigs = fieldConfig.getProcessors();
            JsonNode value = node.get(source);
            JsonNode finalValue = processRawValue(value, processorConfigs, type);
            log.debug("Add global field value [source: {} - target: {}] {} -> {} ", source, target, value, finalValue);
            JsonParseContextHolder.addGlobalField(target, finalValue);
        }
    }

    private JsonNode processConfigRecursively(JsonNode node, JsonConfig config, int depth) {
        List<JsonFieldConfig> fieldConfigs = config.getFields();
        List<JsonConfig> childrenConfig = config.getChildren();
        String path = config.getPath();
        try {
            log.debug("Start processing config at path: {}", path);
            JsonNode currentNode = JsonUtils.atPath(node, path);
            int nextDepth = depth + 1;
            if (currentNode.isArray()) {
                ArrayNode arrayNode = objectMapper.createArrayNode();
                for (JsonNode jsonNode : currentNode) {
                    arrayNode.add(handleSingleJsonNode(jsonNode, fieldConfigs, childrenConfig, nextDepth));
                }
                return arrayNode;
            }
            return handleSingleJsonNode(currentNode, fieldConfigs, childrenConfig, nextDepth);
        } catch (Exception e) {
            log.error("Error processing config at path: {}. Because: {}", config.getPath(), e.getMessage());
            return null;
        }
    }

    private JsonNode handleSingleJsonNode(JsonNode currentNode, List<JsonFieldConfig> fieldConfigs, List<JsonConfig> childrenConfig, int depth) {
        log.debug("[DEPTH: {}] Start handleSingleJsonNode: {}", depth, currentNode);
        JsonNode parentNode = getObjectFromJsonNode(currentNode, fieldConfigs);
        if (!CollectionUtils.isEmpty(childrenConfig)) {
            for (JsonConfig childConfig : childrenConfig) {
                String childFieldName = childConfig.getFieldName();
                JsonNode childNode = this.processConfigRecursively(currentNode, childConfig, depth);
                parentNode = JsonUtils.mergeJson(parentNode, childNode, childFieldName);
                log.debug("Merge child [DEPTH: {}] to parents [DEPTH: {}] with fieldName: [{}] -> {} ", depth + 1, depth, childFieldName, parentNode);
            }
        }
        return parentNode;
    }

    private JsonNode getObjectFromJsonNode(JsonNode node, List<JsonFieldConfig> fieldConfigs) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (JsonFieldConfig fieldConfig : fieldConfigs) {
            String source = fieldConfig.getSource();
            String target = fieldConfig.getTarget();
            FieldTypeEnum type = fieldConfig.getType();
            List<FieldProcessorConfig> processorConfigs = fieldConfig.getProcessors();

            JsonNode value = node.get(source);
            JsonNode finalValue = processRawValue(value, processorConfigs, type);
            log.debug("Process field value [source: {} - target: {}] {} -> {} ", source, target, value, finalValue);
            objectNode.set(target, finalValue);
        }
        log.debug("Json after get from fieldConfig: {}", objectNode);
        return objectNode;
    }

    private JsonNode processRawValue(JsonNode value, List<FieldProcessorConfig> processorConfigs, FieldTypeEnum type) {
        try {
            Object finalValue;
            if (!JsonUtils.isEmpty(value) && !value.isArray()) {
                Object rawValue = value.asText();
                for (FieldProcessorConfig processorConfig : processorConfigs) {
                    rawValue = fieldProcessor.process(rawValue, processorConfig);
                }
                finalValue = fieldProcessor.convertFieldType(rawValue, type);
                return objectMapper.valueToTree(finalValue);
            }
            log.error("Error processRawValue: {} because value is null or array", value);
            return null;
        } catch (Exception e) {
            log.error("Error when processRawValue: {}", e.getMessage());
            return null;
        }
    }


}
