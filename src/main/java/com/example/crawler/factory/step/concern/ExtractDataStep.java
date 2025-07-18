package com.example.crawler.factory.step.concern;

import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.config.crawler.context.crawler.CrawlerContextHolder;
import com.example.crawler.config.crawler.field.FieldConfig;
import com.example.crawler.config.crawler.field.FieldProcessorConfig;
import com.example.crawler.data.enums.HTMLParamEnum;
import com.example.crawler.data.enums.StepTypeEnum;
import com.example.crawler.factory.step.StepAbstract;
import com.example.crawler.service.field.FieldProcessor;
import com.example.crawler.utils.WebElementUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    public StepTypeEnum getType() {
        return StepTypeEnum.EXTRACT_DATA;
    }

    @SneakyThrows
    @Override
    public void process(RemoteWebDriver webDriver, StepConfig stepConfig) {
        List<FieldConfig> fieldConfigs = stepConfig.getFields();
        ObjectNode objectNode = objectMapper.createObjectNode();
        for (FieldConfig fieldConfig : fieldConfigs) {
            try {
                String fieldName = fieldConfig.getFieldName();
                String selector = fieldConfig.getParameter(HTMLParamEnum.SELECTOR.value());
                String selectorType = fieldConfig.getParameter(HTMLParamEnum.SELECTOR_TYPE.value());
                String attribute = fieldConfig.getParameter(HTMLParamEnum.ATTRIBUTE.value(), null);

                By by = WebElementUtils.getBy(selectorType, selector);
                WebElement webElement = WebElementUtils.getWebElementWithRetry(webDriver, by);
                Object rawValue = attribute != null ? webElement.getAttribute(attribute) : webElement.getText();

                List<FieldProcessorConfig> processors = fieldConfig.getProcessors();
                for (FieldProcessorConfig processorConfig : processors) {
                    rawValue = fieldProcessor.process(rawValue, processorConfig);
                }
                Object objectValue = fieldProcessor.convertFieldType(rawValue, fieldConfig.getFieldType());
                if (fieldConfig.isGlobal()) {
                    CrawlerContextHolder.addGlobalField(fieldName, objectValue);
                } else {
                    objectNode.set(fieldName, objectMapper.valueToTree(objectValue));
                }
            } catch (Exception e) {
                log.error("Error when extract data [{}] . Message: {}", objectMapper.writeValueAsString(fieldConfig), e.getMessage());
            }
        }
        CrawlerContextHolder.addExtractedData(objectNode);
    }
}
