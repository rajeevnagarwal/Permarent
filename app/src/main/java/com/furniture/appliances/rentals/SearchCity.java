package com.furniture.appliances.rentals;

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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.facebook.FacebookSdk;
import com.furniture.appliances.rentals.adapter.SearchAdapter;
import com.furniture.appliances.rentals.parser.ParseApi;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Infinia on 07-10-2015.
 */
public class SearchCity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private Toolbar mToolbar;
    TextView title;
    ImageView spinner;
    ListView lv;
    MaterialEditText city;
    SearchAdapter searchAdapter;
    RelativeLayout autoDetect;
    ArrayList<String> cityList = new ArrayList<String>();
    AppPreferences apref = new AppPreferences();


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    double latitude,longitude;
    FusedLocationProviderApi fusedLocationProviderApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_search_city);
        setUpToolbar();
        initView();
        getData();
        buildGoogleApiClient();
        city.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!city.getText().toString().equals(""))
                    lv.setVisibility(View.VISIBLE);
                else
                    lv.setVisibility(View.GONE);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s.toString());
            }
        });
        autoDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean temp = checkLocationService(SearchCity.this);
                if (temp) {
                    apref.writeString(SearchCity.this, "latitude", Double.toString(latitude));
                    apref.writeString(SearchCity.this, "longitude", Double.toString(longitude));
                    String city[] = getLocationName(latitude, longitude, SearchCity.this);
                    apref.writeString(SearchCity.this, "city", city[1]);
                    apref.writeString(SearchCity.this, "area", city[2]);
                    apref.writeString(SearchCity.this, "pincode", city[3]);
                    if (city[1] != null)
                        Toast.makeText(SearchCity.this, "City changed to " + city[1], Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(SearchCity.this, "City changed to Gurgaon", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SearchCity.this, MainActivity.class);
                    SearchCity.this.startActivity(i);
                    finish();
                } else {
                    showSettingsAlert();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Search City", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        RocqAnalytics.stopScreen(this);
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

        AlertDialog.Builder dialog = new AlertDialog.Builder(SearchCity.this);
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
    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        spinner = (ImageView) findViewById(R.id.spinner);
        autoDetect = (RelativeLayout) findViewById(R.id.autoDetect);
        title.setText("Search City");
        spinner.setVisibility(View.INVISIBLE);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(SearchCity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
    private void initView()
    {
       lv = (ListView) findViewById(R.id.lv);
        city = (MaterialEditText)findViewById(R.id.city);
        lv.setVisibility(View.GONE);
    }
    private void getData() {

        cityList = new ParseApi().parseCityData(SearchCity.this);
        java.util.Collections.sort(cityList);
        addCarsToList(cityList);
    }

    public void addCarsToList(ArrayList<String> result) {
        searchAdapter = new SearchAdapter(SearchCity.this, result);
        lv.setAdapter(searchAdapter);
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


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
