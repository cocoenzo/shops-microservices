package com.gft.db.shops.dao;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;

/**
 * Represents the DAO Layer of the Model architecture.<br/>
 * It is defined the main method for interact with the <i>database</i> layer,
 * which in this case it is developed with a {@linkplain ConcurrentHashMap}
 * object.<br/>
 * In my opinion, in this class should contain whether the JdbcTemplate for
 * contact with database layer or any other method to have the objects
 * persistently stored.<br/>
 * The database is represented with {@linkplain DatabaseMock} class.
 * 
 * @author Ignacio Elorriaga
 * @version 1.0
 * @since 1.0
 * 
 */
@Repository
public class ShopsDao {

    private final DatabaseMock database;

    @Autowired
    public ShopsDao(DatabaseMock database) {
        this.database = database;
    }

    public Shop readShop(final String name) {
        return database.readItem(name).orElse(new Shop());

    }

    public Set<Shop> readAll() {
        return database.readAll();
    }

    public ResponseData save(final Shop shop) {
        final Optional<Shop> currentShop = database.addItem(shop);
        if (currentShop.isPresent()) {
            return new ResponseData(ResponseData.ACTION_UPDATE, currentShop.get());
        } else {
            return new ResponseData(ResponseData.ACTION_NEW, shop);
        }
    }

    public ResponseData removeShop(final String name) {
        final Optional<Shop> maybeShop = database.removeItem(name);
        String message = "";
        final Shop shop = new Shop();
        shop.setName(name);
        message = ResponseData.ACTION_NOT_FOUND;
        if (maybeShop.isPresent()) {
            message = ResponseData.ACTION_REMOVE;
        }
        return new ResponseData(message, shop);
    }

}
