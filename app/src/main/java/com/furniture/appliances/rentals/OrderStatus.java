package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.squareup.picasso.Picasso;

/**
 * Created by Rajeev Nagarwal on 12/29/2016.
 */

public class OrderStatus extends AppCompatActivity {
    private Toolbar mToolbar;
    ImageView image;
    TextView text;
    String status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initialize();
        status = getIntent().getStringExtra("status");
        if(status!=null)
        {

            if(status.equals("Placed")) {
                Picasso.with(this).load(R.drawable.status2).into(image);
                text.setText("Order Status: Order Placed");
            }
            else if(status.equals("Dispatched"))
            {
                Picasso.with(this).load(R.drawable.status1).into(image);
                text.setText("Order Status: Order Dispatched");
            }
            else if(status.equals("InProgress"))
            {
                Picasso.with(this).load(R.drawable.status4).into(image);
                text.setText("Order Status: Order in Progress");
            }
            else if(status.equals("Delivered"))
            {
                Picasso.with(this).load(R.drawable.status3).into(image);
                text.setText("Order Status: Order Delivered");
            }

        }
        setUpToolbar();
    }
    private void initialize()
    {
        image = (ImageView)findViewById(R.id.status_img);
        text = (TextView)findViewById(R.id.status_text);
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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            MainActivity.openFragment = 2;
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
