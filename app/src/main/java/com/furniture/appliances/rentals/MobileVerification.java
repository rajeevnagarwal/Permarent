package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.asyncTask.HttpCall;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Miscellaneous;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Infinia on 21-09-2015.
 */
public class MobileVerification extends AppCompatActivity {

    Toolbar mToolbar;
    Button submit;
    MaterialEditText mobile;
    String value_mobile;
    ModelAddress modelAddress = new ModelAddress();
    ModelUser modelUser = new ModelUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelAddress = (ModelAddress) getIntent().getSerializableExtra("modelAddress");
        modelUser = (ModelUser) getIntent().getSerializableExtra("modelUser");
        setContentView(R.layout.activity_mobile_verification);
        //Setting up toolbar
        setUpToolbar();
        initView();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new CheckInternetConnection(MobileVerification.this).isConnectedToInternet()) {
                    if(createUser())
                    new HttpCall().verifyNumber(MobileVerification.this, modelUser,modelAddress);
                } else {
                    new CheckInternetConnection(MobileVerification.this).showDialog();
                }
                
            }
        });
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    private void getDataFromFields() {
        value_mobile = mobile.getText().toString();
    }
    private void initView()
    {
        submit = (Button) findViewById(R.id.submit);
        mobile = (MaterialEditText) findViewById(R.id.mobile);

    }

    private boolean createUser()
    {
        if (validations()) {
           modelUser.mobileno=value_mobile;
            return true;
        }
        return false;
    }

    private boolean validations() {
        getDataFromFields();
        if (Miscellaneous.checkEmptyEditext(MobileVerification.this, value_mobile, "Mobile Number")) {
            return false;
        }
        if (value_mobile != null && value_mobile.length() < 10) {
            Toast.makeText(MobileVerification.this, "Mobile Number not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(MobileVerification.this, AddNewAddress.class);
            i.putExtra("modelAddress",modelAddress);
            i.putExtra("modelUser",modelUser);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Enter Mobile", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
}
