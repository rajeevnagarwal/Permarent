package com.furniture.appliances.rentals.model;

import java.io.Serializable;

/**
 * Created by Infinia on 21-09-2015.
 */
public class ModelCart implements Serializable {
    public int quantity=0;
    public String prod_id;
    public String small_img;
    public String item_name;
    public String rent_amount;
    public String security_amount;
    public String total_amount;
    public String rent_type;
    public String item_id;

}
