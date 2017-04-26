package com.gft.db.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gft.db.shops.dao.ShopsDao;
import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopsException;
import com.gft.db.shops.service.GeocodingService;
import com.gft.db.shops.service.ShopsService;
import com.gft.db.utils.BeanRepository;

public class ShopsServiceTest {

    @Mock
    private ShopsDao dao;

    @InjectMocks
    private ShopsService service;

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private CompletableFuture<Shop> completeFuture;

    private final Shop shop = BeanRepository.createShop();

    @Before
    public void before() {

        MockitoAnnotations.initMocks(this);

        when(dao.readAll()).thenReturn(new HashSet<Shop>(Arrays.asList(shop)));
        when(dao.readShop(anyString())).thenReturn(shop);
        when(dao.removeShop(anyString())).thenReturn(new ResponseData(ResponseData.ACTION_REMOVE, shop));
        when(dao.save(any(Shop.class))).thenReturn(new ResponseData(ResponseData.ACTION_NEW, shop));
        when(geocodingService.obtainLatittudeLongitude(any(Shop.class))).thenReturn(completeFuture);

    }

    @Test
    public void testReadShop() {

        final Shop readShop = service.readShop(BeanRepository.DEFAULT_NAME);

        Assert.assertEquals(readShop, shop);
        verify(dao, times(1)).readShop(anyString());

    }

    @Test
    public void testReadAll() {
        final Set<Shop> readShops = service.readAll();

        Assert.assertEquals(Arrays.asList(shop).size(), readShops.size());
        verify(dao, times(1)).readAll();
    }

    @Test
    public void testRemove() throws ShopsException {
        final ResponseData response = service.remove(shop.getName());

        Assert.assertEquals(ResponseData.ACTION_REMOVE, response.getResult());
        verify(dao, times(1)).removeShop(anyString());
    }

    @Test
    public void testInvalidSave() throws ShopsException {
        final Shop invalidShop = shop;
        invalidShop.setName(null);

        final ResponseData response = service.save(invalidShop);
        Assert.assertEquals(ResponseData.ACTION_ERROR, response.getResult());
        verifyZeroInteractions(dao);
    }

    @Test
    public void testSave() throws ShopsException {
        final ResponseData response = service.save(shop);

        Assert.assertEquals(ResponseData.ACTION_NEW, response.getResult());
        verify(dao, times(1)).save(any(Shop.class));
    }

    @Test
    public void testUpdate() throws ShopsException {
        when(dao.save(any(Shop.class))).thenReturn(new ResponseData(ResponseData.ACTION_UPDATE, shop));

        final ResponseData response = service.save(shop);

        Assert.assertEquals(ResponseData.ACTION_UPDATE, response.getResult());
        verify(dao, times(1)).save(any(Shop.class));
    }
}
