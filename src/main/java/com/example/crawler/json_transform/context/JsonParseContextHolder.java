package com.example.crawler.json_transform.context;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class JsonParseContextHolder {

    private static final ThreadLocal<JsonParseContext> contextManager = new ThreadLocal<>();

    public static JsonParseContext getContext() {
        JsonParseContext jsonParseContext = contextManager.get();
        if (jsonParseContext == null) {
            jsonParseContext = new JsonParseContext();
            contextManager.set(jsonParseContext);
        }
        return jsonParseContext;
    }

    public static void clearContext() {
        log.debug("[JsonParseContextHolder] Clear context");
        contextManager.remove();
    }

    public static void addGlobalField(String key, Object value) {
        getContext().addGlobalField(key, value);
    }

    public static JsonNode getResult(JsonNode jsonNode) {
        return getContext().getResult(jsonNode);
    }

}
