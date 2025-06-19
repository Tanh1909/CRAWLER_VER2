package com.example.crawler.config.crawler;

import com.example.crawler.config.crawler.processor.ProcessorConfig;
import com.example.crawler.data.enums.CrawlerTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "crawler")
public class CrawlerConfig {

    private CrawlerTypeEnum crawlerType;

    private List<StepConfig> steps = new ArrayList<>();

    private List<ProcessorConfig> processors = new ArrayList<>();

}
