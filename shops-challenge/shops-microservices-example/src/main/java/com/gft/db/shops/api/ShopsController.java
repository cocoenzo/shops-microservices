package com.gft.db.shops.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.server.PathParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
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
import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopCommand;
import com.gft.db.shops.data.ShopsException;
import com.gft.db.shops.service.ShopsService;
import com.google.maps.model.LatLng;

@ExposesResourceFor(Shop.class)
@RestController
@RequestMapping(value="/shops", produces = "application/json")
public class ShopsController {

	

	private final Logger log = Logger.getLogger(ShopsController.class);

	private ShopsService service;
    @Autowired
    public ShopsController(final ShopsService service){
    	this.service=service;
    }
//    @Autowired
//    private EntityLinks entityLinks;
    
    private void addSelfLink(final Shop resource){
        final Shop shop = methodOn(ShopsController.class).readShop(resource.getName()).getShop();
        final Link link = linkTo(shop).withSelfRel();
        resource.add(link);
    }
    
    @RequestMapping(method=RequestMethod.GET, path="/read/{name}")
    public @ResponseBody ResponseData readShop(@PathVariable("name") final String name) {
        log.info("Returning the shop" + name);
        if(org.springframework.util.StringUtils.isEmpty(name)){
        	log.error("No selected input parameter name");
        } else {
        	final Shop shop = service.readShop(name);
        	
        	addSelfLink(shop);
        	return new ResponseData(Constants.ACTION_READ_MSG, shop);
        }
        return new ResponseData(ResponseData.ACTION_ERROR, new Shop());
    }

    @RequestMapping("/")
    public Set<Shop> readAll() {
        log.info("Returning all shops");
        return service.readAll();
    }

    @RequestMapping(method=RequestMethod.POST, path="/save")
    public @ResponseBody ResponseData save(final ShopCommand shopCommand) {
    	String message = "";
    	Shop shop = new Shop();
    	try {
        	shop =shopCommand.convertToShop();
        	if(validateShop(shop)){
	        	ResponseData response = service.save(shop);
	        	message = convertToReadableMsg(response);
        	} else {
        		message = "Invalid input parameters. Street and Name are mandatories.";
        	}
		} catch (ShopsException e) {
			message = Constants.ERROR_MSG;
		}
        return new ResponseData(message, shop);
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
        if(shop.getShopAddress() == null){
        	isValid = Boolean.FALSE;
        } else {
        	if(StringUtils.isEmpty(shop.getShopAddress().getStreet())){
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
	private String convertToReadableMsg( ResponseData response) {
		String message = "";
		switch (response.getResult()) {
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
		}
		return message;
	}

    

    @RequestMapping("/find/")
    public Set<Shop> findNearestShops(@RequestParam(name = "lat") final double lat,
            @RequestParam(name = "lng") final double lng) {
        final Set<Shop> shops = new HashSet<Shop>();
        final LatLng latLngObject = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        shops.addAll(service.findNearestShops(latLngObject));
        return shops;

    }
    
    @ExceptionHandler
    public ModelAndView handleRequest(HttpRequest request) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("error", "Internal Error");
    	return new ModelAndView("index", map);
    }
}
