package com.gft.db.shops.client.controller;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gft.db.shops.client.data.ResponseData;
import com.gft.db.shops.client.data.Shop;
import com.gft.db.shops.client.data.ShopAddress;
import com.gft.db.shops.client.service.ShopsClientService;

@Controller
public class ShopsClientController {

    protected ShopsClientService service;

    protected Logger logger = Logger.getLogger(ShopsClientController.class);

    // constructor
    public ShopsClientController(ShopsClientService service) {
        this.service = service;
    }

    @RequestMapping("/shops")
    public String goHome() {
        return "index";
    }

    @RequestMapping("/read/")
    public String readShop(Model model, @RequestParam(name = "name") String name) {

        logger.info("shops-service readShop() invoked: " + name);
        Shop shop = service.readShop(name);
        model.addAttribute("shop", shop);
        // Greeting greeting = helloWorldService.greeting(name);

        // logger.info("helloWorld-service greeting() found: " +
        // greeting.getContent());

        // model.addAttribute("greeting", greeting.getContent());

        return "shop";

    }

    @RequestMapping("/find/")
    public String findNearest(Model model, @RequestParam("latitude") final String latitude,
            @RequestParam("longitude") final String longitude) {
        Set<Shop> shops = service.findNearest(Double.valueOf(latitude), Double.valueOf(longitude));
        model.addAttribute("shops", shops);

        return "shop";
    }

    @RequestMapping("/save")
    public String save(Model model, @RequestParam(name = "name") final String name,
            @RequestParam(name = "street") final String street,
            @RequestParam(name = "postalCode") final int postalCode, @RequestParam(name = "number") final int number) {
        final Shop shop = new Shop();
        shop.setName(name);
        shop.setShopAddress(new ShopAddress(street, number, postalCode));
        ResponseData response = service.save(shop);
        model.addAttribute("response", response);
        model.addAttribute("shop", response.getShop());
        model.addAttribute("shops", new HashSet<Shop>());
        return "shop";
    }
}
