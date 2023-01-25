package com.manapi.manapigateway.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {
	
	public OpenAPI addInfo() {
		return new OpenAPI()
				.info(new Info().title("IMAN API").description("Agile & security management").version("v0.0.1")
						.license(new License().name("GPLv3").url("https://danaremar.github.io/iMan/login")))
				.externalDocs(
						new ExternalDocumentation().description("Github").url("https://github.com/danaremar/iMan"));
	}

}
