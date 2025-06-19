package com.example.crawler.factory.step.concern;

import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.config.crawler.context.CrawlerContext;
import com.example.crawler.config.selenium.WebDriverContext;
import com.example.crawler.data.enums.ParameterEnum;
import com.example.crawler.data.enums.StepType;
import com.example.crawler.factory.step.StepAbstract;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class GetUrlStep extends StepAbstract {

    @Override
    public StepType getType() {
        return StepType.GET_URL;
    }

    @Override
    public void internalExecute(WebDriverContext context, StepConfig stepConfig, CrawlerContext crawlerContext) {
        RemoteWebDriver webDriver = context.getWebDriver();
        String urlParameter = stepConfig.getParameter(ParameterEnum.URL.value());
        webDriver.get(urlParameter);
    }
}
