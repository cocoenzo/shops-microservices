package com.gft.db.shops;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.List;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.gft.db.shops.app.Constants;
import com.gft.db.shops.dao.DatabaseMock;
import com.gft.db.shops.dao.ShopsDao;
import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopAddress;
import com.gft.db.shops.data.ShopsException;
import com.gft.db.shops.service.GeocodingService;
import com.gft.db.shops.service.ShopsService;
import com.google.gson.Gson;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
// @AutoConfigureMockMvc
public class ShopsControllerIntegrationTest {

	@Autowired
	private DatabaseMock database;
	@Autowired
	WebApplicationContext wac;
	@Autowired
	MockHttpSession session;
	@Autowired
	MockHttpServletRequest request;
	@InjectMocks
	private ShopsService service;
	@Mock
	private GeocodingService geoService;

	@Mock
	private ShopsDao dao;

	private MockMvc mockMvc;

	private final Shop shop = new Shop("JUnit Test 1", 23.0d, -0.89d, new ShopAddress("London", 25, 11101));

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = webAppContextSetup(wac).build();

		java.util.List<Shop> predefined = Arrays.asList(shop,
				new Shop("JUnit Test 2", 22.7d, -0.86d, new ShopAddress("London", 25, 11101)));
		for (Shop shop : predefined) {
			database.addItem(shop);
		}

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testRead() {

		// shop.add(new Link("http://localhost/shops", "self"));
		Mockito.when(dao.readShop(Mockito.anyString())).thenReturn(shop);
		// {"result":"Shop read successfully","shop":{"name":"JUnit Test
		// 1","longitude":23.0,"latitude":-0.89,"shopAddress":{"number":25,"postCode":11101,"street":"London"},"distancesToAnotherPoint":null,"links":[{"rel":"self","href":"http://localhost/shops"}]}}
		final ResponseData responseExpected = new ResponseData(Constants.ACTION_READ_MSG, shop);

		try {
			mockMvc.perform(get("/shops/read/{name}", shop.getName())).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("shop.name").value(shop.getName()))
					.andExpect(jsonPath("shop.shopAddress.street").value(shop.getShopAddress().getStreet()));
		} catch (Exception e) {

		}
	}
	
	@Test
	public void testSaveNewElement() {
//		Mockito.when(dao.save(Mockito.any(Shop.class))).thenReturn(new ResponseData(ResponseData.ACTION_NEW, shop));
//		try {
//			mockMvc.perform(post("/shops/save")
//						.param("name", shop.getName())
//						.param("street", shop.getShopAddress().getStreet())
//						.param("number", Integer.toString(shop.getShopAddress().getNumber()))
//						.param("postCode", Integer.toString(shop.getShopAddress().getPostCode())))
//			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
//		} catch (Exception e) {
//			
//		}
	}
}
