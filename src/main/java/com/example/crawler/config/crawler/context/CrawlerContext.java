package com.example.crawler.config.crawler.context;

import com.example.crawler.data.model.CrawlerError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Getter
@Setter
public class CrawlerContext {

    private final Map<String, Object> globalFields = new HashMap<>();

    private final List<ObjectNode> extractedData = new ArrayList<>();

    private final List<CrawlerError> stepErrors = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void addGlobalField(String key, Object value) {
        globalFields.put(key, value);
    }

    public void addExtractedData(ObjectNode data) {
        this.extractedData.add(data);
    }

    public void addStepError(CrawlerError error) {
        this.stepErrors.add(error);
    }

    public List<ObjectNode> getResult() {
        List<ObjectNode> finalData = new ArrayList<>();
        for (ObjectNode data : extractedData) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            globalFields.forEach((key, value) -> {
                objectNode.set(key, objectMapper.valueToTree(value));
            });
            data.fields().forEachRemaining(field -> {
                objectNode.set(field.getKey(), field.getValue());
            });
            finalData.add(objectNode);
        }
        return finalData;
    }


}
