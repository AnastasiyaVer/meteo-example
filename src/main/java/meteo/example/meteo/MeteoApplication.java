package meteo.example.meteo;

import meteo.example.meteo.listener.DataMessageListener;
import meteo.example.meteo.meteo_services.Weather;
import meteo.example.meteo.meteo_services.WeatherService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

@Configuration
@EnableAsync
@EnableAutoConfiguration
@ComponentScan
public class MeteoApplication implements CommandLineRunner {

	public final static String MESSAGE_QUEUE = "data-message-queue";

	@Bean
	Queue queue() {
		return new Queue(MESSAGE_QUEUE, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("my-topic-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(MESSAGE_QUEUE);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(MESSAGE_QUEUE);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(DataMessageListener receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Autowired
	WeatherService weatherService;

	@Override
	public void run(String... args) throws Exception{
		Scanner in = new Scanner(System.in);
		System.out.print("Please, enter the number of services: ");
		int m = in.nextInt();
		in.close();
		Future<Weather>[] weathers = new Future[m];

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				for(int i=0;i<m;i++){
					try {
						weathers[i] = weatherService.sendWeather(new Weather(-50+(int)(Math.random()*101),(int)(Math.random()*31)));
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, 1000, 30*60*1000);

	}

	public static void main(String[] args) {
		SpringApplication.run(MeteoApplication.class, args);
	}

}

