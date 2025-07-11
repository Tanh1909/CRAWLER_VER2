package com.example.crawler.factory.step;

import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.config.crawler.context.crawler.CrawlerContextHolder;
import com.example.crawler.config.selenium.context.WebDriverContext;
import com.example.crawler.data.enums.HTMLParamEnum;
import com.example.crawler.data.enums.SelectorTypeEnum;
import com.example.crawler.data.enums.StepTypeEnum;
import com.example.crawler.data.model.CrawlerError;
import com.example.crawler.utils.WebElementUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

@Log4j2
public abstract class StepAbstract {

    public abstract StepTypeEnum getType();

    public void execute(WebDriverContext webDriverContext, Integer stepNum, StepConfig stepConfig) {
        RemoteWebDriver webDriver = webDriverContext.getWebDriver();
        String sessionId = webDriverContext.getSessionId();
        String stepName = stepConfig.getName();
        StepTypeEnum stepType = stepConfig.getType();
        try {
            log.info("[{}] - [StartStep.{}] Start execute step [name: {}, type: {}]", sessionId, stepNum, stepName, stepType);
            applyDelay(stepConfig, sessionId);
            process(webDriver, stepConfig);
        } catch (Exception e) {
            CrawlerContextHolder.addStepError(new CrawlerError(stepNum, stepName, e.getMessage(), e));
            log.error("[{}] - [ErrorStep.{}] Error when execute step [name: {}, type: {}]", sessionId, stepNum, stepName, stepType);
        } finally {
            log.info("[{}] - [EndStep.{}] End execute step", sessionId, stepNum);
        }
    }

    protected WebElement getWebElement(WebDriver webDriver, StepConfig stepConfig) {
        By by = getBy(stepConfig);
        return WebElementUtils.getWebElementWithRetry(webDriver, by, stepConfig.getTimeOut());
    }

    protected By getBy(StepConfig stepConfig) {
        String selector = stepConfig.getParameter(HTMLParamEnum.SELECTOR.value());
        String selectorType = stepConfig.getParameter(HTMLParamEnum.SELECTOR_TYPE.value(), SelectorTypeEnum.ID.value());
        return WebElementUtils.getBy(selectorType, selector);
    }

    private void applyDelay(StepConfig step, String sessionId) {
        int delaySeconds = step.getDelay();
        if (delaySeconds > 1) {
            try {
                log.debug("[{}] -  Start delay for {} seconds", sessionId, delaySeconds);
                Thread.sleep(Duration.ofSeconds(delaySeconds));
                log.debug("[{}] -  End delay for {} seconds", sessionId, delaySeconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public abstract void process(RemoteWebDriver webDriver, StepConfig stepConfig);

}
