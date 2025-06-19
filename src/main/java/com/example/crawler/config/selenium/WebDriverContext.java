package com.example.crawler.config.selenium;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v114.network.Network;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.Optional;

@Log4j2
@Getter
@Setter
public class WebDriverContext implements AutoCloseable {

    private final RemoteWebDriver webDriver;

    private final DevTools devTools;

    private final SeleniumProperties seleniumProperties;

    private final String sessionId;

    public WebDriverContext(RemoteWebDriver webDriver, SeleniumProperties seleniumProperties) {
        this.seleniumProperties = seleniumProperties;
        this.webDriver = webDriver;
        SeleniumProperties.TimeOut timeOut = seleniumProperties.getTimeOut();
        webDriver.manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(timeOut.getPageLoad()))
                .implicitlyWait(Duration.ofSeconds(timeOut.getImplicit()))
                .scriptTimeout(Duration.ofSeconds(timeOut.getScript()));
        this.devTools = ((HasDevTools) (new Augmenter().augment(webDriver))).getDevTools();
        this.sessionId = webDriver.getSessionId().toString();
        this.devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

    }

    @Override
    public void close() throws Exception {
        try {
            closeDevTools();
            closeWebDriver();
        } catch (Exception e) {
            log.error("[{}] ERROR WHEN TRY CLOSE WEBDRIVER", sessionId, e);
            throw new RuntimeException(e);
        }
    }

    private void closeDevTools() {
        if (devTools != null) {
            devTools.close();
        }
    }

    private void closeWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
            log.info("[{}] CLOSE WEB DRIVER SUCCESSFULLY!!!", sessionId);
            return;
        }
        log.debug("web driver is null!!!");
    }
}
