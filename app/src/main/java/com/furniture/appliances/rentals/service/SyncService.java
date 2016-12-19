package com.furniture.appliances.rentals.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.furniture.appliances.rentals.asyncTask.HttpCall;
import com.furniture.appliances.rentals.parser.ParseApi;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Infinia on 22-09-2015.
 */
public class SyncService extends Service {

    private boolean result;
    AppPreferences apref = new AppPreferences();
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
        if (Config.IS_APP_IN_FOREGROUND)
        {

            //getUserData(apref.readString(getApplicationContext(),"email",null));
            insertUser();

        }
        return START_STICKY;
    }

    public boolean getUserData(String email)
    {
        RequestParams params = new RequestParams();
        params.put("email", email);
        EndPonits.getUser(params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                result = new ParseApi().parseGetUserData(getApplicationContext(), response);
                if(!result)
                    insertUser();

            }

        });
        return result;
    }
    public void insertUser()
    {
        ModelUser modelUser = new ModelUser();
        modelUser.firstname = apref.readString(getApplicationContext(),"name",null);
        modelUser.lastname = "";
        modelUser.email = apref.readString(getApplicationContext(),"email",null);
        modelUser.mobileno = "";
        modelUser.password="";
        modelUser.address="";
        modelUser.pincode="";
        modelUser.image="";
        if(apref.IsLoginedByFb(getApplicationContext()))
            modelUser.source = "facebook";
        if(apref.IsLoginedByGoogle(getApplicationContext()))
            modelUser.source = "google";
        new HttpCall().insertUserData(getApplicationContext(),modelUser);
    }



}


