package com.furniture.appliances.rentals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.parser.ParseApi;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.service.SyncService;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rajeev Nagarwal on 12/6/2016.
 */

public class ComplaintActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ModelSubCategory item;
    private LinearLayout linear_layout;
    String prod_name;
    String quantity;
    private Button submit_complaint;
    private TextView item_name;
    private Spinner spinner_quantity;
    private EditText edit_description;
    private ImageView img_item;
    private String def_quantity="",category="",desc="";
    private ProgressDialog progressDialog;;
    private String orderId;
    private Integer counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        initialize();
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
    private void initialize()
    {
        item_name = (TextView)findViewById(R.id.item_name);
        spinner_quantity = (Spinner)findViewById(R.id.spinner_quantity);
        img_item = (ImageView)findViewById(R.id.img_item);
        submit_complaint = (Button)findViewById(R.id.submit_complaint);
        edit_description= (EditText)findViewById(R.id.edit_desc);
        item = (ModelSubCategory) getIntent().getSerializableExtra("item");
        orderId = (String)getIntent().getStringExtra("orderId");
        Picasso.with(this)
                .load(Config.subCategoryImage + item.small_img)
                .into(img_item);
        prod_name = (item.prod_name);
        item_name.setText(prod_name);
        System.out.println(item.small_img);
        if(item.quantity!=0)
            quantity= (String.valueOf(item.quantity));
        if(item.quantity_monthly!=0)
            quantity = (String.valueOf(item.quantity_monthly));
        if(item.quantity_quarterly!=0)
            quantity = (String.valueOf(item.quantity_quarterly));
       counter = Integer.parseInt(quantity);
        final String[] arr = new String[Integer.parseInt(quantity)];
        if(quantity.equals("1")) {
            arr[0] = "1";

        }
        else {

            for (int i = 0; i <= Integer.parseInt(quantity)-1; i++) {
                arr[i] = String.valueOf(i)+1;
            }
        }
        ArrayAdapter<String> quantity_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arr);
        spinner_quantity.setAdapter(quantity_adapter);
        spinner_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                def_quantity = arr[position];

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void onSubmit(View v)
    {
        desc = edit_description.getText().toString();

        if(desc.length()==0)
        {
            Toast.makeText(this,"Please fill description field",Toast.LENGTH_SHORT).show();
        }
        else
        {
                if(counter!=0) {
                    if (new CheckInternetConnection(this).isConnectedToInternet()) {
                        registerComplaint(desc, prod_name, orderId);
                    } else {
                        new CheckInternetConnection(this).showDialog();
                    }
                }


        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Complaint registration", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
    public void registerComplaint(final String reason, final String name, final String id)
    {

        RequestParams params = new RequestParams();
        params.put("reason", reason);
        params.put("productName",name);
        params.put("orderId",id);
        System.out.println(id);
        EndPonits.registerComplaint(params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(ComplaintActivity.this);
                progressDialog.setMessage("Registering your complaint...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure");
                System.out.println(responseString);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                counter--;
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                RocqAnalytics.initialize(ComplaintActivity.this);
                System.out.println(responseString);
                try {
                    JSONObject json = new JSONObject(responseString);
                    if(json.getBoolean("success")==true)
                    {
                        Toast.makeText(getApplicationContext(),"Complaint Registered Successfully",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {

                }
                if(counter>0)
                {
                 registerComplaint(reason, name, id);
                }



            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            MainActivity.openFragment = 2;
            Intent i = new Intent(ComplaintActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }





}
