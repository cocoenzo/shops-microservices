package com.gft.db.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.gft.db.shops.ShopsMicroservicesApplication;
import com.gft.db.shops.dao.DatabaseMock;
import com.gft.db.shops.dao.ShopsDao;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopAddress;
import com.gft.db.utils.BeanRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ShopsMicroservicesApplication.class })
@AutoConfigureMockMvc
public class ShopsControllerIntegrationTest {

    private static final String URI_SHOPS = "/shops/";

    private static final String URI_READ = URI_SHOPS + "{name}";

    private final DatabaseMock database = new DatabaseMock();
    @Autowired
    private ShopsDao dao;

    @Autowired
    private MockMvc mockMvc;

    private final Shop shop = BeanRepository.createShop();
    private final List<Shop> predefined = new ArrayList<Shop>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        predefined.addAll(Arrays.asList(shop, new Shop("JUnit Test 2", 22.7d, -0.86d, new ShopAddress("London", 25,
                11101))));
        for (Shop shop : predefined) {
            dao.save(shop);
        }

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRead() {

        // {"result":"Shop read successfully","shop":{"name":"JUnit Test
        // 1","longitude":23.0,"latitude":-0.89,"shopAddress":{"number":25,"postCode":11101,"street":"London"},"distancesToAnotherPoint":null,"links":[{"rel":"self","href":"http://localhost/shops"}]}}

        try {
            mockMvc.perform(get(URI_READ, shop.getName())).andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("shop.name").value(shop.getName()))
                    .andExpect(jsonPath("shop.shopAddress.street").value(shop.getShopAddress().getStreet()));
        } catch (Exception e) {

        }
    }

    @Test
    public void testFindAll() {
        try {
            ResultActions result = mockMvc.perform(get(URI_SHOPS));

            result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.length()").value(predefined.size()));
        } catch (Exception e) {

        }
    }
}
