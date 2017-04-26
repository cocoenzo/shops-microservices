package com.gft.db.shops.service;

import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gft.db.shops.app.Constants;
import com.gft.db.shops.dao.ShopsDao;
import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopsException;
import com.google.maps.model.LatLng;

/**
 * Service layer in the model part. <br/>
 * Contains all the method for the logic to save/update, read and find
 * shops.<br/>
 * 
 * When the user saves any new / update shop, the process will retrieve from the
 * Street name its coordinates and this process is defined in a sync. process
 * because it was tested that it takes few seconds, and there is not lag time
 * for the user to be divided into a async process.
 * 
 * @author Ignacio Elorriaga
 * @version 1.0
 * @since 1.0
 * 
 */
@Service
public class ShopsService {

    private final ShopsDao repository;

    private final GeocodingService geocodingService;

    @Autowired
    public ShopsService(final ShopsDao dao, final GeocodingService geoService) {
        this.repository = dao;
        this.geocodingService = geoService;
    }

    public Shop readShop(final String name) {
        return repository.readShop(name);
    }

    public Set<Shop> readAll() {
        return repository.readAll();
    }

    public ResponseData remove(final String shopName) throws ShopsException {
        if (StringUtils.isEmpty(shopName)) {
            throw new ShopsException("No shop to be removed");
        }
        return repository.removeShop(shopName);
    }

    private boolean validateShop(final Shop shop) {
        if (StringUtils.isEmpty(shop.getName())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Shop includeCoordinates(final Shop shop) {
        final Shop saved = repository.readShop(shop.getName());
        if (saved.compare(saved, shop) == 0) {
            saved.setLatitude(shop.getLatitude());
            saved.setLongitude(shop.getLongitude());
            repository.save(shop).getShop();
        }
        return saved;

    }

    /**
     * Stores the new or updated element and then, it reads its latitude and
     * longitude coordinates in an async. mode with the {@linkplain Future}.
     * 
     * @param shop
     *            to be stored.
     * @return the Response with the message of the operation and the object
     *         stored.
     * @throws ShopsException
     *             in case any exception.
     */
    public ResponseData save(final Shop shop) throws ShopsException {
        try {
            if (validateShop(shop)) {
                ResponseData response = repository.save(shop);
                geocodingService.obtainLatittudeLongitude(shop).thenAccept(this::includeCoordinates);
                return response;
            } else {
                return new ResponseData(ResponseData.ACTION_ERROR, shop);
            }
        } catch (Exception e) {
            throw new ShopsException(Constants.ERROR_MSG);
        }
    }

    /**
     * Determines which is the nearest shop from the specified latitude and
     * longitude.<br/>
     * It is calculated with a Math function.
     * 
     * @param latLng
     *            as a init point.
     * @return the result shop.
     */
    public Shop findNearestShops(final LatLng latLng) {
        Shop nearestShop = new Shop();
        double dists = Double.MAX_VALUE;
        nearestShop.setDistancesToAnotherPoint(dists);
        for (Shop shop : readAll()) {

            double distanceKms = GeocodingService.checkDistances(latLng,
                    new LatLng(shop.getLatitude(), shop.getLongitude()));

            shop.setDistancesToAnotherPoint(distanceKms);
            if (shop.getDistancesToAnotherPoint() < nearestShop.getDistancesToAnotherPoint()) {
                nearestShop = shop;
            }
        }

        return nearestShop;
    }
}
