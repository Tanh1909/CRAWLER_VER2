package com.example.crawler.config.selenium;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.PageLoadStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "selenium")
public class SeleniumProperties {

    private Remote remote = new Remote();

    private Chrome chrome = new Chrome();

    private TimeOut timeOut = new TimeOut();

    @Getter
    @Setter
    public static class Remote {

        private String url;

        private Map<String, Object> capabilities;

        private boolean enableTracing = false;
    }

    @Getter
    @Setter
    public static class Chrome {

        private boolean isHeadless = false;

        private PageLoadStrategy pageLoadStrategy = PageLoadStrategy.NORMAL;

        private List<String> arguments;


    }

    @Getter
    @Setter
    public static class TimeOut {
        private int implicit = 0;

        private int pageLoad = 30;

        private int script = 30;

    }

}
