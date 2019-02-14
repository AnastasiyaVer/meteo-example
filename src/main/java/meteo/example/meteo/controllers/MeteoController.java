package meteo.example.meteo.controllers;

import meteo.example.meteo.MeteoApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MeteoController {

    private static final Logger log = LoggerFactory.getLogger(MeteoController.class);
    private RabbitTemplate rabbitTemplate;

    public MeteoController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public String mainPage(){
        return "main";
    }

    @PostMapping("service1")
    public String addDataOne(@RequestParam String temperature, @RequestParam String windSpeed){
        Map<String,String>actionmap = new HashMap<>();
        actionmap.put("s1_temperature", temperature);
        actionmap.put("s1_windSpeed",windSpeed);
        log.info("Sending service1 data request through queue message");
        rabbitTemplate.convertAndSend(MeteoApplication.MESSAGE_QUEUE, actionmap);
        return "main";
    }

    @PostMapping("service2")
    public String addDataTwo(@RequestParam String temperature, @RequestParam String pressure){
        Map<String,String>actionmap = new HashMap<>();
        actionmap.put("s2_temperature", temperature);
        actionmap.put("s2_pressure",pressure);
        log.info("Sending service2 data request through queue message");
        rabbitTemplate.convertAndSend(MeteoApplication.MESSAGE_QUEUE, actionmap);
        return "main";
    }

    @PostMapping("service3")
    public String addDataThree(@RequestParam String humidity, @RequestParam String windSpeed,
                               @RequestParam String directionWind) {
        Map<String, String> actionmap = new HashMap<>();
        actionmap.put("s3_humidity", humidity);
        actionmap.put("s3_windSpeed", windSpeed);
        actionmap.put("s3_directionWind", directionWind);
        log.info("Sending service3 data request through queue message");
        rabbitTemplate.convertAndSend(MeteoApplication.MESSAGE_QUEUE, actionmap);
        return "main";
    }
}
