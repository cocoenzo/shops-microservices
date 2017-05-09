package com.gft.db.shops.service;

import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gft.db.shops.app.Constants;
import com.gft.db.shops.data.Shop;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;

/**
 * Represents the implementation of the Google Maps API in our application.<br/>
 * It is developed to retrieved the lat & lng for a specific street and
 * calculates distance between two LatLng points with formula, instead using the
 * Math function.<br/>
 * 
 * The API_KEY is stored in our application.properties file and retrieved
 * through the Value annotation.
 * 
 * @author Ignacio Elorriaga
 * @version 1.0
 * @since 1.0
 * 
 */
@Service
public class GeocodingService {

    private static final Logger LOG = LoggerFactory.getLogger(GeocodingService.class);

    @Value("${google.apiKey}")
    private String apiKey;

    private final GeoApiContext geoApi = new GeoApiContext();

    @PostConstruct
    private void init() {
        geoApi.setApiKey(apiKey);
    }

    public CompletableFuture<Shop> obtainLatittudeLongitude(final Shop shop) {
        final CompletableFuture<Shop> completableFutureResult = new CompletableFuture<Shop>();

        GeocodingApiRequest requestCoordinates = GeocodingApi.newRequest(geoApi)
                .address(shop.getShopAddress().getStreet());
        requestCoordinates.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(GeocodingResult[] geocodingResult) {
                if (geocodingResult != null && geocodingResult.length > 0) {
                    Geometry geometry = geocodingResult[0].geometry;
                    shop.setLatitude(geometry.location.lat);
                    shop.setLongitude(geometry.location.lng);
                    completableFutureResult.complete(shop);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.error("Error trying to get the Lat and Lng", e);
            }
        });
        return completableFutureResult;
    }

    /**
     * From https://rosettacode.org/wiki/Haversine_formula#Java
     */
    public static double checkDistances(final LatLng from, final LatLng destination) {
        double lat1 = from.lat;
        double lat2 = destination.lat;
        double lng1 = from.lng;
        double lng2 = destination.lng;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Constants.RADIUS * c;
    }

}
