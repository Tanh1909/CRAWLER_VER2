package com.example.crawler.factory.step.concern;

import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.config.crawler.context.CrawlerContext;
import com.example.crawler.config.selenium.WebDriverContext;
import com.example.crawler.data.enums.HTMLParamEnum;
import com.example.crawler.data.enums.StepTypeEnum;
import com.example.crawler.factory.step.StepAbstract;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SendKeyStep extends StepAbstract {

    @Override
    public StepTypeEnum getType() {
        return StepTypeEnum.SEND_KEY;
    }

    @Override
    public void process(WebDriverContext context, StepConfig stepConfig, CrawlerContext crawlerContext) {
        RemoteWebDriver webDriver = context.getWebDriver();
        WebElement webElement = getWebElement(webDriver, stepConfig);
        String value = stepConfig.getParameter(HTMLParamEnum.VALUE.value());
        webElement.sendKeys(value);
    }
}
