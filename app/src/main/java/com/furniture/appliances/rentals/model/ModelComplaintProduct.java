package com.furniture.appliances.rentals.model;

import java.io.Serializable;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class ModelComplaintProduct implements Serializable {
    private String complaint_date;
    private String product_name;
    private String status;
    private String reason;

    public ModelComplaintProduct(String name,String date,String status,String reason)
    {
        this.complaint_date = date;
        this.product_name = name;
        this.status = status;
        this.reason = reason;
    }


    public void setComplaint_date(String complaint_date) {
        this.complaint_date = complaint_date;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getComplaint_date() {
        return complaint_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

}
