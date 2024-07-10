package com.halisson.iadoc_document;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MicroserviceDocumentProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceDocumentProcessorApplication.class, args);
	}

}
