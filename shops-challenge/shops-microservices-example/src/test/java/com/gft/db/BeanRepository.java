package com.gft.db;
import com.gft.db.shops.data.Shop;
import com.gft.db.shops.data.ShopAddress;

public class BeanRepository {

    public static final String DEFAULT_NAME = "Junit Test";
    public static final int DEFAULT_NUMBER = 100;
    public static final double DEFAULT_LAT = 100d;
    public static final double DEFAULT_LONG = 100d;
    public static final int DEFAULT_POST_CODE = 10000;
    public static final String DEFAULT_STREET = "100 Fifth Ave.";

    private BeanRepository() {
    }

    public static Shop createShop() {
        return new Shop(DEFAULT_NAME, DEFAULT_LAT, DEFAULT_LONG, new ShopAddress(DEFAULT_STREET, DEFAULT_NUMBER,
                DEFAULT_POST_CODE));
    }
}
