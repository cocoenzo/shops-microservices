package com.gft.db.shops.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Wither
public class ShopAddress implements Serializable {

    private int number;
    private int postCode;
    private String street;


}
