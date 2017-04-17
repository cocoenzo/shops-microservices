package com.gft.db.shops.service;

import java.io.IOException;

import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gft.db.shops.app.Constants;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopAddress;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

@Service
public class GeocodingService {

    private static final Logger LOG = LoggerFactory.getLogger(GeocodingService.class);

    private final GeoApiContext geoApi = new GeoApiContext().setApiKey(Constants.GOOGLE_KEY);

    public Shop latLng2Address(final LatLng location) {
        Shop shopAddress = new Shop();

        try {
            GeocodingResult[] result = GeocodingApi.reverseGeocode(geoApi, location).await();
            shopAddress = parseResult(result);
            System.out.println(shopAddress.toString());
        } catch (ApiException e) {

            LOG.error("Error with Google Maps", e);
        } catch (InterruptedException e) {

            LOG.error("Error with Google Maps", e);
        } catch (IOException e) {

            LOG.error("Error with Google Maps", e);
        }

        return shopAddress;
    }

    public Shop address2LatLng(final String address) {
        Shop shopAddress = new Shop();
        try {
            final GeocodingResult[] results = GeocodingApi.geocode(geoApi, address).await();
            shopAddress = parseResult(results);
            System.out.println(shopAddress.toString());
        } catch (ApiException e) {

            LOG.error("Error with Google Maps", e);
        } catch (InterruptedException e) {

            LOG.error("Error with Google Maps", e);
        } catch (IOException e) {

            LOG.error("Error with Google Maps", e);
        }

        return shopAddress;

    }

    private Shop parseResult(final GeocodingResult[] result) {
        final Shop shop = new Shop();
        final ShopAddress shopAddress = new ShopAddress();
        shop.setLatitude(result[0].geometry.location.lat);
        shop.setLongitude(result[0].geometry.location.lng);
        shopAddress.setStreet(result[0].formattedAddress);
        if (result[0].postcodeLocalities != null && StringUtils.isEmpty(result[0].postcodeLocalities[0])) {
            shopAddress.setPostCode(Integer.valueOf(result[0].postcodeLocalities[0]));
        } else {
            String[] addressParts = shopAddress.getStreet().split(" ");
            for (String part : addressParts) {
                try {
                    int postCode = Integer.valueOf(part.replaceAll(",", ""));
                    if (postCode > 999) {
                        shopAddress.setPostCode(postCode);
                    } else {
                        shopAddress.setNumber(postCode);
                    }
                } catch (NumberFormatException e) {

                }
            }
        }
        shop.setShopAddress(shopAddress);
        return shop;
    }

    public Distance[] checkDistances(final LatLng desiredPoint, final LatLng destination) {

        Distance[] dists = null;
        try {

            DistanceMatrix request = DistanceMatrixApi.newRequest(geoApi).origins(desiredPoint)
                    .destinations(destination).mode(TravelMode.WALKING).await();
            DistanceMatrixRow[] rows = request.rows;
            for (int i = 0; i < rows.length; i++) {
                DistanceMatrixRow row = rows[i];
                DistanceMatrixElement[] elements = row.elements;
                dists = new Distance[elements.length];
                for (int j = 0; j < elements.length; j++) {
                    DistanceMatrixElement element = elements[j];
                    if (!element.status.ZERO_RESULTS.name().equalsIgnoreCase(element.status.name())) {
                        Distance distance = element.distance;
                        System.out.println("Meters=" + distance.inMeters);
                        System.out.println("Meters=" + distance.humanReadable);
                        dists[j] = distance;
                    } else {
                        dists[0] = null;
                    }
                }

            }
        } catch (Exception e) {
            LOG.error("Error with Google Maps", e);
            dists = new Distance[1];
        }
        return dists;
    }

}
