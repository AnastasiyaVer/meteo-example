package meteo.example.meteo.meteo_services;

import meteo.example.meteo.MeteoApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
@EnableScheduling
public class WeatherService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS");

    @Async
    public Future<Weather> sendWeather(Weather weather) throws InterruptedException {
        Map<String,String> actionmap = new HashMap<>();
        String localDateTime = LocalDateTime.now().format(FORMATTER);
        actionmap.put("Temperature"+localDateTime, String.valueOf(weather.getTemperature()));
        actionmap.put("WindSpeed"+localDateTime,String.valueOf(weather.getWindSpeed()));
        this.rabbitTemplate.convertAndSend(MeteoApplication.MESSAGE_QUEUE, actionmap);
        actionmap.clear();
        Thread.sleep(1000L);
        return null;
    }
}
