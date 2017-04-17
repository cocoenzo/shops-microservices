package com.gft.db.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.Link;

import com.gft.db.BeanRepository;
import com.gft.db.shops.api.ShopsController;
import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopAddress;
import com.gft.db.shops.data.ShopsException;
import com.gft.db.shops.service.ShopsService;
import com.google.maps.model.LatLng;


public class ShopsControllerTest {

    private static final String DEFAULT_NAME = "Junit Test";
    private static final int DEFAULT_NUMBER = 100;
    private static final double DEFAULT_LAT = 100d;
    private static final double DEFAULT_LONG = 100d;
    private static final int DEFAULT_POST_CODE = 10000;
    private static final String DEFAULT_STREET = "100 Fifth Ave.";

    @InjectMocks
    private ShopsController controller;

    @Mock
    private ShopsService service;
    
    @Mock
    private Link link;

    private final Shop defaultShop = BeanRepository.createShop();
    private final Set<Shop> defaultValues = new HashSet<Shop>(Arrays.asList(defaultShop));

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(link.withSelfRel()).thenReturn(link);
        Mockito.when(service.readAll()).thenReturn(defaultValues);
        Mockito.when(service.readShop(Mockito.anyString())).thenReturn(defaultShop);
        try {
			Mockito.when(service.save(Mockito.any(Shop.class))).thenReturn(
			        new ResponseData(ResponseData.ACTION_NEW, defaultShop));
		} catch (ShopsException e) {
			
		}
    }

    @Ignore(value="Due to the static methods in the Link ATHEOAS methods.")
    @Test
    public void testReadShop() {
    	
        final ResponseData response = controller.readShop(DEFAULT_NAME);
        final Shop readShop = (Shop) response.getShop();
        Assert.assertNotNull(readShop);
        Assert.assertEquals(defaultShop, readShop);
        Mockito.verify(service, Mockito.times(1)).readShop(Mockito.eq(DEFAULT_NAME));
    }

    @Test
    public void testSave() {
        final Shop insertShop = new Shop("1" + DEFAULT_NAME, DEFAULT_LONG, DEFAULT_LAT, new ShopAddress(DEFAULT_STREET,
                DEFAULT_NUMBER, DEFAULT_POST_CODE + 1));
        try {
			Mockito.when(service.save(Mockito.any(Shop.class))).thenReturn(
			        new ResponseData(ResponseData.ACTION_NEW, insertShop));
		} catch (ShopsException e) {
		
		}


    }

    @Test
    public void testReadAll() {
        Set<Shop> readShops = controller.readAll();
        Mockito.verify(service, Mockito.times(1)).readAll();
        Assert.assertEquals(defaultValues.size(), readShops.size());
        Assert.assertEquals(defaultValues.iterator().next(), readShops.iterator().next());
    }

    @Test
    public void testFind() {
        Mockito.when(service.findNearestShops(Mockito.any(LatLng.class))).thenReturn(defaultValues);

        Set<Shop> shopsFound = controller.findNearestShops(41.4567, -0.876323);

        Assert.assertNotNull(shopsFound);
        Assert.assertTrue(!shopsFound.isEmpty());
        Mockito.verify(service, Mockito.times(1)).findNearestShops(Mockito.any(LatLng.class));

        Mockito.when(service.findNearestShops(Mockito.any(LatLng.class))).thenReturn(new HashSet<Shop>());

        shopsFound = controller.findNearestShops(41.4567, -0.876323);

        Assert.assertNotNull(shopsFound);
        Assert.assertTrue(shopsFound.isEmpty());

    }
}
