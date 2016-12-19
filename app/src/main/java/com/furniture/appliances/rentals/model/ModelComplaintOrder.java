package com.furniture.appliances.rentals.model;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class ModelComplaintOrder implements Serializable{
    private String OrderId;
    private String complaints;
    private Integer complaint_count;
    private String orderDate;
    public ModelComplaintOrder(String id,String complaints,Integer count,String date)
    {
        this.OrderId = id;
        this.complaints = complaints;
        this.complaint_count = count;
        this.orderDate = date;

    }
    public String getOrderId()
    {
      return this.OrderId  ;
    }
    public String getOrderDate()
    {
        return this.orderDate;
    }
    public String getComplaints()
    {
        return this.complaints;
    }
    public Integer getComplaint_count()
    {
        return this.complaint_count;
    }
    public void setOrderId(String id)
    {
        this.OrderId = id;
    }
    public void setComplaints(String complaints)
    {
        this.complaints = complaints;
    }
    public void setCount(Integer count)
    {
        this.complaint_count = count;
    }
    public void setOrderDate(String date)
    {
        this.orderDate = date;
    }









}
