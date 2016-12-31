package com.furniture.appliances.rentals;

import android.app.ActionBar;
import android.app.LocalActivityManager;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.furniture.appliances.rentals.adapter.SearchAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.fragment.Account;
import com.furniture.appliances.rentals.fragment.Addresses;
import com.furniture.appliances.rentals.fragment.CategoryFragment;
import com.furniture.appliances.rentals.fragment.ComplaintsFragment;
import com.furniture.appliances.rentals.fragment.ContactUs;
import com.furniture.appliances.rentals.fragment.Documents;
import com.furniture.appliances.rentals.fragment.DrawerFragment;
import com.furniture.appliances.rentals.fragment.FAQ;
import com.furniture.appliances.rentals.fragment.Home;
import com.furniture.appliances.rentals.fragment.HomeFragment;
import com.furniture.appliances.rentals.fragment.MyOrders;
import com.furniture.appliances.rentals.fragment.OurServices;
import com.furniture.appliances.rentals.fragment.PrivacyPolicy;
import com.furniture.appliances.rentals.fragment.ReturnAndRefund;
import com.furniture.appliances.rentals.fragment.TermsAndConditions;
import com.furniture.appliances.rentals.fragment.WishList;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;
import com.furniture.appliances.rentals.util.NavigationDrawerCallbacks;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements /*NavigationDrawerCallbacks,*/GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Toolbar mToolbar;
    TextView title,menu_quantity;
    ImageView spinner;
    LinearLayout location;
    private DrawerFragment drawerFragment;
    AppPreferences apref = new AppPreferences();
    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;
    ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    public static int openFragment;
    public static FragmentTabHost tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        apref.setIsLogined(MainActivity.this, true);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        initView();
        setView();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        if(Cart.TOTAL_AMOUNT==0 && Cart.QUANTITY==0)
        {
            DBInteraction dbInteraction = new DBInteraction(MainActivity.this);
            modelCartArrayList = dbInteraction.getCart();
            dbInteraction.close();
            int to = 0,qu=0,sa=0;
            for (int i = 0; i < modelCartArrayList.size(); i++) {
                ModelCart modelCart = modelCartArrayList.get(i);
                to =to+(modelCart.quantity*Integer.parseInt(modelCart.total_amount));
                qu+=modelCart.quantity;
                sa+=Integer.parseInt(modelCart.security_amount);
            }
            Cart.QUANTITY=qu;
            Cart.TOTAL_AMOUNT=to;
            Cart.SECURITY_AMOUNT=sa;
        }
        if(openFragment==2) {
            openSpecifiedFragment(2);
            MainActivity.openFragment = 0;
        }

    }


    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView)findViewById(R.id.title);
        //title.setText(Config.AppName);
        //spinner = (ImageView)findViewById(R.id.spinner);
        //location = (LinearLayout)findViewById(R.id.location);
        /*location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.OPENED_FRAGMENT != null) {
                    if (Config.OPENED_FRAGMENT instanceof Home) {
                        Intent i = new Intent(MainActivity.this, SearchCity.class);
                        startActivity(i);
                    }
                    else if(Config.OPENED_FRAGMENT instanceof HomeFragment)
                    {
                        Intent i = new Intent(MainActivity.this, SearchCity.class);
                        startActivity(i);
                    }

                }
            }
        });*/
        if (mToolbar != null) {
           // title.setText(Config.AppName);
            mToolbar.setTitle(Config.AppName);
            mToolbar.setLogo(R.drawable.ic_logo);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void changeToolbar(String name,boolean isFragmentHome)
    {
        title.setText(name);
        /*if(isFragmentHome)
            spinner.setVisibility(View.VISIBLE);
        else
            spinner.setVisibility(View.INVISIBLE);*/
    }

    private void initView()
    {
        tabhost = (FragmentTabHost)findViewById(R.id.tabhost);
        View tabview = getLayoutInflater().inflate(R.layout.custom_tabview,tabhost,false);
        tabhost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        ((TextView)tabview.findViewById(R.id.tab_text)).setText("Home");
        ((ImageView)tabview.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(R.drawable.home_tab_drawable));
        tabview.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
        tabhost.addTab(tabhost.newTabSpec("Home").setIndicator(tabview),HomeFragment.class,null);
        View tabview1 = getLayoutInflater().inflate(R.layout.custom_tabview,tabhost,false);
        ((TextView)tabview1.findViewById(R.id.tab_text)).setText("Categories");
        ((ImageView)tabview1.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(R.drawable.category_tab_drawable));
        tabview1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.5f));
        tabhost.addTab(tabhost.newTabSpec("Categories").setIndicator(tabview1),CategoryFragment.class,null);
        View tabview2 = getLayoutInflater().inflate(R.layout.custom_tabview,tabhost,false);
        ((TextView)tabview2.findViewById(R.id.tab_text)).setText("Rent-to-Own");
        ((ImageView)tabview2.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(R.drawable.service_tab_drawable));
        tabview2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.5f));
        tabhost.addTab(tabhost.newTabSpec("Rent-to-own").setIndicator(tabview2),OurServices.class,null);
        View tabview3 = getLayoutInflater().inflate(R.layout.custom_tabview,tabhost,false);
        ((TextView)tabview3.findViewById(R.id.tab_text)).setText("WishList");
        ((ImageView)tabview3.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(R.drawable.heart_tab_drawable));
        tabview3.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
        tabhost.addTab(tabhost.newTabSpec("Wish list").setIndicator(tabview3),WishList.class,null);
        View tabview4 = getLayoutInflater().inflate(R.layout.custom_tabview,tabhost,false);
        ((TextView)tabview4.findViewById(R.id.tab_text)).setText("Account");
        ((ImageView)tabview4.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(R.drawable.account_tab_selected));
        tabview4.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
        tabhost.addTab(tabhost.newTabSpec("Account").setIndicator(tabview4),Account.class,null);
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {



                if(tabId.equals(HomeFragment.TAG))
                {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
                    if(fragment==null)
                    {
                        fragment = new HomeFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,HomeFragment.TAG).addToBackStack(HomeFragment.TAG).commit();


                }
                else if(tabId.equals(CategoryFragment.TAG))
                {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(CategoryFragment.TAG);
                    if(fragment==null)
                    {
                        fragment = new CategoryFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,CategoryFragment.TAG).addToBackStack(CategoryFragment.TAG).commit();



                }
                else if(tabId.equals(OurServices.TAG))
                {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(OurServices.TAG);
                    if(fragment==null)
                    {
                        fragment = new OurServices();
                    }
                    getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,OurServices.TAG).addToBackStack(OurServices.TAG).commit();



                }
                else if(tabId.equals(WishList.TAG))
                {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(WishList.TAG);
                    if(fragment==null)
                    {
                        fragment = new WishList();
                    }
                    getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,WishList.TAG).addToBackStack(WishList.TAG).commit();



                }
                else if(tabId.equals(Account.TAG))
                {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(Account.TAG);
                    if(fragment==null)
                    {
                        fragment = new Account();
                    }
                    getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,Account.TAG ).addToBackStack(Account.TAG).commit();


                }

            }
        });
       /* drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);*/
    }

    public void openSpecifiedFragment(int no)
    {
       // drawerFragment.selectItem(no);
        tabhost.setCurrentTab(no);
    }


    private void setView()
    {
       // drawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        //drawerFragment.setUserData(MainActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.ab_cart);
        MenuItemCompat.setActionView(item, R.layout.cart_badge);
        View view = MenuItemCompat.getActionView(item);
        menu_quantity = (TextView) view.findViewById(R.id.quantity);
        menu_quantity.setText(String.valueOf(Cart.QUANTITY));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cart.QUANTITY != 0) {
                    if (!apref.IsLoginedByEmail(MainActivity.this) && !apref.IsLoginedByGoogle(MainActivity.this) &&!apref.IsLoginedByFb(MainActivity.this)) {
                        apref.setIsReadyForCheckout1(MainActivity.this, true);
                        Intent i = new Intent(MainActivity.this, Login.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(MainActivity.this, Checkout1.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
       /* if (!drawerFragment.isDrawerOpen()) {

            return true;
        }*/
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.ab_cart)
            Toast.makeText(MainActivity.this,"as",Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
       /* if (drawerFragment.isDrawerOpen())
            drawerFragment.closeDrawer();
        else*/
            super.onBackPressed();
    }

    /*@Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = getSupportFragmentManager().findFragmentByTag(Home.TAG);
                if (fragment == null) {
                    fragment = new Home();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, Home.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("Home", new ActionProperties());
                break;
            case 1:
                fragment = getSupportFragmentManager().findFragmentByTag(Addresses.TAG);
                if (fragment == null) {
                    fragment = new Addresses();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, Addresses.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("Address", new ActionProperties());
                break;
            case 2:
                fragment = getSupportFragmentManager().findFragmentByTag(MyOrders.TAG);
                if (fragment == null) {
                    fragment = new MyOrders();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, MyOrders.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("My Orders", new ActionProperties());
                break;
            case 3:
                fragment = getSupportFragmentManager().findFragmentByTag(ContactUs.TAG);
                if(fragment==null)
                {
                    fragment = new ContactUs();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,ContactUs.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("Contact Us",new ActionProperties());
                break;

            case 4:
                String url = "";
                try {
                    //Check whether Google Play store is installed or not:
                    this.getPackageManager().getPackageInfo("com.android.vending", 0);
                    url = Config.playStoreUrl;
                } catch ( final Exception e ) {
                    url = Config.browserUrl;
                }
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(in);
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("Rate", new ActionProperties());
                break;
            case 5:
                fragment = getSupportFragmentManager().findFragmentByTag(FAQ.TAG);
                if (fragment == null) {
                    fragment = new FAQ();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, FAQ.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("FAQ", new ActionProperties());
                break;
            case 6:
                fragment = getSupportFragmentManager().findFragmentByTag(TermsAndConditions.TAG);
                if (fragment == null) {
                    fragment = new TermsAndConditions();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, TermsAndConditions.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("T&C", new ActionProperties());
                break;
            case 7:
                fragment = getSupportFragmentManager().findFragmentByTag(PrivacyPolicy.TAG);
                if (fragment == null) {
                    fragment = new PrivacyPolicy();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, PrivacyPolicy.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("Privacy Policy", new ActionProperties());
                break;
            case 8:
                fragment = getSupportFragmentManager().findFragmentByTag(Documents.TAG);
                if(fragment==null)
                {
                    fragment = new Documents();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, Documents.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("Documents Required", new ActionProperties());
                break;
            case 9:
                fragment = getSupportFragmentManager().findFragmentByTag(ReturnAndRefund.TAG);
                if(fragment==null)
                {
                    fragment = new ReturnAndRefund();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, ReturnAndRefund.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("Return and Refund Policy", new ActionProperties());
                break;
            case 10:
                fragment = getSupportFragmentManager().findFragmentByTag(ComplaintsFragment.TAG);
                if(fragment==null)
                {
                    fragment = new ComplaintsFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,ComplaintsFragment.TAG).commit();
                RocqAnalytics.initialize(this);
                RocqAnalytics.trackScreen("Complaints", new ActionProperties());
                break;
            case 11:
                if(!apref.IsLoginedByFb(MainActivity.this) && !apref.IsLoginedByGoogle(MainActivity.this) &&!apref.IsLoginedByEmail(MainActivity.this)) {
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                }
                if(apref.IsLoginedByEmail(MainActivity.this))
                {
                    Cart.QUANTITY=0;
                    Cart.TOTAL_AMOUNT=0;
                    DBInteraction dbInteraction = new DBInteraction(MainActivity.this);
                    dbInteraction.resetProducts(0, 0, 0);
                    dbInteraction.resetCart(0);
                    dbInteraction.clearDatabase();
                    dbInteraction.close();
                    MainActivity.this.getSharedPreferences("permarent", 0).edit().clear().commit();
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                if(apref.IsLoginedByGoogle(MainActivity.this))
                {
                    Cart.QUANTITY=0;
                    Cart.TOTAL_AMOUNT=0;
                    DBInteraction dbInteraction = new DBInteraction(MainActivity.this);
                    dbInteraction.resetProducts(0, 0, 0);
                    dbInteraction.resetCart(0);
                    dbInteraction.clearDatabase();
                    dbInteraction.close();
                    MainActivity.this.getSharedPreferences("permarent", 0).edit().clear().commit();
                    if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient.connect();
                    }
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                if(apref.IsLoginedByFb(MainActivity.this))
                {
                    Cart.QUANTITY=0;
                    Cart.TOTAL_AMOUNT=0;
                    DBInteraction dbInteraction = new DBInteraction(MainActivity.this);
                    dbInteraction.resetProducts(0, 0, 0);
                    dbInteraction.resetCart(0);
                    dbInteraction.clearDatabase();
                    dbInteraction.close();
                    MainActivity.this.getSharedPreferences("permarent", 0).edit().clear().commit();
                    LoginManager.getInstance().logOut();
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
    }*/
    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        mSignInClicked = false;

    }
    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        mGoogleApiClient.connect();
        // updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Main Activity", new ActionProperties());
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        RocqAnalytics.stopScreen(this);
    }




}
