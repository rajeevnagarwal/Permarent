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
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelOffer;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.GenerateOTP;
import com.furniture.appliances.rentals.util.ListViewHeight;
import com.furniture.appliances.rentals.util.Miscellaneous;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    ArrayList<ModelCart> list = new ArrayList<ModelCart>();
    ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<ModelSubCategory>();
    ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<ModelAddress>();
    Integer position;
    ArrayList<ModelOffer> offers;
    Boolean coupon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);
        list = (ArrayList<ModelCart>)getIntent().getSerializableExtra("cartarray");
        modelAddressArrayList = (ArrayList<ModelAddress>)getIntent().getSerializableExtra("arrayList");
        position = getIntent().getIntExtra("position",-1);
        offers = (ArrayList<ModelOffer>)getIntent().getSerializableExtra("offers");
        if(offers!=null)
        {
            coupon = true;
        }
        if(modelAddressArrayList==null)
        {
            modelAddressArrayList=new ArrayList<>();
        }
        setUpToolbar();
        initView();
        refreshAdapter(modelAddressArrayList);
        setView();
        //getDataFromDb();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createOrder()) {
                    Intent i = new Intent(Checkout2.this,Checkout3.class);
                    if(coupon) {
                        i.putExtra("modelOrder", modelOrder);
                        i.putExtra("couponCode", offers.get(position).couponCode);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        i.putExtra("modelOrder", modelOrder);
                        startActivity(i);
                        finish();

                    }
                }

            }
        });
    }

    private boolean createOrder() {
        if (validations()) {
            String productList = "";
            String productQty = "";
            String perItemMonthlyRent = "";
            String totalMonthlyRent = "";
            String bookingDuration = "";
            String totalItemSecurity = "";
            String perItemSecurity = "";
            System.out.println("Address" + modelAddress.houseNo + modelAddress.house + modelAddress.localityName + modelAddress.street + modelAddress.city + modelAddress.location + modelAddress.pincode + modelAddress.state + modelAddress.mobile_no);
            modelOrder.orderid = "OD" + GenerateOTP.OTP(1, 9) + GenerateOTP.OTP(100000000, 999999999);
            /*for(int i=0;i<modelSubCategoryArrayList.size();i++)
            {
                ModelSubCategory modelSubCategory = modelSubCategoryArrayList.get(i);
                productList =productList+ modelSubCategory.productName+",";
                productQty = productQty+ modelSubCategory.
            }*/
            for (int i = 0; i < list.size(); i++) {
                ModelCart obj = list.get(i);
                productList = productList + obj.item_name + "_" + obj.prod_id + ",";
                productQty = productQty + obj.quantity + ",";
                perItemMonthlyRent = perItemMonthlyRent + obj.rent_amount + ",";
                totalMonthlyRent = totalMonthlyRent + obj.total_amount + ",";
                perItemSecurity = perItemSecurity + obj.security_amount + ",";
                totalItemSecurity = totalItemSecurity + Integer.parseInt(obj.security_amount) * obj.quantity + ",";
                if (obj.rent_type.equals("0")) {
                    bookingDuration = bookingDuration + "3 months,";
                } else if (obj.rent_type.equals("1")) {
                    bookingDuration = bookingDuration + "6 months,";
                } else if (obj.rent_type.equals("2")) {
                    bookingDuration = bookingDuration + "9 months,";
                } else if (obj.rent_type.equals("3")) {
                    bookingDuration = bookingDuration + "12 months,";
                }


            }
            productList = productList.substring(0, productList.length() - 1);
            bookingDuration = bookingDuration.substring(0, bookingDuration.length() - 1);
            totalItemSecurity = totalItemSecurity.substring(0, totalItemSecurity.length() - 1);
            perItemMonthlyRent = perItemSecurity.substring(0, perItemMonthlyRent.length() - 1);
            perItemSecurity = perItemSecurity.substring(0, perItemSecurity.length() - 1);
            productQty = productQty.substring(0, productQty.length() - 1);
            /*System.out.println("Hello"+productList);
            System.out.println("Hello"+bookingDuration);
            System.out.println("Hello"+totalItemSecurity);
            System.out.println("Hello"+perItemMonthlyRent);
            System.out.println("Hello"+perItemSecurity);
            System.out.println("Hello"+productQty);
            modelOrder.productlist = productList;*/
           /* modelOrder.paymentid = "";
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
            modelOrder.deliverycharges = "0";*/

            modelOrder.productName = productList;
            modelOrder.name = AppPreferences.readString(this, "name", null);
            modelOrder.productQty = productQty;
            modelOrder.perItemMonthlyRent = perItemMonthlyRent;
            modelOrder.totalMonthlyRent = totalMonthlyRent;
            modelOrder.perItemSecurity = perItemSecurity;
            modelOrder.totalItemSecurity = totalItemSecurity;
            modelOrder.bookingDuration = bookingDuration;
            modelOrder.email = AppPreferences.readString(this, "email", null);
            modelOrder.city = modelAddress.city;
            modelOrder.location = modelAddress.location;
            modelOrder.state = modelAddress.state;
            modelOrder.localityName = modelAddress.localityName;
            modelOrder.pincode = modelAddress.pincode;
            modelOrder.mobileno = modelAddress.mobile_no;
            modelOrder.others = modelAddress.others;
            modelOrder.shippingCharges = "250";
            modelOrder.labourCharges = "200";
            modelOrder.totalRental = String.valueOf(Cart.TOTAL_AMOUNT - Cart.SECURITY_AMOUNT);
            modelOrder.totalSecurity = String.valueOf(Cart.SECURITY_AMOUNT);
            if (coupon) {

                ModelOffer offer = offers.get(position);
                try
                {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date start = format.parse(offer.offerStartDate);
                    Date end = format.parse(offer.offerEndDate);
                    Date now = new Date();
                    System.out.println(modelOrder.bookingDuration);
                    System.out.println(offer.getapplicableMonths());
                    if(now.before(end)&&now.after(start))
                    {
                        Integer monthlyRent = Integer.parseInt(modelOrder.totalMonthlyRent);
                        Integer rentdiscount = Integer.parseInt(offer.getMonthlyRent());
                        Integer securitydiscount = Integer.parseInt(offer.getSecurity());
                        Integer shippingChargesdiscount = Integer.parseInt(offer.getshippingCharges());
                        Integer installationChargesdiscount = Integer.parseInt(offer.getinstallationCharges());
                        Integer totalsecurity = Integer.parseInt(modelOrder.totalSecurity);
                        Integer shipping = Integer.parseInt(modelOrder.shippingCharges);
                        monthlyRent = monthlyRent - ((rentdiscount*monthlyRent)/100);
                        totalsecurity = totalsecurity - ((totalsecurity*securitydiscount)/100);
                        shipping = shipping - ((shippingChargesdiscount*shipping)/100);
                        modelOrder.shippingCharges = String.valueOf(shipping);
                        modelOrder.totalMonthlyRent = String.valueOf(monthlyRent);
                        modelOrder.totalSecurity = String.valueOf(totalsecurity);

                    }

                }
                catch(Exception e)
                {

                }
                System.out.println("Offer" + offers.get(position).getapplicableMonths() + offers.get(position).offerEndDate);
            }


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
        userName.setText(AppPreferences.readString(Checkout2.this, "name", ""));
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

   /* private void getDataFromDb() {
        DBInteraction dbInteraction = new DBInteraction(Checkout2.this);
        modelAddressArrayList = dbInteraction.getAllAddress();
        modelSubCategoryArrayList=dbInteraction.getSelectedItems();
        dbInteraction.close();
        refreshAdapter(modelAddressArrayList);
    }*/

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


