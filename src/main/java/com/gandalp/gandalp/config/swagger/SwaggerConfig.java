package com.gandalp.gandalp.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Value("${springdoc.server-url}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server().url(serverUrl);

        return new OpenAPI()
            .addServersItem(server)
            .info(new Info()
                .title("Gandalp RESTful API Documentation")
                .version("1.0")
                .description("API 명세서"));
    }
}
