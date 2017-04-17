package com.gft.db.shops.dao;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.gft.db.shops.data.Shop;

/**
 * Represents the database component.<br/>
 * It should be accessed through the JdbcTemplate object and it could be an Oracle database or whatever type.
 * @author Ignacio Elorriaga
 *
 */
@Component
public class DatabaseMock {

	private final Map<String, Shop> items = new ConcurrentHashMap<String, Shop>();
	
	/**
	 * Include new element in the database.<br/>
	 * It uses the put to know the return value is whether null or not.
	 * @param shop to be included
	 * @return true if the Key exist previously (TRUE), false in case it wasn't there.
	 */
	public boolean addItem(final Shop shop){
		return items.put(shop.getName(), shop) == null ? Boolean.FALSE : Boolean.TRUE;
	}
	
	public boolean removeItem(final String shopName) {
		return items.remove(shopName) == null ? Boolean.FALSE: Boolean.TRUE;
	}
	
	public Set<Shop> readAll() {
		return new HashSet<Shop>(items.values());
	}
	public Shop readItem(final String shopName) {
		return items.get(shopName);
	}
	
	
}
