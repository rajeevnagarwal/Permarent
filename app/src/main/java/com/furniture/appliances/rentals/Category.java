package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.adapter.CartAdapter;
import com.furniture.appliances.rentals.adapter.SubCategoryAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelCategory;
import com.furniture.appliances.rentals.ui.SlidingTabLayout;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

/**
 * Created by Infinia on 16-10-2015.
 */
public class Category extends AppCompatActivity {

    Toolbar mToolbar;
    TextView title,menu_quantity;
    ImageView spinner;
    ViewPager pager;
    SubCategoryAdapter adapter;
    ListView lv;
    SlidingTabLayout tabs;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> titlecode = new ArrayList<>();
    int defaultFragment;

    RelativeLayout cart;
    ArrayList<ModelCart> modelCartArrayList = new ArrayList<ModelCart>();
    ModelCategory modelCategory = new ModelCategory();
    public static CartAdapter cartAdapter;
    Button checkout;
    TextView price,quantity;
    AppPreferences apref = new AppPreferences();
    RelativeLayout rl1;
    private SlidingUpPanelLayout mLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        modelCategory = (ModelCategory) getIntent().getSerializableExtra("modelCategory");
        defaultFragment = getIntent().getIntExtra("defaultFragment",0);
        setUpToolbar();
        initView();
        setData();
        getDataFromDb();

        tabs.setDistributeEvenly(true);
        System.out.println(modelCategory.subcategory);
        String temp[] = modelCategory.subcategory.split(",");
        for(int i=0;i<temp.length;i++)
            titles.add(temp[i]);
        temp = modelCategory.subcategoryid.split(",");
        for(int i=0;i<temp.length;i++)
        {
            titlecode.add(temp[i]);
        }

        adapter = new SubCategoryAdapter(getSupportFragmentManager(),titles,titlecode,modelCategory);
        pager.setAdapter(adapter);
        pager.setCurrentItem(defaultFragment);
        tabs.setViewPager(pager);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cart.QUANTITY != 0) {
                    if (!apref.IsLoginedByEmail(Category.this) && !apref.IsLoginedByGoogle(Category.this) &&!apref.IsLoginedByFb(Category.this)) {
                        apref.setIsReadyForCheckout1(Category.this, true);
                        Intent i = new Intent(Category.this, Login.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(Category.this, Checkout1.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(Category.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void getDataFromDb() {

        DBInteraction dbInteraction = new DBInteraction(Category.this);
        modelCartArrayList = dbInteraction.getCart();
        cartAdapter = new CartAdapter(Category.this, modelCartArrayList);
        dbInteraction.close();
        refreshAdapter(modelCartArrayList);
    }

    private void refreshAdapter(ArrayList<ModelCart> result)
    {
        cartAdapter = new CartAdapter(Category.this, result);
        lv.setAdapter(cartAdapter);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        spinner = (ImageView) findViewById(R.id.spinner);
        title.setText(modelCategory.categoryName);
        spinner.setVisibility(View.INVISIBLE);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
                    if (!apref.IsLoginedByEmail(Category.this) && !apref.IsLoginedByGoogle(Category.this) && !apref.IsLoginedByFb(Category.this)) {
                        apref.setIsReadyForCheckout1(Category.this, true);
                        Intent i = new Intent(Category.this, Login.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(Category.this, Checkout1.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(Category.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(Category.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public void setData()
    {
        price.setText(getResources().getString(R.string.Rs)+" "+String.valueOf(Cart.TOTAL_AMOUNT));
        quantity.setText(String.valueOf(Cart.QUANTITY));


    }

    public void updateMenu()
    {
        menu_quantity.setText(String.valueOf(Cart.QUANTITY));
        if(Cart.QUANTITY==0)
        {
            lv.setVisibility(View.GONE);
            rl1.setVisibility(View.VISIBLE);
        }
        else
        {
            lv.setVisibility(View.VISIBLE);
            rl1.setVisibility(View.GONE);
        }

    }

    private void initView()
    {
        price = (TextView)findViewById(R.id.price);
        quantity = (TextView)findViewById(R.id.quantity);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        checkout = (Button)findViewById(R.id.checkout);
        lv = (ListView) findViewById(R.id.list);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);

        if(Cart.QUANTITY==0)
        {
            lv.setVisibility(View.GONE);
            rl1.setVisibility(View.VISIBLE);
        }
        else
        {
            lv.setVisibility(View.VISIBLE);
            rl1.setVisibility(View.GONE);
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen(modelCategory.categoryName, new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }







}

