package com.example.crawler;

import com.example.crawler.config.selenium.WebDriverContext;
import com.example.crawler.config.selenium.WebDriverFactory;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v114.network.Network;
import org.openqa.selenium.devtools.v114.network.model.RequestId;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
@EnableConfigurationProperties
public class CrawlerApplication {

    @Autowired
    private WebDriverFactory driverFactory;

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            try (WebDriverContext webDriverContext = driverFactory.create()) {
                RemoteWebDriver webDriver = webDriverContext.getWebDriver();
                DevTools devTools = webDriverContext.getDevTools();
                devTools.addListener(Network.requestWillBeSent(), request -> {
                    String url = request.getRequest().getUrl();
                    RequestId requestId = request.getRequestId();
                    System.out.println(url + "--- " + requestId);
                });
                webDriver.get("https://mbpc.amione.vn/login");
                Thread.sleep(Duration.ofSeconds(10));
            }
        };
    }


}
