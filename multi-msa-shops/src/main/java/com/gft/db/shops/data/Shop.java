package com.gft.db.shops.data;

import java.io.Serializable;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "shop")
public class Shop extends ResourceSupport implements Serializable, Comparator<Shop> {
	@NonNull
    private String name;

    private double longitude;
    private double latitude;
    private ShopAddress shopAddress = new ShopAddress();
    private double distancesToAnotherPoint;
    private String distanceRedable = "";
    private String manager;
  

    @Override
    public int compare(final Shop o1, final Shop o2) {
        int diffs = 0;
        if (o1 == null || o2 == null) {
            return --diffs;
        }
        if (o1.getName() != null && !o1.getName().equalsIgnoreCase(o2.getName())) {
            diffs++;
        }

        if (o1.getShopAddress() != null
                && !o1.getShopAddress().getStreet().equalsIgnoreCase(o2.getShopAddress().getStreet())) {
            diffs++;
        }
        if (o1.getShopAddress() != null && o2.getShopAddress() != null
                && o1.getShopAddress().getNumber() != o2.getShopAddress().getNumber()) {
            diffs++;
        }
        if (o1.getShopAddress() != null && o2.getShopAddress() != null
                && o1.getShopAddress().getPostCode() != o2.getShopAddress().getPostCode()) {
            diffs++;
        }

        return diffs;
    }

}
