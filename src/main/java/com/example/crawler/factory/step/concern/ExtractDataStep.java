package com.example.crawler.factory.step.concern;

import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.config.crawler.context.CrawlerContext;
import com.example.crawler.config.crawler.field.FieldConfig;
import com.example.crawler.config.crawler.field.FieldProcessorConfig;
import com.example.crawler.config.selenium.WebDriverContext;
import com.example.crawler.data.enums.ParameterEnum;
import com.example.crawler.data.enums.StepType;
import com.example.crawler.factory.step.StepAbstract;
import com.example.crawler.service.field.FieldProcessor;
import com.example.crawler.utils.WebElementUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class ExtractDataStep extends StepAbstract {

    private final FieldProcessor fieldProcessor;

    private final ObjectMapper objectMapper;

    @Override
    public StepType getType() {
        return StepType.EXTRACT_DATA;
    }

    @Override
    public void internalExecute(WebDriverContext context, StepConfig stepConfig, CrawlerContext crawlerContext) {
        RemoteWebDriver webDriver = context.getWebDriver();
        List<FieldConfig> fieldConfigs = stepConfig.getFields();
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (FieldConfig fieldConfig : fieldConfigs) {
            String fieldName = fieldConfig.getFieldName();
            String selector = fieldConfig.getParameter(ParameterEnum.SELECTOR.value());
            String selectorType = fieldConfig.getParameter(ParameterEnum.SELECTOR_TYPE.value());
            By by = WebElementUtils.getBy(selectorType, selector);
            WebElement webElement = WebElementUtils.getWebElementVisibility(webDriver, by);
            String rawText = "";
            List<FieldProcessorConfig> processors = fieldConfig.getProcessors();
            Object rawValue = "";
            for (FieldProcessorConfig processorConfig : processors) {
                rawValue = fieldProcessor.process(rawValue, processorConfig);
            }
            Object objectValue = fieldProcessor.convertFieldType(rawValue, fieldConfig);
            if (fieldConfig.isGlobal()) {
                crawlerContext.addGlobalField(fieldName, objectValue);
            } else {
                objectNode.set(fieldName, objectMapper.valueToTree(objectValue));
            }
        }
        crawlerContext.addExtractedData(objectNode);

    }
}
