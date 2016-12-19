package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.adapter.PlaceAdapter;
import com.furniture.appliances.rentals.asyncTask.GetInfo;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.ClearableAutoCompleteTextView;
import com.furniture.appliances.rentals.util.Miscellaneous;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Infinia on 22-09-2015.
 */
public class AddNewAddress extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private PlaceAdapter placeAdapter;
    private Toolbar mToolbar;
    MaterialEditText city,area,house,street,pincode;
    String value_city,value_area,value_house,value_street,value_pincode;
    Button submit;
    ModelAddress modelAddress = new ModelAddress();
    ModelAddress received = new ModelAddress();
    ModelUser modelUser = new ModelUser();
    AppPreferences apref = new AppPreferences();
    String placeId,details[]=new String[3];
    private ClearableAutoCompleteTextView autoCompleteTextView;
    LinearLayout placeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        received = (ModelAddress) getIntent().getSerializableExtra("modelAddress");
        setUpToolbar();
        initView();
        setView();
        setGoogleCLient();
        setPlaceAdapter();
        getDataFromDb();
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setGoogleCLient() {
        googleApiClient = new GoogleApiClient.Builder(AddNewAddress.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

    }

    //Place Adapter to hold autocomplete string
    private void setPlaceAdapter() {
        autoCompleteTextView.setOnItemClickListener(autocompleteClickListener);
        placeAdapter = new PlaceAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoCompleteTextView.setAdapter(placeAdapter);
    }


    private AdapterView.OnItemClickListener autocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAdapter.PlaceAutocomplete item = placeAdapter.getItem(position);
            placeId = String.valueOf(item.placeId);
            new GetInfo(AddNewAddress.this,placeId).execute();


        }
    };

    private void getDataFromDb()
    {
        DBInteraction dbInteraction = new DBInteraction(AddNewAddress.this);
        modelUser = dbInteraction.getUserById(apref.readString(AddNewAddress.this,"email",null));
        dbInteraction.close();
    }

    private void setView()
    {
        if(received!=null && received.pincode!=null)
        {
            city.setText(received.city);
            area.setText(received.area);
            pincode.setText(received.pincode);
            house.setText(received.house);
            street.setText(received.street);
            placeDetails.setVisibility(View.VISIBLE);
        }
    }

    public void setData(String v1, String v2, String v3,String v4)
    {
        area.setText(v1);
        city.setText(v2);
        pincode.setText(v3);
        street.setText(v4);
        placeDetails.setVisibility(View.VISIBLE);
    }

    private void getDataFromFields() {
        value_city = city.getText().toString();
        value_area = area.getText().toString();
        value_house = house.getText().toString();
        value_street = street.getText().toString();
        value_pincode = pincode.getText().toString();
    }

    private void initView()
    {
        placeDetails = (LinearLayout) findViewById(R.id.placeDetails);
        city = (MaterialEditText) findViewById(R.id.city);
        area = (MaterialEditText) findViewById(R.id.area);
        house = (MaterialEditText) findViewById(R.id.house);
        street = (MaterialEditText) findViewById(R.id.street);
        pincode = (MaterialEditText) findViewById(R.id.pincode);
        submit = (Button)findViewById(R.id.submit);
        autoCompleteTextView = (ClearableAutoCompleteTextView) findViewById(R.id.searchPlaces);
        autoCompleteTextView.setThreshold(3);
        submit.setOnClickListener(this);
    }

    private boolean createAddress()
    {
        if (validations()) {
            modelAddress.city = value_city;
            modelAddress.area = value_area;
            modelAddress.house = value_house;
            modelAddress.street=value_street;
            modelAddress.name="";
            modelAddress.pincode = value_pincode;
            modelAddress.detail =modelAddress.house + ", " + modelAddress.street + ", " + modelAddress.area + ", " + modelAddress.city;
            modelAddress.mobile_no="";
            return true;
        }
        return false;
    }

    private boolean validations() {
        getDataFromFields();
        if (Miscellaneous.checkEmptyEditext(AddNewAddress.this, value_city, "City")) {
            return false;
        }
        if (Miscellaneous.checkEmptyEditext(AddNewAddress.this, value_area, "Area")) {
            return false;
        }
        if (Miscellaneous.checkEmptyEditext(AddNewAddress.this, value_house, "House/Flat No")) {
            return false;
        }
        if (Miscellaneous.checkEmptyEditext(AddNewAddress.this, value_street, "Street/Building")) {
            return false;
        }
        if (Miscellaneous.checkEmptyEditext(AddNewAddress.this, value_pincode, "PinCode")) {
            return false;
        }
        if (value_pincode != null && value_pincode.length() < 5) {
            Toast.makeText(AddNewAddress.this, "PinCode not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (value_pincode != null && value_pincode.length() > 7) {
            Toast.makeText(AddNewAddress.this, "PinCode not valid", Toast.LENGTH_SHORT).show();
            return false;
        }

       else
            return true;
    }

    /**
     * Button on click listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (createAddress()) {
                    if (new CheckInternetConnection(AddNewAddress.this).isConnectedToInternet()) {
                            Intent i = new Intent(AddNewAddress.this,MobileVerification.class);
                            i.putExtra("modelAddress",modelAddress);
                            i.putExtra("modelUser",modelUser);
                            startActivity(i);
                            finish();
                    } else {
                        new CheckInternetConnection(AddNewAddress.this).showDialog();
                    }
                }
                break;
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(AddNewAddress.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onConnected(Bundle bundle) {
        placeAdapter.setGoogleApiClient(googleApiClient);


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        placeAdapter.setGoogleApiClient(null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Add new address", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
}
