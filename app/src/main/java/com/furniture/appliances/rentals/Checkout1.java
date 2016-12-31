package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.adapter.CheckOutAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelOffer;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Infinia on 22-09-2015.
 */
public class Checkout1 extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    TextView rent, security, payable;
    Button submit,apply;
    ListView lv;
    CheckOutAdapter checkOutAdapter;
    LinearLayout capply;
    RelativeLayout cremove;
    ImageButton remove;
    MaterialEditText code;
    ArrayList<ModelCart> modelCartArrayList = new ArrayList<ModelCart>();
    ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<>();
    ArrayList<ModelOffer> offers = new ArrayList<ModelOffer>();
    ArrayList<String> stroffer = new ArrayList<String>();
    AppPreferences apref = new AppPreferences();
    int pos = 0;
    boolean isCouponApplied;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout1);
        setUpToolbar();
        initView();
        getDataFromDb();
        fetchCoupons();
        //setText();
        lv.setAdapter(checkOutAdapter);
    }
    private void fetchCoupons()
    {
        EndPonits.getCoupons(null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure in Coupons");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Success"+responseString);
                try {
                    JSONArray array = new JSONArray(responseString);
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        ModelOffer offer = new ModelOffer();
                        offer.offerId =   obj.getString("offerId");
                        offer.couponCode = obj.getString("couponCode");
                        stroffer.add(offer.couponCode);
                        offer.discountApplicable = obj.getJSONObject("discountApplicable").toString();
                        offer.offerStartDate = obj.getString("offerStartDate");
                        offer.offerEndDate = obj.getString("offerEndDate");
                        offers.add(offer);

                    }
                    setText();

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if(AppPreferences.readString(this,"email",null)!=null) {
                    if (modelAddressArrayList.size() != 0) {
                        if(!isCouponApplied) {
                            Intent i = new Intent(Checkout1.this, Checkout2.class);
                            i.putExtra("cartarray", modelCartArrayList);
                            i.putExtra("arrayList", modelAddressArrayList);
                            startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(Checkout1.this, Checkout2.class);
                            i.putExtra("cartarray", modelCartArrayList);
                            i.putExtra("arrayList", modelAddressArrayList);
                            i.putExtra("position",pos);
                            i.putExtra("offers",offers);
                            startActivity(i);

                        }
                    } else {
                        apref.setIsReadyForCheckout2(Checkout1.this, true);
                        Intent i = new Intent(Checkout1.this, AddNewAddress.class);
                        startActivity(i);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(this,"Please Login first!!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.apply:
                String temp = code.getText().toString();
                if(temp.equals(""))
                    Toast.makeText(Checkout1.this, "Enter coupon code to apply.", Toast.LENGTH_SHORT).show();
                else if(stroffer.contains(temp))
                {
                   Toast.makeText(Checkout1.this, "Coupon code applied successfully.", Toast.LENGTH_SHORT).show();
                    pos = stroffer.indexOf(temp);
                    isCouponApplied = true;
                    /*setText();*/
                   // Toast.makeText(Checkout1.this, "Coupon code not valid.", Toast.LENGTH_SHORT).show();


                }
                else {
                    Toast.makeText(Checkout1.this, "Coupon code not valid.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.remove:
                Toast.makeText(Checkout1.this, "Coupon removed.", Toast.LENGTH_SHORT).show();
                isCouponApplied =false;
                calculateWithoutDiscount();
                setText();
                capply.setVisibility(View.VISIBLE);
                cremove.setVisibility(View.GONE);
                break;


        }
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
        rent = (TextView) findViewById(R.id.rent);
        security = (TextView) findViewById(R.id.security);
        payable = (TextView) findViewById(R.id.payable);
        code = (MaterialEditText)findViewById(R.id.code);
        submit = (Button) findViewById(R.id.submit);
        apply = (Button)findViewById(R.id.apply);
        lv = (ListView) findViewById(R.id.lv);
        capply = (LinearLayout)findViewById(R.id.capply);
        cremove = (RelativeLayout)findViewById(R.id.cremove);
        remove = (ImageButton)findViewById(R.id.remove);
        submit.setOnClickListener(this);
        apply.setOnClickListener(this);
        remove.setOnClickListener(this);

    }

    public void getDataFromDb() {

        DBInteraction dbInteraction = new DBInteraction(Checkout1.this);
        modelCartArrayList = dbInteraction.getCart();
        //modelAddressArrayList = dbInteraction.getAllAddress();
        fetchAddresses(AppPreferences.readString(this,"email",null));
        dbInteraction.close();
        refreshAdapter(modelCartArrayList);
    }
    private void fetchAddresses(String mail)
    {
        if(mail!=null)
        {
            RequestParams params = new RequestParams();
            params.put("email",mail);
            EndPonits.getUserInfo(params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("Failure in Addresses");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    String mobile="";
                    System.out.println("Success"+responseString);
                    try {
                        JSONArray array = new JSONArray(responseString);
                        for(int i=0;i<array.length();i++)
                        {

                            JSONObject obj = array.getJSONObject(i);
                            if(obj.has("contactNos"))
                            {
                                System.out.println("Contact");
                                JSONArray contacts = obj.getJSONArray("contactNos");
                                mobile = String.valueOf(contacts.getLong(0));

                            }
                            if(obj.has("shippingAddresses"))
                            {
                                System.out.println("Found");

                                JSONArray add = obj.getJSONArray("shippingAddresses");
                                for(int j=0;j<add.length();j++)
                                {
                                    ModelAddress model = new ModelAddress();
                                    JSONObject obj_add = add.getJSONObject(j);
                                    System.out.println(obj_add.getString("location"));
                                    model.location = obj_add.getString("location");
                                    model.city = obj_add.getString("city");
                                    model.state = obj_add.getString("state");
                                    model.houseNo = obj_add.getString("houseNo");
                                    model.localityName = obj_add.getString("localityName");
                                    model.pincode = obj_add.getString("pincode");
                                    model.others = obj_add.getString("others");
                                    model.mobile_no = "";
                                    if(obj.has("contactNos"))
                                    {
                                        System.out.println("Contact");
                                        JSONArray contacts = obj.getJSONArray("contactNos");
                                        //mobile = String.valueOf(contacts.getLong(0));
                                        model.mobile_no = String.valueOf(contacts.getLong(0));

                                    }
                                    System.out.println("Contact"+model.mobile_no);

                                    modelAddressArrayList.add(model);

                                }


                            }
                        }
                        if(!mobile.equals(""))
                        {
                            for(int i=0;i<modelAddressArrayList.size();i++)
                            {
                                if(modelAddressArrayList.get(i).mobile_no.equals(""))
                                {
                                    modelAddressArrayList.get(i).mobile_no = mobile;
                                }
                            }

                        }




                    }
                    catch(Exception e)
                    {

                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void refreshAdapter(ArrayList<ModelCart> result)
    {
        checkOutAdapter = new CheckOutAdapter(Checkout1.this, result);
        lv.setAdapter(checkOutAdapter);
        //ListViewHeight.setListViewHeightBasedOnChildren(lv);
    }

    public void setText() {

        if(isCouponApplied)
            calculateDiscount();
        security.setText(getResources().getString(R.string.Rs)+" " + String.valueOf(Cart.SECURITY_AMOUNT));
        rent.setText(getResources().getString(R.string.Rs) + " " + String.valueOf(Cart.TOTAL_AMOUNT - Cart.SECURITY_AMOUNT));
        payable.setText(getResources().getString(R.string.Rs) + " " + String.valueOf(Cart.TOTAL_AMOUNT));
        if(modelCartArrayList.size()==0)
        {
            if(Cart.SECURITY_AMOUNT<0)
                Cart.SECURITY_AMOUNT=0;
            if(Cart.TOTAL_AMOUNT<0)
                Cart.TOTAL_AMOUNT=0;
            Intent i = new Intent(Checkout1.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void calculateDiscount()
    {
        int to = 0,qu=0,sa=0;
        for (int i = 0; i < modelCartArrayList.size(); i++) {
            ModelCart modelCart = modelCartArrayList.get(i);
            int temp_amount = Integer.parseInt(modelCart.total_amount) - Integer.parseInt(modelCart.security_amount);
            int t1 = ((temp_amount * 80) / 100) + ((modelCart.quantity - 1) * (temp_amount));
            to = to + t1;
            qu+=modelCart.quantity;
            sa+=Integer.parseInt(modelCart.security_amount);
        }
        Cart.SECURITY_AMOUNT=sa;
        Cart.TOTAL_AMOUNT=to+sa;
        code.setText("");
        capply.setVisibility(View.GONE);
        cremove.setVisibility(View.VISIBLE);
    }

    public void calculateWithoutDiscount()
    {
        int to = 0,qu=0,sa=0;
        for (int i = 0; i < modelCartArrayList.size(); i++) {
            ModelCart modelCart = modelCartArrayList.get(i);

            int temp_amount = Integer.parseInt(modelCart.total_amount) - Integer.parseInt(modelCart.security_amount);
            int t1 = (modelCart.quantity) * (temp_amount);
            to = to + t1;
            qu+=modelCart.quantity;
            sa+=Integer.parseInt(modelCart.security_amount);
        }
        Cart.SECURITY_AMOUNT=sa;
        Cart.TOTAL_AMOUNT=to+sa;
        code.setText("");
        capply.setVisibility(View.GONE);
        cremove.setVisibility(View.VISIBLE);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(Checkout1.this, MainActivity.class);
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
        RocqAnalytics.trackScreen("Checkout 1", new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
}

