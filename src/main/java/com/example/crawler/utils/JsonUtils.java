package com.example.crawler.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    public static void mergeJson(ObjectNode parent, ObjectNode child) {
        try {
            child.fields().forEachRemaining(entry -> {
                parent.set(entry.getKey(), entry.getValue());
            });
        } catch (Exception e) {
            log.error("Error while merging json: {}", e.getMessage());
        }
    }

    public static void mergeJson(ObjectNode parent, Collection<ObjectNode> child) {
        for (ObjectNode childObjectNode : child) {
            mergeJson(parent, childObjectNode);
        }
    }

    public static JsonNode getNodeAtPath(JsonNode node, String path) {
        if (path == null || path.isEmpty()) {
            return node;
        }
        return node.get(path);
    }

    public static boolean isEmpty(JsonNode node) {
        return node == null || node.isMissingNode() || node.isNull();
    }

}
