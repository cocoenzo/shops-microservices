package com.gft.db.shops.dao;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.gft.db.shops.data.Shop;

/**
 * Represents the database component.<br/>
 * It should be accessed through the JdbcTemplate object and it could be an
 * Oracle database or whatever type.<br/>
 * 
 * In this implementation it was developed with {@linkplain Optional} depends on
 * whether the object is present or not. In case it is present the previous
 * object was returned.<br/>
 * <h5>Concurrent issue</h5> In this case it is solved with the
 * {@linkplain ConcurrentHashMap} implementation, however, as far as I am
 * concerned, it should be delegated on the database layer with the different
 * options available (Row, column, table, so on...)
 * 
 * @author Ignacio Elorriaga
 * 
 */
@Repository
public class DatabaseMock {

    private final Map<String, Shop> items = new ConcurrentHashMap<String, Shop>();

    /**
     * Include new element in the database.<br/>
     * It uses the put to know the return value is whether null or not.
     * 
     * @param shop
     *            to be included
     * @return true if the Key exist previously (TRUE), false in case it wasn't
     *         there.
     */
    public Optional<Shop> addItem(final Shop shop) {
        Shop inserted = items.put(shop.getName(), shop);
        final Optional<Shop> maybe = Optional.ofNullable(inserted);
        return maybe;
    }

    public Optional<Shop> removeItem(final String shopName) {
        return Optional.ofNullable(items.remove(shopName));
    }

    public Set<Shop> readAll() {
        return new HashSet<Shop>(items.values());
    }

    public Shop readItem(final String shopName) {
        final Optional<Shop> maybe = Optional.ofNullable(items.get(shopName));
        Shop shop = new Shop();
        if (maybe.isPresent()) {
            shop = maybe.get();
        }
        return shop;
    }

}
