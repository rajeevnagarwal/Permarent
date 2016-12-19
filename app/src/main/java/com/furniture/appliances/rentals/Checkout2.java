package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.adapter.CheckoutAddressAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.GenerateOTP;
import com.furniture.appliances.rentals.util.ListViewHeight;
import com.furniture.appliances.rentals.util.Miscellaneous;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

/**
 * Created by Infinia on 22-09-2015.
 */
public class Checkout2 extends AppCompatActivity {
    private Toolbar mToolbar;
    MaterialEditText email, userName;
    Button submit;
    String value_userName;
    AppPreferences apref = new AppPreferences();
    ModelAddress modelAddress = new ModelAddress();
    ModelOrder modelOrder = new ModelOrder();
    ListView lv;
    CheckoutAddressAdapter checkoutAddressAdapter;
    ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<ModelSubCategory>();
    ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<ModelAddress>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);
        setUpToolbar();
        initView();
        setView();
        getDataFromDb();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createOrder()) {
                    Intent i = new Intent(Checkout2.this,Checkout3.class);
                    i.putExtra("modelOrder",modelOrder);
                    startActivity(i);
                    finish();
                }

            }
        });
    }

    private boolean createOrder() {
        if (validations()) {
            String productList="";
            modelOrder.orderid = "OD"+GenerateOTP.OTP(1, 9) + GenerateOTP.OTP(100000000, 999999999);
            for(int i=0;i<modelSubCategoryArrayList.size();i++)
            {
                ModelSubCategory modelSubCategory = modelSubCategoryArrayList.get(i);
                productList =productList+ modelSubCategory.prod_id+"_"+modelSubCategory.quantity+"|"+modelSubCategory.quantity_quarterly+"|"+modelSubCategory.quantity_monthly+",";
            }
            modelOrder.productlist = productList;
            modelOrder.paymentid = "";
            modelOrder.email = apref.readString(Checkout2.this,"email","");
            modelOrder.name = value_userName;
            modelOrder.address = modelAddress.detail;
            modelOrder.pincode = modelAddress.pincode;
            modelOrder.mobileno = modelAddress.mobile_no;
            modelOrder.amountpaid = String.valueOf(Cart.TOTAL_AMOUNT);
            modelOrder.securitypaid = String.valueOf(Cart.SECURITY_AMOUNT);
            modelOrder.rentpaid = String.valueOf(Cart.TOTAL_AMOUNT-Cart.SECURITY_AMOUNT);
            modelOrder.transactionstatus = "successful";
            modelOrder.invoiceid = "INV"+GenerateOTP.OTP(1, 9) + GenerateOTP.OTP(100000000, 999999999);
            modelOrder.deliverycharges = "0";
            return true;
        }
        return false;
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        userName = (MaterialEditText) findViewById(R.id.userName);
        submit = (Button) findViewById(R.id.submit);
        lv = (ListView) findViewById(R.id.lv);
    }

    private void setView() {
        userName.setText(apref.readString(Checkout2.this, "name", ""));
        //email.setText(apref.readString(Checkout2.this,"email",""));

    }

    private void getDataFromFields() {
        value_userName = userName.getText().toString();
    }

    private boolean validations() {
        getDataFromFields();
        if (Miscellaneous.checkEmptyEditext(Checkout2.this, value_userName, "Contact Details")) {
            return false;
        }
        if (modelAddress.pincode == null) {
            Toast.makeText(Checkout2.this, "Please select atleast one address", Toast.LENGTH_SHORT).show();
            ;
            return false;
        } else
            return true;
    }

    public void setData(ModelAddress modelAddress) {
        this.modelAddress = modelAddress;
    }

    private void refreshAdapter(ArrayList<ModelAddress> result) {
        checkoutAddressAdapter = new CheckoutAddressAdapter(Checkout2.this, result);
        lv.setAdapter(checkoutAddressAdapter);
        ListViewHeight.setListViewHeightBasedOnChildren(lv);
    }

    private void getDataFromDb() {
        DBInteraction dbInteraction = new DBInteraction(Checkout2.this);
        modelAddressArrayList = dbInteraction.getAllAddress();
        modelSubCategoryArrayList=dbInteraction.getSelectedItems();
        dbInteraction.close();
        refreshAdapter(modelAddressArrayList);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(Checkout2.this, Checkout1.class);
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
        RocqAnalytics.trackScreen("Checkout 2", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
}


