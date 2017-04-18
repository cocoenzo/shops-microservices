package com.gft.db.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.gft.db.shops.dao.ShopsDao;
import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopsException;
import com.gft.db.shops.service.GeocodingService;
import com.gft.db.shops.service.ShopsService;
import com.gft.db.utils.BeanRepository;
import com.google.maps.model.Distance;
import com.google.maps.model.LatLng;

public class ShopsServiceTest {

    @Mock
    private ShopsDao dao;

    @InjectMocks
    private ShopsService service;

    @Mock
    private GeocodingService geocodingService;

    private final Distance[] distances = new Distance[1];

    @Before
    public void before() {
        distances[0] = new Distance();
        distances[0].inMeters = 100l;

        MockitoAnnotations.initMocks(this);
        
        Mockito.when(dao.readAll()).thenReturn(new HashSet<Shop>(Arrays.asList(BeanRepository.createShop())));
        Mockito.when(dao.readShop(Mockito.anyString())).thenReturn(BeanRepository.createShop());
        Mockito.when(dao.removeShop(Mockito.any(Shop.class))).thenReturn(
                new ResponseData(ResponseData.ACTION_REMOVE, BeanRepository.createShop()));
        Mockito.when(dao.save(Mockito.any(Shop.class))).thenReturn(
                new ResponseData(ResponseData.ACTION_NEW, BeanRepository.createShop()));
        Mockito.when(geocodingService.address2LatLng(Mockito.anyString())).thenReturn(BeanRepository.createShop());
        Mockito.when(geocodingService.checkDistances(Mockito.any(LatLng.class), Mockito.any(LatLng.class))).thenReturn(
                distances);
    }

    @Test
    public void testReadShop() {

        final Shop readShop = service.readShop(BeanRepository.DEFAULT_NAME);

        Assert.assertEquals(readShop, BeanRepository.createShop());
        Mockito.verify(dao, Mockito.times(1)).readShop(Mockito.anyString());

    }

    @Test
    public void testReadAll() {
        final Set<Shop> readShops = service.readAll();

        Assert.assertEquals(readShops, new HashSet<Shop>(Arrays.asList(BeanRepository.createShop())));
        Mockito.verify(dao, Mockito.times(1)).readAll();
    }

    @Test
    public void testRemove() throws ShopsException{
        final ResponseData response = service.remove(BeanRepository.createShop());

        Assert.assertEquals(ResponseData.ACTION_REMOVE, response.getResult());
        Mockito.verify(dao, Mockito.times(1)).removeShop(Mockito.any(Shop.class));
    }

    @Test
    public void testInvalidSave() throws ShopsException{
        final Shop invalidShop = BeanRepository.createShop();
        invalidShop.setName(null);

        final ResponseData response = service.save(invalidShop);
        Assert.assertEquals(ResponseData.ACTION_ERROR, response.getResult());
        Mockito.verifyZeroInteractions(dao);
    }

    @Test
    public void testSave() throws ShopsException{
        final ResponseData response = service.save(BeanRepository.createShop());

        Assert.assertEquals(ResponseData.ACTION_NEW, response.getResult());
        Mockito.verify(dao, Mockito.times(1)).save(Mockito.any(Shop.class));
    }

    @Test
    public void testUpdate() throws ShopsException{
        Mockito.when(dao.save(Mockito.any(Shop.class))).thenReturn(
                new ResponseData(ResponseData.ACTION_UPDATE, BeanRepository.createShop()));

        final ResponseData response = service.save(BeanRepository.createShop());

        Assert.assertEquals(ResponseData.ACTION_UPDATE, response.getResult());
        Mockito.verify(dao, Mockito.times(1)).save(Mockito.any(Shop.class));
    }
}
