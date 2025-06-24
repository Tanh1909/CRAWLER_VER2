package com.example.crawler.json_transform.processor;

import com.example.crawler.json_transform.model.JsonConfig;
import com.fasterxml.jackson.databind.JsonNode;

public interface JsonParserProcessor {

    JsonNode process(JsonNode rawData, JsonConfig config);

}
