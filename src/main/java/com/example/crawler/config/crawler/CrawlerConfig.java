package com.example.crawler.config.crawler;

import com.example.crawler.data.enums.CrawlerType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "crawler")
public class CrawlerConfig {

    private CrawlerType crawlerType;

    private List<StepConfig> steps;


}
