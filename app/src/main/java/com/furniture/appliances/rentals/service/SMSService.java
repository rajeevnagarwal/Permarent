package com.furniture.appliances.rentals.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Infinia on 28-10-2015.
 */
public class SMSService  extends Service {

    private boolean result;
    AppPreferences apref = new AppPreferences();
    boolean isSentToCustomer=false;
    Service mService;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * Method of Service class
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            mService = this;
            String mobileNo = intent.getStringExtra("mobileNo");
            String orderId = intent.getStringExtra("orderId");

            sendSms(mobileNo,orderId,"Hurray! Your order '"+orderId+"' has been successfully placed. Our representative will call you shortly. Happy Renting!");
        return START_STICKY;
    }

    public boolean sendSms(final String mobileNo,final String orderId,String message)
    {
        RequestParams params = new RequestParams();
        params.put("method", "sms");
        params.put("api_key", Config.api_key);
        params.put("to", mobileNo);
        params.put("sender","PMRENT");
        params.put("message",message);
        params.put("format","json");
        params.put("custom","1,2");
        params.put("flash","0");
        EndPonits.verifyNumber(params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    String status = response.getString("status");
                    if (status != null && status.equals("OK")) {
                        if (!isSentToCustomer) {
                            isSentToCustomer = true;
                            sendSms(Config.contactNo, orderId, "Order '" + orderId + "' Received!!");
                        } else {
                            if (mService != null)
                                mService.stopSelf();
                        }
                    } else {
                        sendSms(mobileNo, orderId, "Hurray! Your order '" + orderId + "' has been successfully placed. Our representative will call you shortly. Happy Renting!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
        return result;
    }



}


