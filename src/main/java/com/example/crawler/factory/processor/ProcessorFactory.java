package com.example.crawler.factory.processor;

import com.example.crawler.data.enums.ProcessorTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessorFactory {

    private final List<ProcessorAbstract> processors;

    public ProcessorAbstract getProcessor(String type) {
        ProcessorTypeEnum processorTypeEnum = ProcessorTypeEnum.from(type);
        for (ProcessorAbstract processor : processors) {
            if (processor.getType().equals(processorTypeEnum)) {
                return processor;
            }
        }
        throw new UnsupportedOperationException("Processor type hasn't been supported yet: " + type);
    }


}
