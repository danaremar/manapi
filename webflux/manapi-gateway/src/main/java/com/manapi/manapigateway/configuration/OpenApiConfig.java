package com.manapi.manapigateway.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.parser.OpenAPIV3Parser;

@Configuration
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig implements WebFluxConfigurer {

	@Autowired
	private RouteDefinitionLocator routeDefinitionLocator;

	@Bean
	public OpenAPI customOpenAPI() {

		OpenAPI openAPI = new OpenAPI();

		// MICROSERVICES
		List<RouteDefinition> routeDefinitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();
		for (RouteDefinition r : routeDefinitions) {
			openAPI = mergeOpenAPISpecs(getOpenAPISpec(r.getUri().toString() + "/v3/api-docs"), openAPI);
		}

		// project
		// openAPI = mergeOpenAPISpecs(getOpenAPISpec("http://localhost:8081/v3/api-docs"), openAPI);

		// external example
		// openAPI = mergeOpenAPISpecs(getOpenAPISpec("https://petstore3.swagger.io/api/v3/openapi.json"), openAPI);

		// gateway information
		setInfo(openAPI);

		return openAPI;
	}

	/**
	 * Sets info to show in Gateway
	 * 
	 * @param openAPI
	 */
	public void setInfo(OpenAPI openAPI) {
		openAPI
				.info(new Info().title("manapi docs")
						.description("Agile management application")
						.version("v0.2")
						.license(new License().name("GPLv3")
								.url("https://danaremar.github.io/iMan/login")))
				.externalDocs(
						new ExternalDocumentation()
								.description("Github")
								.url("https://github.com/danaremar/iMan"));
	}

	/**
	 * Request to specification URL
	 * 
	 * @param specificationUrl
	 * @return
	 */
	private OpenAPI getOpenAPISpec(String specificationUrl) {
		WebClient webClient = WebClient.builder().build();
		String specJson = webClient.get().uri(specificationUrl).retrieve().bodyToMono(String.class).block();
		return new OpenAPIV3Parser().readContents(specJson).getOpenAPI();
	}

	/**
	 * Merge both API specs
	 * 
	 * @param target
	 * @param source
	 * @return
	 */
	private OpenAPI mergeOpenAPISpecs(OpenAPI target, OpenAPI source) {
		if (target == null)
			return source;
		if (source == null)
			return target;
		if (target.getPaths() != null && source.getPaths() != null)
			target.getPaths().putAll(source.getPaths());

		if (target.getComponents() != null && source.getComponents() != null) {
			if (target.getComponents().getSchemas() != null && source.getComponents().getSchemas() != null) {
				target.getComponents().getSchemas().putAll(source.getComponents().getSchemas());
			}

			if (target.getComponents().getParameters() != null && source.getComponents().getParameters() != null) {
				target.getComponents().getParameters().putAll(source.getComponents().getParameters());
			}

			if (target.getComponents().getResponses() != null && source.getComponents().getResponses() != null) {
				target.getComponents().getResponses().putAll(source.getComponents().getResponses());
			}

			// add another section to include
		}
		return target;
	}

}
