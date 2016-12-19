package com.furniture.appliances.rentals;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.facebook.FacebookSdk;
import com.furniture.appliances.rentals.asyncTask.HttpCall;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Config;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.loopj.android.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Infinia on 22-09-2015.
 */
public class Welcome extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener ,LocationListener {
    ProgressDialog progressDialog;
    AppPreferences apref = new AppPreferences();
    ProgressWheel progressWheel;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    double latitude,longitude;
    FusedLocationProviderApi fusedLocationProviderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RocqAnalytics.initialize(this);
        RocqAnalytics.registerPush(this);
        setContentView(R.layout.activity_welcome);
        //progressWheel = (ProgressWheel)findViewById(R.id.progressWheel);
        FacebookSdk.sdkInitialize(getApplicationContext());
        buildGoogleApiClient();
        RocqAnalytics.startScreen(this);

    }

    protected synchronized void buildGoogleApiClient() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }


    class splash extends TimerTask {

        @Override
        public void run() {
            Intent i = new Intent(Welcome.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    public void removeDialog() {
       // progressWheel.stopSpinning();
    }

    protected void showDialog(String text) {
       //progressWheel.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        RocqAnalytics.initialize(Welcome.this);
        RocqAnalytics.trackScreen("Welcome", new ActionProperties());
        RocqAnalytics.registerPush(Welcome.this);
        String temp =RocqAnalytics.getPushRegistrationId(Welcome.this);
     ///   System.out.print("Reg id is : " + temp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(Welcome.this,"Resume",Toast.LENGTH_SHORT).show();
        Config.IS_APP_IN_FOREGROUND = true;
        Config.CURRENT_ACTIVITY_CONTEXT = this;
        String lat = apref.readString(Welcome.this, "latitude", null);
        String longi = apref.readString(Welcome.this, "longitude", null);
        if (apref.IsLogined(Welcome.this)) {

            if (lat != null && longi != null) {
                Timer t = new Timer();
                t.schedule(new splash(), 3000);

            } else {
                if (checkLocationService(Welcome.this)) {
                    apref.writeString(Welcome.this, "latitude", Double.toString(latitude));
                    apref.writeString(Welcome.this, "longitude", Double.toString(longitude));
                    String city[] = getLocationName(latitude, longitude, Welcome.this);
                    apref.writeString(Welcome.this, "city", city[1]);
                    apref.writeString(Welcome.this, "area", city[2]);
                    apref.writeString(Welcome.this, "pincode", city[3]);
                    removeDialog();
                    Timer t = new Timer();
                    t.schedule(new splash(), 2000);
                } else {
                    showSettingsAlert();
                }
            }

        } else {
            if (lat != null && longi != null) {
                showDialog("Loading");
                if (new CheckInternetConnection(Welcome.this).isConnectedToInternet()) {
                    new HttpCall().getAllCategory(Welcome.this, null);

                } else {
                    new CheckInternetConnection(Welcome.this).showDialog();
                }

            } else {
                if (checkLocationService(Welcome.this)) {
                    progressDialog = new ProgressDialog(Welcome.this);
                    progressDialog.setMessage("Fetching Location");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    apref.writeString(Welcome.this, "latitude", Double.toString(latitude));
                    apref.writeString(Welcome.this, "longitude", Double.toString(longitude));
                    String city[] = getLocationName(latitude, longitude, Welcome.this);
                    apref.writeString(Welcome.this, "city", city[1]);
                    apref.writeString(Welcome.this, "area", city[2]);
                    apref.writeString(Welcome.this, "pincode", city[3]);
                    progressDialog.dismiss();
                    if (new CheckInternetConnection(Welcome.this).isConnectedToInternet()) {
                        showDialog("Loading");
                        new HttpCall().getAllCategory(Welcome.this, null);

                    } else {
                        new CheckInternetConnection(Welcome.this).showDialog();
                    }
                } else {
                    showSettingsAlert();
                }
            }
        }
    }

    private boolean checkLocationService(Context context)
    {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    private void showSettingsAlert() {

            AlertDialog.Builder dialog = new AlertDialog.Builder(Welcome.this);
            dialog.setMessage("GPS is not enabled. Move to Location settings.");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //this will navigate user to the device location settings screen
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
    }




    @Override
    protected void onPause() {
        super.onPause();
        Config.IS_APP_IN_FOREGROUND = false;
        Config.CURRENT_ACTIVITY_CONTEXT = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onConnected(Bundle arg0) {
        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    public String[] getLocationName(double lattitude, double longitude,Context context) {

        String[] details = new String[4];
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {

            List<Address> addresses = gcd.getFromLocation(lattitude, longitude,
                    10);

            for (Address adrs : addresses) {
                if (adrs != null) {

                    details[0] = adrs.getAdminArea();
                    details[1] = adrs.getSubAdminArea();
                    details[2] = adrs.getThoroughfare();
                    details[3] = adrs.getPostalCode();

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return details;

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }



}

