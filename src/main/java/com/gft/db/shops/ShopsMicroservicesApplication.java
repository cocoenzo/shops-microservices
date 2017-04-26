package com.gft.db.shops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class ShopsMicroservicesApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ShopsMicroservicesApplication.class, args);
    }

    /**
     * Used as a WAR
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShopsMicroservicesApplication.class);
    }

}
