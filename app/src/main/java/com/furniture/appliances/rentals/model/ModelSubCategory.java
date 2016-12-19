package com.furniture.appliances.rentals.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Infinia on 9/16/2015.
 */
public class ModelSubCategory implements Serializable{
    public String prod_code;
    public String prod_id;
    public String prod_name;
    public String vendor_code;
    public String category_code;
    public String category_desc;
    public String subcategory_code;
    public String subcategory_desc;
    public String small_img;
    public String big_img;
    public String material;
    public String dimensions;
    public String color;
    public String type;
    public String max_quantity;
    public String brand;
    public String other_description;
    public String rent_amount;
    public String rent_duration;
    public String security_deposit;
    public String min_rent_period;
    public String shipping_charges;
    public String created_on;
    public String rent_to_own;
    public String package_products;
    public int quantity=0;
    public int quantity_quarterly=0;
    public int quantity_monthly=0;
    public String productDesc;
    public String retailPrice;
    public String relatedProducts;
    public String productAvailability;
    public String popularRating;
    public String newRating;
    public Boolean liked;
    public String rentalAmount;
    public String weight;
    public String briefDesc;
    public String capacity;
    public String threeMo;
    public String sixMo;
    public String nineMo;
    public String twelveMo;
    public void setValues()
    {
        try
        {
            JSONArray array = new JSONArray(this.productDesc);
            for(int i=0;i<array.length();i++)
            {
                System.out.println("SetValues");
                JSONObject obj = array.getJSONObject(i);
                this.briefDesc = obj.getString("briefDesc");
                this.material = obj.getString("material");
                this.dimensions = obj.getString("dimensions");
                this.weight = String.valueOf(obj.getInt("weight"));
                this.color = obj.getString("color");
                this.capacity = obj.getString("capacity");
                this.brand = obj.getString("brand");
                this.type = obj.getString("type");
                this.other_description = obj.getString("otherDesc");

            }

            array = new JSONArray(this.rentalAmount);
            for(int i=0;i<array.length();i++)
            {
                JSONObject obj = array.getJSONObject(i);
                this.threeMo = obj.getString("threeMo");
                this.sixMo = obj.getString("sixMo");
                this.nineMo = obj.getString("nineMo");
                this.twelveMo = obj.getString("twelveMo");
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

    }

}
