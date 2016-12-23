package com.furniture.appliances.rentals.model;

/**
 * Created by Rajeev Nagarwal on 12/11/2016.
 */

public class ModelProduct {
    public String pname;
    public String minRentalDuration;
    public String id;
    public String rating;
    public String availability;
    public String sec_amount;
    public String retail_price;
    public String likes;
    public String dimensions;
    public ModelProduct(String name,String duration,String rating,String availability,String sec_amount,String rprice,String likes,String dimen)
    {
        this.id = "";
        this.pname = name;
        this.minRentalDuration = duration;
        this.rating = rating;
        this.availability = availability;
        this.sec_amount = sec_amount;
        this.retail_price = rprice;
        this.likes = likes;
        this.dimensions = dimen;
    }
    public ModelProduct(String id,String name,String duration,String rating,String availability,String sec_amount,String rprice,String likes,String dimen)
    {
        this.id = id;
        this.pname = name;
        this.minRentalDuration = duration;
        this.rating = rating;
        this.availability = availability;
        this.sec_amount = sec_amount;
        this.retail_price = rprice;
        this.likes = likes;
        this.dimensions = dimen;
    }


}
