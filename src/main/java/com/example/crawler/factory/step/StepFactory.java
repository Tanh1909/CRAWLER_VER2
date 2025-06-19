package com.example.crawler.factory.step;

import com.example.crawler.data.enums.StepTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StepFactory {

    private final List<StepAbstract> stepAbstracts;

    public StepAbstract getStep(String stepType) {
        StepTypeEnum stepTypeEnum = StepTypeEnum.from(stepType);
        for (StepAbstract stepAbstract : stepAbstracts) {
            if (stepAbstract.getType().equals(stepTypeEnum)) {
                return stepAbstract;
            }
        }
        throw new UnsupportedOperationException("Step type hasn't been supported yet: " + stepType);
    }

}
