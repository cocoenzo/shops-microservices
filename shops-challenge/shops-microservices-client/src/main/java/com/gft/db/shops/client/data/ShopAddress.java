package com.gft.db.shops.client.data;

import java.io.Serializable;

public class ShopAddress implements Serializable {

    private int number;
    private int postCode;
    private String street;

    public ShopAddress() {
        street = "";
    }

    public ShopAddress(final int number, final int postCode) {
        this.number = number;
        this.postCode = postCode;
    }

    public ShopAddress(final String street, final int number, final int postCode) {
        this.street = street;
        this.number = number;
        this.postCode = postCode;
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
    public String toString() {
        return "ShopAddress [number=" + number + ", postCode=" + postCode + ", street=" + street + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        result = prime * result + postCode;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShopAddress other = (ShopAddress) obj;
        if (number != other.number)
            return false;
        if (postCode != other.postCode)
            return false;
        return true;
    }

}
