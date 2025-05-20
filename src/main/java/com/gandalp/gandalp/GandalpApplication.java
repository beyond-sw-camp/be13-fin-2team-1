package com.gandalp.gandalp;

import com.gandalp.gandalp.openAi.OpenAIProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(OpenAIProperties.class)
@EnableRedisRepositories()
public class GandalpApplication {

	public static void main(String[] args) {
		SpringApplication.run(GandalpApplication.class, args);
	}

}
