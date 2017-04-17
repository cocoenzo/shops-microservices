package com.gft.db.shops.client.service;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.gft.db.shops.client.data.ResponseData;
import com.gft.db.shops.client.data.Shop;

@Service
public class ShopsClientService {

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    protected String serviceUrl;

    protected Logger logger = Logger.getLogger(ShopsClientService.class);

    public ShopsClientService(final String serviceUrl) {
        this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
    }

    @PostConstruct
    public void demoOnly() {

        logger.warn("The RestTemplate request factory is " + restTemplate.getRequestFactory());
    }

    public Shop readShop(final String name) {

        logger.info("greeting() invoked: for " + name);

        final ModelAndView shops = restTemplate.getForObject(serviceUrl + "read/{name}", ModelAndView.class, name);
        final Shop shop = (Shop)shops.getModel().get("shop");
        return shop;

    }

    public ResponseData save(final Shop shop) {
        final String params = "name=" + shop.getName() + "&street=" + shop.getShopAddress().getStreet()
                + "&postalCode=" + shop.getShopAddress().getPostCode() + "&number=" + shop.getShopAddress().getNumber();
        final ResponseData response = restTemplate.getForObject(serviceUrl + "save/?" + params, ResponseData.class);
        return response;

    }

    public Set<Shop> findNearest(final double lat, final double lng) {
        final Set<Shop> shops = restTemplate.getForObject(serviceUrl + "find/?lat=" + lat + "&lng=" + lng, Set.class);
        return shops;
    }
}
