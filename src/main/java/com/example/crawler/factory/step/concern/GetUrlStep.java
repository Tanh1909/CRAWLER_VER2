package com.example.crawler.factory.step.concern;

import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.data.enums.HTMLParamEnum;
import com.example.crawler.data.enums.StepTypeEnum;
import com.example.crawler.factory.step.StepAbstract;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class GetUrlStep extends StepAbstract {

    @Override
    public StepTypeEnum getType() {
        return StepTypeEnum.GET_URL;
    }

    @Override
    public void process(RemoteWebDriver webDriver, StepConfig stepConfig) {
        String urlParameter = stepConfig.getParameter(HTMLParamEnum.URL.value());
        webDriver.get(urlParameter);
    }
}
