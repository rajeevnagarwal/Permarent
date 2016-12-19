package com.furniture.appliances.rentals.model;

import java.io.Serializable;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class Subcategory implements Serializable {
    private String name;
    private String image;
    public Subcategory(String name)
    {
        this.name = name;
        this.image = "";
    }
    public String getName()
    {
        return this.name;
    }
    public String getImage()
    {
        return this.image;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setImage(String image)
    {
        this.image = image;
    }





}
