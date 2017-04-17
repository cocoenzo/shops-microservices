package com.gft.db.shops.client.data;


public class ShopCommand extends Shop {

    private int number;
    private int postCode;
    private String street;
    private String name;

    public ShopCommand() {
        super();
    }

    public ShopCommand(final String name, final int number, final int postCode, final String street) {
        super.setName(name);
        super.setShopAddress(new ShopAddress(street, number, postCode));
        this.name = name;
        this.number = number;
        this.postCode = postCode;
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Shop convertToShop() {
        return new Shop(this.name, 0.0d, 0.0d, new ShopAddress(street, number, postCode));
    }
}
