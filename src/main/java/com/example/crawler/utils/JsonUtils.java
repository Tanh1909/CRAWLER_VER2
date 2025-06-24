package com.example.crawler.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    public static JsonNode mergeJson(JsonNode parent, JsonNode child) {
        ObjectNode parentNode = parent.deepCopy();
        try {
            if (child.isObject()) {
                child.fields().forEachRemaining(entry -> parentNode.set(entry.getKey(), entry.getValue()));
            } else {
                for (JsonNode item : child) {
                    item.fields().forEachRemaining(entry -> parentNode.set(entry.getKey(), entry.getValue()));
                }
            }
        } catch (Exception e) {
            log.error("Error while merging json: {}", e.getMessage());
        }
        return parentNode;
    }

    public static JsonNode mergeJson(JsonNode parent, JsonNode child, String fieldMerge) {
        ObjectNode parentNode = parent.deepCopy();
        try {
            if (fieldMerge == null) {
                log.warn("FieldMerge is null!!!");
                return parent;
            }
            return parentNode.set(fieldMerge, child);
        } catch (Exception e) {
            log.error("Error while merging json: {}", e.getMessage());
        }
        return parentNode;
    }

    public static JsonNode mergeJson(String parentFieldName, JsonNode node) {
        if (parentFieldName == null) return node;
        //ve sau sua lai khong tao object mapper o day
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set(parentFieldName, node);
        return objectNode;
    }

    public static JsonNode atPath(JsonNode node, String path) {
        if (path == null || path.isEmpty()) {
            return node;
        }
        return node.at(path);
    }

    public static boolean isEmpty(JsonNode node) {
        return node == null || node.isMissingNode() || node.isNull();
    }

}
