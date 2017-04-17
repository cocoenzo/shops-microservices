package com.gft.db.shops.client.data;

import java.io.Serializable;
import java.util.Comparator;

public class Shop implements Serializable, Comparator<Shop> {

    private String name;

    private double longitude;
    private double latitude;
    private ShopAddress shopAddress;
    private long[] distancesToAnotherPoint;

    public Shop(String name, double longitude, double latitude, ShopAddress shopAddress) {
        super();
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.shopAddress = shopAddress;
    }

    public Shop() {
        this.shopAddress = new ShopAddress();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ShopAddress getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(ShopAddress shopAddress) {
        this.shopAddress = shopAddress;
    }

    public long[] getDistancesToAnotherPoint() {
        return distancesToAnotherPoint;
    }

    public void setDistancesToAnotherPoint(long[] distancesToAnotherPoint) {
        this.distancesToAnotherPoint = distancesToAnotherPoint;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((shopAddress == null) ? 0 : shopAddress.hashCode());
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
        final  Shop other = (Shop) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Shop [name=" + name + ", longitude=" + longitude + ", latitude=" + latitude + ", shopAddress="
                + shopAddress + "]";
    }

    public int compare(final Shop o1,final Shop o2) {
        int diffs = 0;
        if (o1 == null || o2 == null) {
            return --diffs;
        }
        if (o1.getName() != null && !o1.getName().equalsIgnoreCase(o2.getName())) {
            diffs++;
        }

        return diffs;
    }

}
