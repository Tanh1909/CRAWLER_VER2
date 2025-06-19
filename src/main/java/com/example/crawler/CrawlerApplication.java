package com.example.crawler;

import com.example.crawler.config.crawler.CrawlerConfig;
import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.config.crawler.context.CrawlerContext;
import com.example.crawler.config.selenium.WebDriverContext;
import com.example.crawler.config.selenium.WebDriverFactory;
import com.example.crawler.factory.step.StepAbstract;
import com.example.crawler.factory.step.StepFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties
public class CrawlerApplication {

    @Autowired
    private WebDriverFactory driverFactory;

    @Autowired
    private CrawlerConfig config;

    @Autowired
    private StepFactory stepFactory;

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            try (WebDriverContext webDriverContext = driverFactory.create()) {
                List<StepConfig> steps = config.getSteps();
                CrawlerContext crawlerContext = new CrawlerContext();
                int i = 0;
                for (StepConfig step : steps) {
                    i++;
                    StepAbstract stepAbstract = stepFactory.getStep(step.getType().value());
                    stepAbstract.execute(i, webDriverContext, step, crawlerContext);
                }
                System.out.println(crawlerContext);
            }
        };
    }


}
