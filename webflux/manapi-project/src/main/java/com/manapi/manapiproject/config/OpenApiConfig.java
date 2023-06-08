package com.manapi.manapiproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI addInfo() {
                return new OpenAPI()
                                .info(new Info().title("MANAPI Project Service")
                                                .version("v0.1")
                                                .license(new License().name("GPLv3")
                                                                .url("https://danaremar.github.io/iMan/login")));
        }
}
