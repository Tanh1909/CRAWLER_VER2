package com.example.crawler.factory.step;

import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.config.crawler.context.CrawlerContext;
import com.example.crawler.config.selenium.WebDriverContext;
import com.example.crawler.data.enums.ParameterEnum;
import com.example.crawler.data.enums.SelectorType;
import com.example.crawler.data.enums.StepType;
import com.example.crawler.data.model.CrawlerError;
import com.example.crawler.utils.WebElementUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;

@Log4j2
public abstract class StepAbstract {

    public abstract StepType getType();

    public void execute(Integer stepNum, WebDriverContext webDriverContext, StepConfig stepConfig, CrawlerContext crawlerContext) {
        String sessionId = webDriverContext.getSessionId();
        String stepName = stepConfig.getName();
        StepType stepType = stepConfig.getType();
        log.info("[{}] - [stepNum.{}] Start execute step [name: {}, type: {}]", sessionId, stepNum, stepName, stepType);
        try {
            internalExecute(webDriverContext, stepConfig, crawlerContext);
            applyDelay(stepConfig);
        } catch (Exception e) {
            crawlerContext.addStepError(new CrawlerError(stepNum, stepName, e.getMessage(), e));
            log.error("[{}] - [stepNum.{}] Error when execute step [name: {}, type: {}]", sessionId, stepNum, stepName, stepType);
        }
        log.info("[{}] - [stepNum.{}] End execute step [name: {}, type: {}]", sessionId, stepNum, stepName, stepType);
    }

    protected WebElement getWebElement(WebDriver webDriver, StepConfig stepConfig) {
        By by = getBy(stepConfig);
        return WebElementUtils.getWebElementVisibility(webDriver, by, stepConfig.getTimeOut());
    }

    protected By getBy(StepConfig stepConfig) {
        String selector = stepConfig.getParameter(ParameterEnum.SELECTOR.value());
        String selectorType = stepConfig.getParameter(ParameterEnum.SELECTOR_TYPE.value(), SelectorType.ID.value());
        return WebElementUtils.getBy(selectorType, selector);
    }

    private void applyDelay(StepConfig step) {
        int delaySeconds = step.getDelay();
        if (delaySeconds > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(delaySeconds));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public abstract void internalExecute(WebDriverContext context, StepConfig stepConfig, CrawlerContext crawlerContext);

}
