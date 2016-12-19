package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.furniture.appliances.rentals.adapter.ComplaintOrderAdapter;
import com.furniture.appliances.rentals.adapter.ComplaintProductAdapter;
import com.furniture.appliances.rentals.model.ModelComplaintOrder;
import com.furniture.appliances.rentals.model.ModelComplaintProduct;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.util.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class ProductComplaintActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ModelComplaintOrder order;
    private RecyclerView product_complaint_recycle;
    ArrayList<ModelComplaintProduct> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productcomplaint);
        setUpToolbar();
        initialize();
        parseJSONArray(order.getComplaints());
        setView();

    }

    private void setView() {
        ComplaintProductAdapter adapter = new ComplaintProductAdapter(list, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        product_complaint_recycle.setLayoutManager(layoutManager);

        product_complaint_recycle.setAdapter(adapter);
    }

    private void initialize() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        list = new ArrayList<ModelComplaintProduct>();
        order = (ModelComplaintOrder) getIntent().getSerializableExtra("order");
        product_complaint_recycle = (RecyclerView) findViewById(R.id.product_complaint_recyclerView);

    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void parseJSONArray(String res) {
        try {
            JSONArray array = new JSONArray(res);
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                list.add(new ModelComplaintProduct(json.getString("productName"), json.getString("complaintDate"), json.getString("currentStatus"), json.getString("reason")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            MainActivity.openFragment = 10;
            Intent i = new Intent(ProductComplaintActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


}

