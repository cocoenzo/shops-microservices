package com.gft.db.shops.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

/**
 * Represents the implementation of the Google Maps API in our application.<br/>
 * It is developed to retrieved the lat & lng for a specific street and
 * calculates distance between two LatLng points with the Google Maps API,
 * instead using the Math function.<br/>
 * Those processes are developed in an async method with the "await" function
 * from the Google Maps API. The main issue that we have here, it could be the
 * resources consumed and time expend to retrieve the distance, however, it was
 * considered clearer in the process because we do not need to include any extra
 * formula.<br/>
 * 
 * The API_KEY is stored in our Constant class, however, it must be included in
 * a properties file or as a DAP property (hidden for any developer). For this
 * task, we have considered it was enough to be stored as a Constant var.
 * 
 * @author Ignacio Elorriaga
 * @version 1.0
 * @since 1.0
 * 
 */
@Service
public class GeocodingService {

    private static final Logger LOG = LoggerFactory.getLogger(GeocodingService.class);

    private final GeoApiContext geoApi = new GeoApiContext().setApiKey(Constants.GOOGLE_KEY);

    public Shop address2LatLng(final String address) {
        Shop shopAddress = new Shop();
        try {
            final GeocodingResult[] results = GeocodingApi.geocode(geoApi, address).await();
            shopAddress = parseResult(results);
        } catch (ApiException | InterruptedException | IOException e) {
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
