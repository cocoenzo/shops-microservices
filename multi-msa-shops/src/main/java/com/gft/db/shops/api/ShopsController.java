package com.gft.db.shops.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gft.db.shops.app.Constants;
import com.gft.db.shops.data.Person;
import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopCommand;
import com.gft.db.shops.data.ShopsException;
import com.gft.db.shops.service.ShopsService;
import com.google.maps.model.LatLng;

import lombok.extern.slf4j.Slf4j;

/**
 * API for the Shops service.<br/>
 * It is defined all the available method for the client and implemented with
 * RESTFull Service. They are:
 * <ul>
 * <li>Read all shops, with URI http://host/shops/ form</li>
 * <li>Read a shop, with URI [GET] http://host/shops/{name} form</li>
 * <li>Read the nearest shop by one Lat and Lng, with URI [GET]
 * http://host/shops/?lat=xxx&lng=xxx form</li>
 * <li>Save a shop, with URI [POST] http://host/shops/ form</li>
 * <li>Remove a shop, with URI [DELETE] http://host/shops/{name}</li>
 * </ul>
 * For the list it is used the new forEach in the iterator method to provide the
 * link.<br/>
 * In the save method it is validated the input data, due to avoid null values
 * (name and address) or empty and it is also validated in the Service Layer.
 * 
 * @author Ignacio Elorriaga
 * @version 1.0
 * @since 1.0
 */
@ExposesResourceFor(Shop.class)
@RestController
@RequestMapping(value = "/shops", produces = "application/json")
@Slf4j
public class ShopsController {


    private final ShopsService service;

    @Autowired
    public ShopsController(final ShopsService service) {
        this.service = service;
    }

    private void addSelfLink(final Shop resource) {
        if (resource.getLinks().size() == 0) {
            resource.add(linkTo(methodOn(ShopsController.class).readShop(resource.getName())).withRel("self"));
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{name}")
    public @ResponseBody ResponseData readShop(@PathVariable("name") final String name) {
        log.info("Returning the shop" + name);
        String message = "";
        Shop shop = new Shop();
        if (StringUtils.isEmpty(name)) {
            log.error("No selected input parameter name");
            message = ResponseData.ACTION_ERROR;
        } else {
            shop = service.readShop(name);
            message = ResponseData.ACTION_NOT_FOUND;
            if (!StringUtils.isEmpty(shop.getName())) {
                addSelfLink(shop);
                message = Constants.ACTION_READ_MSG;
            }
        }
        return new ResponseData(convertToReadableMsg(message), shop);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/", params = { "lat", "lng" })
    public Shop findNearestShops(@RequestParam(name = "lat") final double lat,
            @RequestParam(name = "lng") final double lng) {
        log.info("Looking for the nearest shop from lat=" + lat + ", lng=" + lng);
        final LatLng latLngObject = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        final Shop shop = service.findNearestShops(latLngObject);
        if (!StringUtils.isEmpty(shop.getName())) {
            addSelfLink(shop);
        }
        log.info("Shop found: " + shop);
        return shop;

    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public Set<Shop> readAll() {
        log.info("Returning all shops");
        Set<Shop> shops = service.readAll();
        shops.forEach(new Consumer<Shop>() {
            @Override
            public void accept(final Shop shop) {
                addSelfLink(shop);
            }
        });
        return shops;
    }

    /**
     * Stores a shop in the database.<br/>
     * It checks firstly, that the mandatory params are valid and then the
     * service is executed.
     * 
     * @param shopCommand
     *            with the values from the input data.
     * @return the result of the operation.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public @ResponseBody ResponseData save(final ShopCommand shopCommand) {
        String message = "";
        Shop shop = new Shop();
        try {
            shop = shopCommand.convertToShop();
            if (validateShop(shop)) {
                log.info("Saving the shop: " + shop);
                ResponseData response = service.save(shop);
                response.setResult(convertToReadableMsg(response.getResult()));
                addSelfLink(shop);
                return response;
            } else {
                message = Constants.ACTION_INVALID_PARAMS;
            }
        } catch (ShopsException e) {
            log.error("There was an error while saving: " + shop, e);
            message = Constants.ERROR_MSG;
        }
        return new ResponseData(message, shop);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{name}")
    public @ResponseBody ResponseData remove(@PathVariable final String name) {
        log.info("Removing the shop: " + name);
        ResponseData response = new ResponseData();
        try {
            response = service.remove(name);
        } catch (ShopsException e) {
            log.error("Error removing the shop=" + name, e);
            response = new ResponseData(ResponseData.ACTION_ERROR, new Shop());
        }
        response.setResult(convertToReadableMsg(response.getResult()));
        return response;

    }

    @RequestMapping(method = RequestMethod.GET, path="/managers")
    public Set<Person> readManagers() {
    	return service.readManagers();
    }
    @RequestMapping(method = RequestMethod.POST, path = "/addManager/")
    public @ResponseBody ResponseData includeManager(final String name, final String manager) throws ShopsException{
    	final Shop shop = service.readShop(name);
    	final Person person = service.readManager(manager);
    	shop.setManager(person.getName()+" "+ person.getSurname());
    	return service.save(shop);
    }
    /**
     * Checks the PK of the object.<br/>
     * It checks the object is not null and contains a name.
     * 
     * @param shop
     *            to be validated
     * @return true if it is valid, false otherwise
     */
    private boolean validateShop(final Shop shop) {
        boolean isValid = Boolean.TRUE;

        if (StringUtils.isEmpty(shop.getName())) {
            isValid = Boolean.FALSE;
        }
        if (shop.getShopAddress() == null) {
            isValid = Boolean.FALSE;
        } else {
            if (StringUtils.isEmpty(shop.getShopAddress().getStreet())) {
                isValid = Boolean.FALSE;
            }
        }

        return isValid;
    }

    /**
     * Changes the result message to more readable message for the user.<br/>
     * 
     * @param response
     * @return
     */
    private String convertToReadableMsg(String messageFromServer) {
        String message = messageFromServer;
        switch (messageFromServer) {
        case ResponseData.ACTION_ERROR:
            message = Constants.ERROR_MSG;
            break;
        case ResponseData.ACTION_NEW:
            message = Constants.ACTION_CREATED_MSG;
            break;
        case ResponseData.ACTION_UPDATE:
            message = Constants.ACTION_UPDATED_MSG;
            break;
        case ResponseData.ACTION_REMOVE:
            message = Constants.ACTION_REMOVE_MSG;
            break;
        case ResponseData.ACTION_NOT_FOUND:
            message = Constants.ACTION_NOT_FOUND;
            break;

        }
        return message;
    }

    @ExceptionHandler
    public ModelAndView handleRequest(HttpRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("error", "Internal Error");
        return new ModelAndView("index", map);
    }
}
