package com.manapi.manapigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ManapiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManapiGatewayApplication.class, args);
	}

	@Value("${manapi.resource.images}")
	private String imageUrl;

	@Value("${manapi.frontend.url}")
	private String frontEndUrl;

	@Bean
	public WebMvcConfigurer configurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/images/**")
						.addResourceLocations("file:" + imageUrl);
			}

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")

						// WEB RESTRICTED
						.allowedOrigins("http://localhost:4200", "http://localhost:8080", "https://danaremar.github.io",
								"https://web-danaremar.cloud.okteto.net");

			}
		};
	}

}
