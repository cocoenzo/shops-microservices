package com.gft.db.shops.dao;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gft.db.shops.data.ResponseData;
import com.gft.db.shops.data.Shop;

@Repository
public class ShopsDao {

	
	private DatabaseMock database;
	
	@Autowired
	public ShopsDao(DatabaseMock database){
		this.database=database;
	}
   
    public Shop readShop(final String name) {
       final Shop shop= database.readItem(name);
       return shop == null ? new Shop() : shop;
       
    }

    public Set<Shop> readAll() {
        return database.readAll();
    }

    public ResponseData save(final Shop shop) {
        if(database.addItem(shop)){
           	return new ResponseData(ResponseData.ACTION_UPDATE, shop);
        } else {
        	return new ResponseData(ResponseData.ACTION_NEW, shop);
        }
     }

    public ResponseData removeShop(final Shop shop) {
    	if(database.removeItem(shop.getName())){
    		return new ResponseData(ResponseData.ACTION_REMOVE, shop);
       } else {
    	   return new ResponseData(ResponseData.ACTION_ERROR, shop);
       }
    }

}
