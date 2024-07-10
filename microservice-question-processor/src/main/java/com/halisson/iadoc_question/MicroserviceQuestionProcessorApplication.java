package com.halisson.iadoc_question;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MicroserviceQuestionProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceQuestionProcessorApplication.class, args);
	}

}
