package com.furniture.appliances.rentals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.dq.rocq.models.Position;
import com.furniture.appliances.rentals.asyncTask.HttpCall;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Config;

import org.json.JSONObject;

/**
 * Created by Infinia on 28-10-2015.
 */
public class Checkout3 extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    TextView total;
    Button submit;
    AppPreferences apref = new AppPreferences();
    ModelOrder modelOrder = new ModelOrder();
    TextView payNow,cod;
    int method=0;

    Activity activity;
    CheckoutFragment co;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelOrder = (ModelOrder)getIntent().getSerializableExtra("modelOrder");

        activity = this;
        co = new CheckoutFragment();
        co.setPublicKey(Config.RAZORPAY_KEY);
        co.setModelOrder(modelOrder);
        setContentView(R.layout.activity_checkout3);
        setUpToolbar();
        initView();
        setView();

    }

    private void initView() {
        activity = Checkout3.this;
        submit = (Button) findViewById(R.id.submit);
        total = (TextView)findViewById(R.id.total);
        payNow = (TextView)findViewById(R.id.payNow);
        cod = (TextView)findViewById(R.id.cod);
        submit.setOnClickListener(this);
        payNow.setOnClickListener(this);
        cod.setOnClickListener(this);
    }

    private void setView()
    {
        total.setText(getResources().getString(R.string.Rs) + " " + String.valueOf(Cart.TOTAL_AMOUNT));
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if(method==0) {
                    try {
                        JSONObject options = new JSONObject("{" +
                                "description: '<Permarent>'," +
                                "image: '<https://www.permarent.com/img/logos/logo.jpg>'," + // can also be base64, if you don't want it to load from network
                                "currency: 'INR'}"
                        );

                        options.put("amount", modelOrder.amountpaid + "00");
                        options.put("name", "Permarent");
                        options.put("prefill", new JSONObject("{email: '" + modelOrder.email + "' , contact: '" + modelOrder.mobileno + "', name: '" + modelOrder.name + "'}"));

                        co.open(activity, options);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    if (new CheckInternetConnection(Checkout3.this).isConnectedToInternet()) {
                        modelOrder.paymentid="COD";
                    new HttpCall().insertOrderDetails(Checkout3.this, modelOrder);
                } else {
                    new CheckInternetConnection(Checkout3.this).showDialog();
                }
                }

                break;
            case R.id.payNow:
                RocqAnalytics.initialize(Checkout3.this);
                RocqAnalytics.trackEvent("Razorpay", new ActionProperties(""), Position.LEFT);
                method=0;
                payNow.setBackgroundResource(R.color.green);
                payNow.setTextColor(getResources().getColor(R.color.white));
                cod.setBackgroundResource(R.drawable.border_bg);
                cod.setTextColor(getResources().getColor(R.color.green));
                break;
            case R.id.cod:
                RocqAnalytics.initialize(Checkout3.this);
                RocqAnalytics.trackEvent("COD", new ActionProperties(""), Position.LEFT);
                method=1;
                cod.setBackgroundResource(R.color.green);
                cod.setTextColor(getResources().getColor(R.color.white));
                payNow.setBackgroundResource(R.drawable.border_bg);
                payNow.setTextColor(getResources().getColor(R.color.green));
                break;


        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(Checkout3.this, Checkout2.class);
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
        RocqAnalytics.trackScreen("Checkout 3", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
}
