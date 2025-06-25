package com.example.crawler.config.crawler;

import com.example.crawler.config.crawler.processor.ProcessorConfig;
import com.example.crawler.data.enums.CrawlerTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "crawler")
public class CrawlerConfig {

    private CrawlerTypeEnum crawlerType;

    private List<ApiConfig> apiConfigs;

    private List<StepConfig> steps;

    private List<ProcessorConfig> processors;

    public List<StepConfig> getSteps() {
        return steps == null ? Collections.emptyList() : steps;
    }

    public List<ProcessorConfig> getProcessors() {
        return processors == null ? Collections.emptyList() : processors;
    }

    public List<ApiConfig> getApiConfigs() {
        return apiConfigs == null ? Collections.emptyList() : apiConfigs;
    }
}
