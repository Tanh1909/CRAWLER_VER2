package com.example.crawler.factory.processor;

import com.example.crawler.config.crawler.processor.ProcessorConfig;
import com.example.crawler.config.selenium.context.WebDriverContext;
import com.example.crawler.config.selenium.context.WebDriverContextHolder;
import com.example.crawler.data.enums.ProcessorTypeEnum;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public abstract class ProcessorAbstract {

    @Setter(onMethod_ = @Autowired)
    protected WebDriverContextHolder webDriverContextHolder;

    public abstract ProcessorTypeEnum getType();

    public void execute(ProcessorConfig processorConfig) {
        WebDriverContext webDriverContext = getWebDriverContext();
        String sessionId = webDriverContext.getSessionId();
        ProcessorTypeEnum type = processorConfig.getType();
        String typeValue = type.value();
        try {
            log.info("[{}] Start execute processor [type: {}]", sessionId, typeValue);
            process(processorConfig);
        } catch (Exception e) {
            log.error("[{}] Error execute processor [type: {}]", sessionId, typeValue);
        } finally {
            log.info("[{}] End execute processor [type: {}]", sessionId, typeValue);
        }
    }

    protected WebDriverContext getWebDriverContext() {
        return webDriverContextHolder.getContext();
    }

    protected abstract void process(ProcessorConfig processorConfig);

}
