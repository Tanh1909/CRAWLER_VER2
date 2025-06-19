package com.example.crawler.factory.processor;

import com.example.crawler.config.crawler.context.CrawlerContext;
import com.example.crawler.config.crawler.processor.ProcessorConfig;
import com.example.crawler.config.selenium.WebDriverContext;
import com.example.crawler.data.enums.ProcessorTypeEnum;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class ProcessorAbstract {

    public abstract ProcessorTypeEnum getType();

    public void execute(WebDriverContext webDriverContext, CrawlerContext crawlerContext, ProcessorConfig processorConfig) {
        String sessionId = webDriverContext.getSessionId();
        ProcessorTypeEnum type = processorConfig.getType();
        String typeValue = type.value();
        try {
            log.info("[{}] Start execute processor [type: {}]", sessionId, typeValue);
            process(webDriverContext, crawlerContext, processorConfig);
        } catch (Exception e) {
            log.error("[{}] Error execute processor [type: {}]", sessionId, typeValue);
        } finally {
            log.info("[{}] End execute processor [type: {}]", sessionId, typeValue);
        }
    }

    protected abstract void process(WebDriverContext webDriverContext, CrawlerContext crawlerContext, ProcessorConfig processorConfig);

}
