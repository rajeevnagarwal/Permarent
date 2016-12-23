package com.furniture.appliances.rentals.model;

import java.io.Serializable;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class Subcategory implements Serializable {
    private String name;
    private String image;
    private String id;
    public Subcategory(String name,String id)
    {
        this.name = name;
        this.image = "";
        this.id = id;
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
    public String getId(){return this.id;}
    public void setId(String id){this.id = id;}





}
