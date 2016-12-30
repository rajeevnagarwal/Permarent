package com.furniture.appliances.rentals.restApi;

import android.os.Looper;
import android.support.v7.widget.RecyclerView;

import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by Infinia on 18-09-2015.
 */
public class EndPonits {

    public static AsyncHttpClient syncHttpClient= new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void getAllCategories(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getAllCategory,params,asyncHttpResponseHandler);
    }
    public static void getSubCategories(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getSubCategories, params, asyncHttpResponseHandler);
    }
    public static void getAllProducts(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.subCategroyList, params, asyncHttpResponseHandler);
    }
    public static void insertUserData(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.insertUserData, params, asyncHttpResponseHandler);
    }
    public static void insertAddress(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.insertAddress, params, asyncHttpResponseHandler);
    }
    public static void insertOrderDetails(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.insertOrderDetails, params, asyncHttpResponseHandler);
    }
    public static void signIn(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.signIn, params, asyncHttpResponseHandler);
    }
    public static void getUser(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getUserData, params, asyncHttpResponseHandler);
    }
    public static void verifyNumber(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().post(Config.verifyNumber, params, asyncHttpResponseHandler);
    }

    public static void getPreviousOrders(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getPreviousOrders, params, asyncHttpResponseHandler);
    }
    public static void registerComplaint(RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.registerComplaint,params,asyncHttpResponseHandler);
    }
    public static void getOrderDetails(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getOrderDetails,params,asyncHttpResponseHandler);
    }
    public static void listSubCategories(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.listSubcategories,params,asyncHttpResponseHandler);
    }
    public static void getUserInfo(RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getUserInfo,params,asyncHttpResponseHandler);
    }
    public static void fetchproducts(RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.listProducts,params,asyncHttpResponseHandler);
    }
    public static void getProductDetails(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getProduct,params,asyncHttpResponseHandler);
    }

    public static void getReferral(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getReferral, params,asyncHttpResponseHandler);
    }
    public static void getWallet(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getWallet, params,asyncHttpResponseHandler);
    }
    public static void getCoupons(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getCoupons,params,asyncHttpResponseHandler);
    }
    public static void sendFeedback(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.sendFeedback,params,asyncHttpResponseHandler);
    }
    public static void getProductNames(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getProductNames,params,asyncHttpResponseHandler);
    }
    public static void incrementLikes(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.incrementLikes,params,asyncHttpResponseHandler);
    }
    public static void insertRating(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.insertRating,params,asyncHttpResponseHandler);
    }
    public static void getPopularProducts(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getPopularProducts,params,asyncHttpResponseHandler);
    }
    public static void getCategories(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.getCategories,params,asyncHttpResponseHandler);
    }
    public static void listProducts(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.listProducts,params,asyncHttpResponseHandler);
    }
    public static void regNewUser(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.regNewUser,params,asyncHttpResponseHandler);
    }
    public static void checkUser(RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.checkUser,params,asyncHttpResponseHandler);
    }
    public static void addContactNo(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.addContactNo,params,asyncHttpResponseHandler);
    }
    public static void addAddress(RequestParams params,AsyncHttpResponseHandler asyncHttpResponseHandler)
    {
        getClient().get(Config.addAddress,params,asyncHttpResponseHandler);
    }
    private static AsyncHttpClient getClient()
    {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }
}

