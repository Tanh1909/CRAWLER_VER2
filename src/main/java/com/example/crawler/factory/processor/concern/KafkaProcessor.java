package com.example.crawler.factory.processor.concern;

import com.example.crawler.config.crawler.context.crawler.CrawlerContextHolder;
import com.example.crawler.config.crawler.processor.ProcessorConfig;
import com.example.crawler.data.enums.ProcessorParamEnum;
import com.example.crawler.data.enums.ProcessorTypeEnum;
import com.example.crawler.factory.processor.ProcessorAbstract;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class KafkaProcessor extends ProcessorAbstract {

    private final ObjectMapper objectMapper;

    @Override
    public ProcessorTypeEnum getType() {
        return ProcessorTypeEnum.KAFKA;
    }

    @Override
    protected void process(ProcessorConfig processorConfig) {
        String topic = processorConfig.getParam(ProcessorParamEnum.TOPIC.value());
        String key = processorConfig.getParam(ProcessorParamEnum.KEY.value(), null);
        List<JsonNode> result = CrawlerContextHolder.getResult();
        log.info("//PUSH TO KAFKA [topic: {}, key {}] data: {}", topic, key, objectMapper.valueToTree(result));
    }

}
