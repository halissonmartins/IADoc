package com.halisson.iadoc_discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MicroserviceDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceDiscoveryApplication.class, args);
	}

}
