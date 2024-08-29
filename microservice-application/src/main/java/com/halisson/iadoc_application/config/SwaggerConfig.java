package com.halisson.iadoc_application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI openAPI() {
		
		return new OpenAPI()
				.info(new Info()
						.title("Document and question processor using AI")
						.description("The application is responsible for uploading PDF documents and registering questions about them through the REST API.")
						.version("0.0.1-SNAPSHOT")
					);
	}
}
