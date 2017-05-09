package com.gft.db.shops.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShopCommand extends Shop {

    private int number;
    private int postCode;
    private String street;
    private String name;
    private String managerName;
    
    public ShopCommand(final String name, final int number, final int postCode, final String street) {
        super.setName(name);
        super.setShopAddress(new ShopAddress(number, postCode,street));
        this.name = name;
        this.number = number;
        this.postCode = postCode;
        this.street = street;
    }

    public Shop convertToShop() {
        return Shop.builder().build();
    }
}
