package com.gft.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MultiMsaEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiMsaEurekaApplication.class, args);
	}
}
