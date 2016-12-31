package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.asyncTask.HttpCall;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Config;
import com.furniture.appliances.rentals.util.Miscellaneous;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Infinia on 22-09-2015.
 */
public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    Button submit;
    MaterialEditText email, fname, lname, password;
    String value_email, value_fname, value_lname, value_password;
    ModelUser modelUser = new ModelUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        setUpToolbar();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(SignUp.this, Login.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Sign Up");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        submit = (Button) findViewById(R.id.submit);
        fname = (MaterialEditText) findViewById(R.id.fname);
        lname = (MaterialEditText) findViewById(R.id.lname);
        email = (MaterialEditText) findViewById(R.id.email);
        password = (MaterialEditText) findViewById(R.id.password);
       /* address = (MaterialEditText) findViewById(R.id.address);
       mobile = (MaterialEditText) findViewById(R.id.mobile);
        pincode = (MaterialEditText) findViewById(R.id.pincode);*/
        submit.setOnClickListener(this);
    }

    private void getDataFromFields() {
        value_fname = fname.getText().toString();
        value_lname = lname.getText().toString();
        value_email = email.getText().toString();
        value_password = password.getText().toString();
        /*value_mobile = mobile.getText().toString();
        value_address = address.getText().toString();
        value_pincode = pincode.getText().toString();*/
    }

    private boolean createUser() {
        if (validations()) {
            modelUser.firstname = value_fname;
            modelUser.lastname = value_lname;
            modelUser.email = value_email;
            modelUser.mobileno = "";
            modelUser.password = value_password;
            modelUser.address = "";
            modelUser.pincode = "";
            modelUser.image = "";
            modelUser.source = "permarent";
            modelUser.sex = "";
            return true;
        }
        return false;
    }

    private boolean validations() {
        getDataFromFields();
        if (Miscellaneous.checkEmptyEditext(SignUp.this, value_email, "E Mail")) {
            return false;
        }
        if (!Miscellaneous.isEmailValid(value_email)) {
            Toast.makeText(SignUp.this, "E Mail address is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Miscellaneous.checkEmptyEditext(SignUp.this, value_fname, "First Name")) {
            return false;
        }
        if (Miscellaneous.checkEmptyEditext(SignUp.this, value_lname, "Last Name")) {
            return false;
        }
        return !Miscellaneous.checkEmptyEditext(SignUp.this, value_password, "Password");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (createUser()) {
                    if (new CheckInternetConnection(SignUp.this).isConnectedToInternet()) {
                        new HttpCall().insertUserData(SignUp.this, modelUser);
                    } else {
                        new CheckInternetConnection(SignUp.this).showDialog();
                    }
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Config.IS_APP_IN_FOREGROUND = true;
        Config.CURRENT_ACTIVITY_CONTEXT = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Config.IS_APP_IN_FOREGROUND = false;
        Config.CURRENT_ACTIVITY_CONTEXT = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Sign Up", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }


}
