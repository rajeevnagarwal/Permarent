package com.furniture.appliances.rentals.util;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.furniture.appliances.rentals.model.ModelAddress;

import java.util.ArrayList;

/**
 * Created by Infinia on 30-06-2015.
 */
public class Config {
    public static String mainUrl ="https://www.permarent.com/";
    public static String newUrl = "http://88.198.151.27:9000/";




    public static String signIn = mainUrl+"/connection/checkUser.php";
    public static String getAllCategory = mainUrl+"/connection/headings.php";
    public static String getSubCategories= mainUrl+"/connection/categories.php";
    public static String insertUserData = mainUrl+"/connection/insertUserData.php";
    public static String getUserData = mainUrl+"/connection/getUserData.php";
    public static String insertAddress = mainUrl+"/connection/insertAddress.php";
    //public static String insertOrderDetails = mainUrl+"/connection/insertOrderDetails.php";
    public static String getPreviousOrders = mainUrl+"/connection/getOrders.php";
    public static String headingImage= mainUrl+"img/rentals/";
    public static String verifyNumber= "https://alerts.sinfini.com/api/v3/index.php";
    public static String subCategroyList= mainUrl+"connection/productlist.php";
    public static String subCategoryImage= mainUrl+"img/rentals/products/";
    public static String api_key="Abf85d8db7127ee20cbdf9cf8234f4bd2";

    public static String getPlaceDetails = "https://maps.googleapis.com/maps/api/place/details/json?";
    public static String KEY ="AIzaSyAmr6ODvsYUh628E27s8SrYso8btHPnm-s";
    public static String registerComplaint = newUrl+"registerComplaint";
    public static String getOrderDetails = newUrl+"getOrderDetails";
    public static String listSubcategories = newUrl+"listSubcategories";
    public static String getUserInfo = newUrl+"getUserInfo";
    public static String listProducts = newUrl+"listProducts";
    public static String getReferral = newUrl+"referralCode";
    public static String getWallet = newUrl+"getWalletCash";
    public static String getCoupons = newUrl+"getOffers";
    public static String getProduct = newUrl+"getProductDetails";
    public static String sendFeedback = newUrl+"sendFeedback";
    public static String getProductNames = newUrl+"getProductNames";
    public static String incrementLikes = newUrl+"incrementLikes";
    public static String insertRating = newUrl+"insertRating";
    public static String getPopularProducts = newUrl+"getPopularProducts";
    public static String getCategories = newUrl+"getCategories";
    public static String regNewUser = newUrl+"regNewUser";
    public static String checkUser = newUrl+"checkUser";
    public static String addContactNo = newUrl+"addContactNo";
    public static String addAddress = newUrl+"addAddress";
    public static String insertOrderDetails = newUrl+"insertOrderDetails";




    //Boolean constant to check if app is opened, used by GCM
    public static boolean IS_APP_IN_FOREGROUND = false;
    public static Activity CURRENT_ACTIVITY_CONTEXT;
    public static Fragment OPENED_FRAGMENT;

    public static int DISCOUNT_YEARLY=5;
    public static int DISCOUNT_HALF_YEARLY=10;

    public static String RAZORPAY_KEY="rzp_live_TR4ZYMDnP09tc7";
    public static String SMS_SENDER="PMRENT";

    public static String browserUrl ="https://play.google.com/store/apps/details?id=com.furniture.appliances.rentals";
    public static String playStoreUrl ="market://details?id=com.furniture.appliances.rentals";
    public static String contactNo="+919587334056";
    public static String adminNo = "9971118772,7353982196,7022014310";

    public static ArrayList<ModelAddress> Addresses = new ArrayList<ModelAddress>();
    public static String AppName="Permarent";

}
