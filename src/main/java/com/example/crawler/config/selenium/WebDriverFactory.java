package com.example.crawler.config.selenium;

import com.example.crawler.config.selenium.context.WebDriverContext;
import com.example.crawler.service.devtool.IDevToolsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebDriverFactory {

    private final IDevToolsService devToolsService;

    private final SeleniumProperties seleniumProperties;

    public WebDriverContext create() {
        ChromeOptions chromeOptions = new ChromeOptions();
        SeleniumProperties.Chrome chromeProperties = seleniumProperties.getChrome();
        chromeOptions
                .addArguments(chromeProperties.getArguments())
                .setPageLoadStrategy(chromeProperties.getPageLoadStrategy());
        try {
            log.info("START CREATE WEB DRIVER");
            SeleniumProperties.Remote remoteProperties = seleniumProperties.getRemote();
            RemoteWebDriver webDriver = new RemoteWebDriver(new URL(remoteProperties.getUrl()), chromeOptions, remoteProperties.isEnableTracing());
            WebDriverContext webDriverContext = new WebDriverContext(webDriver, seleniumProperties, devToolsService);
            log.info("[{}] CREATED WEB DRIVER SUCCESSFULLY", webDriverContext.getSessionId());
            return webDriverContext;
        } catch (MalformedURLException e) {
            log.error("ERROR WHEN CREATE WEB DRIVER: {}", e.getMessage());
            return null;
        }

    }

}
