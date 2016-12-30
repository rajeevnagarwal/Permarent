package com.furniture.appliances.rentals.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Rajeev Nagarwal on 12/30/2016.
 */

public class ModelOffer implements Serializable {
    public String offerId;
    public String couponCode;
    public String discountApplicable;
    public String offerStartDate;
    public String offerEndDate;
    public String getMonthlyRent()
    {
        String monthlyRent="";
        try
        {
            JSONObject object = new JSONObject(this.discountApplicable);
            monthlyRent = String.valueOf(object.getInt("monthlyRent"));

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        finally
        {
            return monthlyRent;
        }
    }
    public String getSecurity()
    {
        String security="";
        try
        {
            JSONObject object = new JSONObject(this.discountApplicable);
            security = String.valueOf(object.getInt("security"));

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        finally
        {
            return security;
        }
    }
    public String getshippingCharges()
    {
        String shippingCharges="";
        try
        {
            JSONObject object = new JSONObject(this.discountApplicable);
            shippingCharges = String.valueOf(object.getInt("shippingCharges"));

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        finally
        {
            return shippingCharges;
        }
    }
    public String getfloorCharges()
    {
        String floorCharges="";
        try
        {
            JSONObject object = new JSONObject(this.discountApplicable);
            floorCharges = String.valueOf(object.getInt("floorCharges"));

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        finally
        {
            return floorCharges;
        }
    }
    public String getinstallationCharges()
    {
        String installationCharges="";
        try
        {
            JSONObject object = new JSONObject(this.discountApplicable);
            installationCharges = String.valueOf(object.getInt("installationCharges"));

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        finally
        {
            return installationCharges;
        }
    }
    public String getapplicableMonths()
    {
        String applicableMonths="";
        try
        {
            JSONObject object = new JSONObject(this.discountApplicable);
            applicableMonths = String.valueOf(object.getInt("applicableMonths"));

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        finally
        {
            return applicableMonths;
        }
    }

}
