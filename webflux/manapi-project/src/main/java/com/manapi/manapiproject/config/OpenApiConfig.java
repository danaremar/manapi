package com.manapi.manapiproject.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    public OpenAPI addInfo() {
        return new OpenAPI()
                .info(new Info().title("MANAPI Gateway Service")
                        .description("Contains authentication, authorization & connection with feature microservices")
                        .version("v0.0.1")
                        .license(new License().name("GPLv3")
                                .url("https://danaremar.github.io/iMan/login")))
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Github")
                                .url("https://github.com/danaremar/iMan"));
    }

}
