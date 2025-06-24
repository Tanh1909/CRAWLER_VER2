package com.example.crawler.json_transform.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Getter
@Setter
public class JsonParseContext {

    private final Map<String, Object> globalFields = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void addGlobalField(String key, Object value) {
        globalFields.put(key, value);
    }

    public JsonNode getResult(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            return addGlobalFieldToSingleJson(jsonNode);
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            for (JsonNode node : jsonNode) {
                JsonNode item = addGlobalFieldToSingleJson(node);
                arrayNode.add(item);
            }
            return arrayNode;
        } else return jsonNode;
    }

    private JsonNode addGlobalFieldToSingleJson(JsonNode jsonNode) {
        ObjectNode objectNode = jsonNode.deepCopy();
        getGlobalFields().forEach((key, value) -> objectNode.set(key, objectMapper.valueToTree(value)));
        return objectNode;
    }

}
