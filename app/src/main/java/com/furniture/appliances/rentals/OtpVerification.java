package com.furniture.appliances.rentals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.asyncTask.HttpCall;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Infinia on 22-09-2015.
 */
public class OtpVerification extends AppCompatActivity {

    Toolbar mToolbar;
    static Button verify;
   static MaterialEditText code;
    TextView mobile;
    ModelAddress modelAddress = new ModelAddress();
    AppPreferences apref = new AppPreferences();
    ModelUser modelUser = new ModelUser();
    String otp;
    String value_mobile;
    static ProgressDialog mProgressDialog;


    public static void setSmsReceived(String smsReceived) {
        OtpVerification.smsReceived = smsReceived;
        setData(smsReceived);

    }

    public static String smsReceived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelAddress = (ModelAddress) getIntent().getSerializableExtra("modelAddress");
        value_mobile = getIntent().getStringExtra("mobile");
       // modelUser = (ModelUser) getIntent().getSerializableExtra("modelUser");
        otp = getIntent().getStringExtra("otp");
        setContentView(R.layout.activity_otp_cerification);
        setUpToolbar();
        initView();
        mProgressDialog = new ProgressDialog(OtpVerification.this);
        mProgressDialog.setMessage("Receiving SMS.");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(code.getText().toString().equals("")) {
                            Toast.makeText(OtpVerification.this, "Sorry ! We couldn't detect the OTP. Please enter it manually.", Toast.LENGTH_SHORT).show();
                            verify.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }, 30000);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otp.equals(code.getText().toString()))
                {
                    if (new CheckInternetConnection(OtpVerification.this).isConnectedToInternet()) {
                        RequestParams params = new RequestParams();
                        params.put("email",apref.readString(getApplicationContext(),"email",null));
                        params.put("location",modelAddress.location);
                        params.put("city",modelAddress.city);
                        params.put("state",modelAddress.state);
                        params.put("others",modelAddress.others);
                        params.put("pincode",modelAddress.pincode);
                        params.put("locality",modelAddress.localityName);
                        params.put("houseNo",modelAddress.houseNo);
                        EndPonits.addAddress(params, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                System.out.println("Failure in Address");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                System.out.println("Success in Address");
                                try {
                                    JSONObject obj = new JSONObject(responseString);
                                    if(obj.getString("message").equals("Added Successfully")) {
                                        Boolean isNoVerified = true;

                                        if (apref.IsReadyForCheckout2(getApplicationContext())) {
                                            Intent i = new Intent(getApplicationContext(), Checkout2.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getApplicationContext().startActivity(i);
                                            if (getApplicationContext() instanceof OtpVerification)
                                                ((OtpVerification) getApplicationContext()).finish();
                                        } else {
                                            if (isNoVerified) {
                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                getApplicationContext().startActivity(i);
                                                if (getApplicationContext() instanceof AddNewAddress)
                                                    ((AddNewAddress) getApplicationContext()).finish();
                                            } else {
                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                getApplicationContext().startActivity(i);
                                                if (getApplicationContext() instanceof OtpVerification)
                                                    ((OtpVerification) getApplicationContext()).finish();
                                            }
                                        }
                                    }

                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }


                        });

                    }
                    else {
                        new CheckInternetConnection(OtpVerification.this).showDialog();
                    }
                }
                else
                {
                    Toast.makeText(OtpVerification.this,"Incorrect OTP ! Try Again!",Toast.LENGTH_SHORT).show();
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
    public static void setData(String msg)
    {
        code.setText(msg.substring(64,69));
        verify.setVisibility(View.VISIBLE);
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();

    }
    private void initView()
    {
        verify = (Button) findViewById(R.id.verify);
        code = (MaterialEditText)findViewById(R.id.code);
        mobile = (TextView)findViewById(R.id.mobile);
        mobile.setText("We've sent an OTP via sms to " + value_mobile);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(OtpVerification.this, MobileVerification.class);
            i.putExtra("modelAddress",modelAddress);
            //i.putExtra("modelUser",modelUser);
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
        RocqAnalytics.trackScreen("Enter OTP", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
}

