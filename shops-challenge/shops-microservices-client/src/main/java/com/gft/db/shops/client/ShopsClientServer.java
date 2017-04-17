package com.gft.db.shops.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.gft.db.shops.client.controller.ShopsClientController;
import com.gft.db.shops.client.controller.ShopsClientIndexController;
import com.gft.db.shops.client.service.ShopsClientService;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false, basePackages = { "com.gft.db.shops" })
public class ShopsClientServer {

    public static final String SERVICE_URL = "http://shops-service/";

    public static void main(String[] args) {

        SpringApplication.run(ShopsClientServer.class, args);
    }

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ShopsClientService helloWorldService() {
        return new ShopsClientService(SERVICE_URL);
    }

    @Bean
    public ShopsClientController helloWorldController() {
        return new ShopsClientController(helloWorldService());
    }

    @Bean
    public ShopsClientIndexController homeController() {
        return new ShopsClientIndexController();
    }
}
