package com.gft.db.shops.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gft.db.shops.app.Constants;
import com.gft.db.shops.dao.ShopsDao;
import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopsException;
import com.google.maps.model.Distance;
import com.google.maps.model.LatLng;

@Service
public class ShopsService {

    private ShopsDao repository;


    private GeocodingService geocodingService;

    @Autowired
    public ShopsService(final ShopsDao dao, final GeocodingService geoService) {
    	this.repository=dao;
    	this.geocodingService=geoService;
    }
    public Shop readShop(final String name) {
        return repository.readShop(name);
    }

    public Set<Shop> readAll() {
        return repository.readAll();
    }

    public ResponseData remove(final Shop shop) throws ShopsException{
    	if(shop == null){
    		throw new ShopsException("No shop to be removed");
    	}
        return repository.removeShop(shop);
    }
    private boolean validateShop(final Shop shop){
    	if(StringUtils.isEmpty(shop.getName())) {
    		return Boolean.FALSE;
    	}
    	return Boolean.TRUE;
    }
    public ResponseData save(final Shop shop) throws ShopsException{
       try{
    	   if(validateShop(shop)){
            final Shop readData = geocodingService.address2LatLng(shop.getShopAddress().getStreet());
            shop.setLatitude(readData.getLatitude());
            shop.setLongitude(readData.getLongitude());
            if (shop.getShopAddress().getNumber() == 0) {
                shop.getShopAddress().setNumber(readData.getShopAddress().getNumber());
            }
            if (shop.getShopAddress().getPostCode() == 0) {
                shop.getShopAddress().setPostCode(readData.getShopAddress().getPostCode());
            }

            return repository.save(shop);
    	   } else {
    		   return new ResponseData(ResponseData.ACTION_ERROR, shop);
    	   }
        } catch(Exception e) {
            throw new ShopsException(Constants.ERROR_MSG);
        }
    }

    public Set<Shop> findNearestShops(final LatLng latLng) {
        final Set<Shop> shops = new HashSet<Shop>();

        for (Shop shop : readAll()) {

            Distance[] distances = geocodingService.checkDistances(latLng,
                    new LatLng(shop.getLatitude(), shop.getLongitude()));
            if (distances.length == 1 && distances[0] == null) {
                break;
            }
            long[] distancesNumbers = new long[distances.length];
            for (int i = 0; i < distances.length; i++) {
                Distance distance = distances[i];
                distancesNumbers[i] = distance.inMeters;
            }
            shop.setDistancesToAnotherPoint(distancesNumbers);
            shops.add(shop);
        }

        return shops;
    }
}
