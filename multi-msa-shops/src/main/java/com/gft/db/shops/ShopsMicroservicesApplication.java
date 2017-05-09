package com.gft.db.shops;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.gft.db.shops.dao.ShopsDao;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopAddress;

@EnableAutoConfiguration
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class ShopsMicroservicesApplication extends SpringBootServletInitializer {

	@Autowired
	private ShopsDao dao;
	
    public static void main(String[] args) {
        SpringApplication.run(ShopsMicroservicesApplication.class, args);
    }

    @PostConstruct
    private void createDefaults() {
    	final Shop shop = Shop.builder().name("Test MSA 1").shopAddress(new ShopAddress(1, 50002, "Plaza Antonio beltran martinez 1, Zaragoza")).build();
    	dao.save(shop);
    }
//    /**
//     * Used as a WAR
//     */
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(ShopsMicroservicesApplication.class);
//    }

}
