package com.example.crawler;

import com.example.crawler.config.crawler.CrawlerConfig;
import com.example.crawler.config.crawler.StepConfig;
import com.example.crawler.config.crawler.context.devtools.DevtoolsContext;
import com.example.crawler.config.crawler.context.devtools.DevtoolsContextHolder;
import com.example.crawler.config.crawler.processor.ProcessorConfig;
import com.example.crawler.config.selenium.context.WebDriverContext;
import com.example.crawler.config.selenium.context.WebDriverContextHolder;
import com.example.crawler.data.model.NetworkModel;
import com.example.crawler.factory.processor.ProcessorFactory;
import com.example.crawler.factory.step.StepAbstract;
import com.example.crawler.factory.step.StepFactory;
import com.example.crawler.json_transform.model.JsonParserConfig;
import com.example.crawler.json_transform.processor.JsonParserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties
public class CrawlerApplication {


    @Autowired
    private CrawlerConfig config;

    @Autowired
    private StepFactory stepFactory;

    @Autowired
    private WebDriverContextHolder webDriverContextHolder;

    @Autowired
    private JsonParserProcessor jsonParserProcessor;

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private JsonParserConfig jsonConfig;

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            try (WebDriverContext webDriverContext = webDriverContextHolder.getContext()) {
                List<StepConfig> steps = config.getSteps();
                int i = 0;
                for (StepConfig step : steps) {
                    i++;
                    StepAbstract stepAbstract = stepFactory.getStep(step.getType().value());
                    stepAbstract.execute(webDriverContext, i, step);
                }
                List<ProcessorConfig> processors = config.getProcessors();
                for (ProcessorConfig processor : processors) {
                    processorFactory.getProcessor(processor.getType().value())
                            .execute(processor);
                }
                DevtoolsContext context = DevtoolsContextHolder.getContext();
                System.out.println(context);
            }


//            String json = "{\n" +
//                    "  \"cam_id\": 702,\n" +
//                    "  \"type\": \"opposite_vehicle\",\n" +
//                    "  \"data\": [\n" +
//                    "    {\n" +
//                    "      \"id\": \"e5ee2cd0-9081-5658-a85a-3a620ba183a8\",\n" +
//                    "      \"time_in_opposite_direction\": 1.7507373165553272E9,\n" +
//                    "      \"opposite_vehicle_image\": \"http://qmh1.cameraddns.net:18083/sc/sources/opposite_vehicles/e5ee2cd0-9081-5658-a85a-3a620ba183a8.jpg\",\n" +
//                    "      \"video_url\": \"http://qmh1.cameraddns.net:18083/sc/sources/videos/2025-06-24/1750737316_192.168.3.254_8102_3002.avi\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "      \"id\": \"2a7c0ee9-4417-50d6-8098-6a27bb885dd0\",\n" +
//                    "      \"time_in_opposite_direction\": 1.750774391921356E9,\n" +
//                    "      \"opposite_vehicle_image\": \"http://qmh1.cameraddns.net:18083/sc/sources/opposite_vehicles/2a7c0ee9-4417-50d6-8098-6a27bb885dd0.jpg\",\n" +
//                    "      \"video_url\": \"http://qmh1.cameraddns.net:18083/sc/sources/videos/2025-06-24/1750774392_192.168.3.254_8102_3002.avi\"\n" +
//                    "    }\n" +
//                    "  ]\n" +
//                    "}\n";
//            System.out.println(jsonConfig);
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(json);
//            JsonNode process = jsonParserProcessor.process(root, jsonConfig);
        };
    }


}
