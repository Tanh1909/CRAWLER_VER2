package com.example.crawler.config.selenium.context;

import com.example.crawler.config.selenium.WebDriverFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class WebDriverContextHolder {

    private final WebDriverFactory webDriverFactory;

    private static final ThreadLocal<WebDriverContext> contextManager = new ThreadLocal<>();

    public WebDriverContext getContext() {
        WebDriverContext webDriverContext = contextManager.get();
        if (webDriverContext == null) {
            webDriverContext = webDriverFactory.create();
            contextManager.set(webDriverContext);
        }
        return webDriverContext;
    }

    public void clearContext() {
        log.debug("[WebDriverContextHolder] Clear context");
        contextManager.remove();
    }

}
