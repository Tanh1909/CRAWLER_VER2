package com.example.crawler.config.crawler.context.crawler;

import com.example.crawler.data.model.CrawlerError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@Getter
@Setter
public class CrawlerContextHolder {

    private static final ThreadLocal<CrawlerContext> contextManager = new ThreadLocal<>();

    public static CrawlerContext getContext() {
        CrawlerContext crawlerContext = contextManager.get();
        if (crawlerContext == null) {
            crawlerContext = new CrawlerContext();
            contextManager.set(crawlerContext);
        }
        return crawlerContext;
    }

    public static void clearContext() {
        log.debug("[CrawlerContextHolder] Clear context");
        contextManager.remove();
    }

    public static List<JsonNode> getResult() {
        return getContext().getResult();
    }

    public static void addExtractedData(ObjectNode node) {
        getContext().addExtractedData(node);
    }

    public static void addGlobalField(String key, Object value) {
        getContext().addGlobalField(key, value);
    }

    public static void addStepError(CrawlerError error) {
        getContext().addStepError(error);
    }

}
