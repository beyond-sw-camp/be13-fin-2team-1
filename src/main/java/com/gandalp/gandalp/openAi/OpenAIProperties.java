package com.gandalp.gandalp.openAi;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAIProperties {
    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
