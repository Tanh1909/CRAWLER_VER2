package com.example.crawler.factory.step.concern;

import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.data.enums.StepTypeEnum;
import com.example.crawler.factory.step.StepAbstract;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ClickStep extends StepAbstract {

    @Override
    public StepTypeEnum getType() {
        return StepTypeEnum.CLICK;
    }

    @Override
    public void process(RemoteWebDriver webDriver, StepConfig stepConfig) {
        getWebElement(webDriver, stepConfig).click();
    }
}
