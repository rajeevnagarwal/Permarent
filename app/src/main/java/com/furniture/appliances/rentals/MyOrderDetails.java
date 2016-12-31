package com.furniture.appliances.rentals;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.adapter.OrderDetailsAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Infinia on 13-11-2015.
 */
public class MyOrderDetails extends AppCompatActivity {

    private Toolbar mToolbar;
    TextView rent, security;
    ModelOrder modelOrder = new ModelOrder();
    OrderDetailsAdapter orderDetailsAdapter;
    ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<>();
    private ListView lv;
    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelOrder = (ModelOrder) getIntent().getSerializableExtra("model");
        orderId = modelOrder.orderid;
        setContentView(R.layout.activity_myorders_details);
        initView();
        setData();
        fetchProducts();
        //getDataFromDb();
        setUpToolbar();
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setData()
    {
        security.setText(getResources().getString(R.string.Rs) + " " + String.valueOf(modelOrder.totalRental));
        rent.setText(getResources().getString(R.string.Rs) + " " + String.valueOf(modelOrder.totalSecurity));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            MainActivity.openFragment = 2;
            Intent i = new Intent(MyOrderDetails.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
    private void fetchProducts()
    {
        System.out.println("Product"+modelOrder.productName);
        try
        {
            JSONArray array = new JSONArray(modelOrder.productName);
            for(int i=0;i<array.length();i++)
            {
                JSONObject obj = array.getJSONObject(i);
                ModelSubCategory model = new ModelSubCategory();
                String sen = obj.getString("productName");
                if(sen.contains("_"))
                {
                    model.productName = sen.split("_")[0];
                    model.productId = sen.split("_")[1];
                    if(obj.getString("bookingDuration").equals("3 months"))
                    {
                        model.quantity_threeMo = Integer.parseInt(obj.getString("quantity"));
                        model.quantity_nineMo = 0;
                        model.quantity_sixMo = 0;
                        model.quantity_twelveMo = 0;
                    }
                    else if(obj.getString("bookingDuration").equals("6 months"))
                    {
                        model.quantity_threeMo = 0;
                        model.quantity_nineMo = 0;
                        model.quantity_sixMo = Integer.parseInt(obj.getString("quantity"));
                        model.quantity_twelveMo = 0;

                    }
                    else if(obj.getString("bookingDuration").equals("9 months"))
                    {
                        model.quantity_threeMo = 0;
                        model.quantity_nineMo = Integer.parseInt(obj.getString("quantity"));
                        model.quantity_sixMo = 0;
                        model.quantity_twelveMo = 0;

                    }
                    else if(obj.getString("bookingDuration").equals("12 months"))
                    {
                        model.quantity_threeMo = 0;
                        model.quantity_nineMo = 0;
                        model.quantity_sixMo = 0;
                        model.quantity_twelveMo = Integer.parseInt(obj.getString("quantity"));

                    }
                    fetchImage(model);
                    refreshAdapter(modelSubCategoryArrayList);
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void fetchImage(final ModelSubCategory model)
    {
        RequestParams params = new RequestParams();
        params.put("productId",model.productId);
        EndPonits.getProductDetails(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure in ProductImage");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONArray array = new JSONArray(responseString);
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        model.smallImages = obj.getJSONArray("smallImages").toString();
                        model.largeImages = obj.getJSONArray("largeImages").toString();
                        modelSubCategoryArrayList.add(model);
                        orderDetailsAdapter.notifyDataSetChanged();



                    }

                }
                catch(Exception e)
                {

                }

            }
        });
    }



    /*private void getDataFromDb() {
        String temp[] = modelOrder.productlist.split(",");
        System.out.println(modelOrder.productlist);
        DBInteraction dbInteraction = new DBInteraction(MyOrderDetails.this);
        for(int i=0;i<temp.length;i++) {
            String temp2[] = temp[i].split("_");
            ModelSubCategory modelSubCategory = temp2[0];
            System.out.println(modelSubCategory);
            if(temp2[1].contains("|")) {
                String quantity[] = temp2[1].split("|");
                modelSubCategory.quantity_threeMo = Integer.parseInt(quantity[1]);
                modelSubCategory.quantity_sixMo = Integer.parseInt(quantity[3]);
                modelSubCategory.quantity_nineMo = Integer.parseInt(quantity[5]);
                modelSubCategory.quantity_twelveMo = Integer.parseInt(quantity[7]);
                modelSubCategoryArrayList.add(modelSubCategory);
            }
        }
        dbInteraction.close();
        refreshAdapter(modelSubCategoryArrayList);
    }*/

    private void refreshAdapter(ArrayList<ModelSubCategory> result)
    {
        orderDetailsAdapter = new OrderDetailsAdapter(MyOrderDetails.this,result);
        lv.setAdapter(orderDetailsAdapter);
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        rent = (TextView) findViewById(R.id.rent);
        security = (TextView) findViewById(R.id.security);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
               Intent i = new Intent(getApplicationContext(),ComplaintActivity.class);
                i.putExtra("item",modelSubCategoryArrayList.get(position));
                i.putExtra("orderId",orderId);
                startActivity(i);
            }} );
    }

    @Override
    protected void onStart() {
        super.onStart();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("My Order Details", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
}
