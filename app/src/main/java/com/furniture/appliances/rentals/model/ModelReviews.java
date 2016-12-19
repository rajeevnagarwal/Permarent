package com.furniture.appliances.rentals.model;

/**
 * Created by Rajeev Nagarwal on 12/17/2016.
 */

public class ModelReviews {
    public String firstName;
    public String lastName;
    public String mail;
    public String rating;
    public String review;
    public ModelReviews(String first,String last,String mail,String rate,String review)
    {
        this.firstName = first;
        this.lastName = last;
        this.mail = mail;
        this.rating= rate;
        this.review = review;
    }

}
