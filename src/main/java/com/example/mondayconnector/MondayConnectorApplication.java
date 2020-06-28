package com.example.mondayconnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MondayConnectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MondayConnectorApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "app.monday")
	public MondaySource mondaySource() {
		return new MondaySource();
	}
}
