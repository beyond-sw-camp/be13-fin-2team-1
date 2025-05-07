package com.gandalp.gandalp;

import com.gandalp.gandalp.openAi.OpenAIProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(OpenAIProperties.class)
public class GandalpApplication {

	public static void main(String[] args) {
		SpringApplication.run(GandalpApplication.class, args);
	}

}
