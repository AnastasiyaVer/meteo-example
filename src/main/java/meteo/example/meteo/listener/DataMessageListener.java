package meteo.example.meteo.listener;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableScheduling
@Component
public class DataMessageListener {

    @Autowired
    DataSource dataSource;

    private static final Logger log = LogManager.getLogger(DataMessageListener.class);
    Map<String,String> meteoData = new HashMap<>();

    public void receiveMessage(Map<String, String> message) {
        log.info("Received <" + message + ">");
        meteoData.putAll(message);
        List<String>keys = new ArrayList<>();
    }

    String name = "table";

    @Scheduled(cron = "0 0 * * * *")
    public void createTable(){
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
        String localDateTime = LocalDateTime.now().format(FORMATTER);
        String nameTable = name+"_"+localDateTime;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("create table "+nameTable+" (ID serial NOT NULL Primary key, \n" +
                "name_meteo varchar(50), \n" +
                "val varchar (50) )");
        for (String key : meteoData.keySet()) {
            jdbcTemplate.execute("insert into "+nameTable+" (name_meteo, val) values ('"+ key +"', '"+meteoData.get(key)+"');");
        }
        log.info("Message save...");
        meteoData.clear();
    }

}