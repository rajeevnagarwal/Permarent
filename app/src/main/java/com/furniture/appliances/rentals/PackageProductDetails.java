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
import android.widget.TextView;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.adapter.PackageProductAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCategory;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.util.AppPreferences;

import java.util.ArrayList;

/**
 * Created by Infinia on 02-10-2015.
 */
public class PackageProductDetails extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private PackageProductAdapter mAdapter;
    TextView menu_quantity;
    ModelSubCategory model = new ModelSubCategory();
    ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<>();
    AppPreferences apref = new AppPreferences();
    ModelCategory modelCategory = new ModelCategory();
    int defaultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_product_details);
        model = (ModelSubCategory) getIntent().getSerializableExtra("model");
        setUpToolbar();
//        getDataFromDb();
        initView();
        String temp_big_img[] = model.largeImages.substring(0, model.largeImages.length()).split(",");
        //temp_big_img = model.big_img.substring(0, model.big_img.length()-1).split(",");
        String temp_material[] = new String[Integer.parseInt(model.package_products)];
        String temp_dimensions[] =new String[Integer.parseInt(model.package_products)];
        String temp_color[] = new String[Integer.parseInt(model.package_products)];
        String temp_type[] =new String[Integer.parseInt(model.package_products)];
        String temp_brand[] = new String[Integer.parseInt(model.package_products)];
        String temp_other_description[] = new String[Integer.parseInt(model.package_products)];
        String temp_quantity[] =new String[Integer.parseInt(model.package_products)];

        String temp[] = model.material.substring(0, model.material.length()-1).split("%");
        if(temp.length!=Integer.parseInt(model.package_products)) {
            for (int i = 0; i < temp.length; i++)
                temp_material[i] = temp[i];
            for (int i = temp.length; i < Integer.parseInt(model.package_products); i++)
                temp_material[i] = "";
        }
        else
            temp_material=temp;
        temp = model.dimensions.substring(0, model.dimensions.length()-1).split("%");
        if(temp.length!=Integer.parseInt(model.package_products)) {
            for (int i = 0; i < temp.length; i++)
                temp_dimensions[i] = temp[i];
            for (int i = temp.length; i < Integer.parseInt(model.package_products); i++)
                temp_dimensions[i] = "";
        }
        else
            temp_dimensions=temp;
        temp = model.color.substring(0, model.color.length()-1).split("%");
        if(temp.length!=Integer.parseInt(model.package_products)) {
            for (int i = 0; i < temp.length; i++)
                temp_color[i] = temp[i];
            for (int i = temp.length; i < Integer.parseInt(model.package_products); i++)
                temp_color[i] = "";
        }
        else
            temp_color=temp;
        temp = model.type.substring(0, model.type.length()-1).split("%");
        if(temp.length!=Integer.parseInt(model.package_products)) {
            for (int i = 0; i < temp.length; i++)
                temp_type[i] = temp[i];
            for (int i = temp.length; i < Integer.parseInt(model.package_products); i++)
                temp_type[i] = "";
        }
        else
            temp_type=temp;
        temp = model.brand.substring(0, model.brand.length()-1).split("%");
        if(temp.length!=Integer.parseInt(model.package_products)) {
            for (int i = 0; i < temp.length; i++)
                temp_brand[i] = temp[i];
            for (int i = temp.length; i < Integer.parseInt(model.package_products); i++)
                temp_brand[i] = "";
        }
        else
            temp_brand=temp;
        temp = model.otherDesc.substring(0, model.otherDesc.length() - 1).split("%");
        if(temp.length!=Integer.parseInt(model.package_products)) {
            for (int i = 0; i < temp.length; i++)
                temp_other_description[i] = temp[i];
            for (int i = temp.length; i < Integer.parseInt(model.package_products); i++)
                temp_other_description[i] = "";
        }
        else
            temp_other_description=temp;

        temp = model.max_quantity.substring(0, model.max_quantity.length() - 1).split("%");
        if(temp.length!=Integer.parseInt(model.package_products)) {
            for (int i = 0; i < temp.length; i++)
                temp_quantity[i] = temp[i];
            for (int i = temp.length; i < Integer.parseInt(model.package_products); i++)
                temp_quantity[i] = "";
        }
        else
            temp_quantity=temp;
        for (int i = 0; i < Integer.parseInt(model.package_products); i++) {
            ModelSubCategory modelSubCategory = new ModelSubCategory();
//            modelSubCategory.prod_code = model.prod_code;
            modelSubCategory.productId = model.productId;
            modelSubCategory.vendorId = model.vendorId;
            modelSubCategory.categoryId = model.categoryId;
            modelSubCategory.categoryName =  model.categoryName;
            modelSubCategory.subCategoryId = model.subCategoryId;
            modelSubCategory.subCategoryName =  model.subCategoryName;
            modelSubCategory.smallImages = model.smallImages;
            modelSubCategory.largeImages = temp_big_img[i];
            if (temp_material.length != 0)
                modelSubCategory.material = temp_material[i];
            else
                modelSubCategory.material = "";
            if (temp_dimensions.length != 0)
                modelSubCategory.dimensions = temp_dimensions[i];
            else
                modelSubCategory.dimensions = "";
            if (temp_color.length != 0)
                modelSubCategory.color = temp_color[i];
            else
                modelSubCategory.color = "";
            if (temp_type.length != 0)
            modelSubCategory.type = temp_type[i];
            else
                modelSubCategory.type = "";
            if (temp_brand.length != 0)
            modelSubCategory.brand = temp_brand[i];
            else
                modelSubCategory.brand = "";
            if (temp_other_description.length != 0)
            modelSubCategory.otherDesc = temp_other_description[i];
            else
                modelSubCategory.otherDesc = "";
            modelSubCategory.max_quantity = temp_quantity[i];
            modelSubCategory.rentalAmount = model.rentalAmount;
//            modelSubCategory.rent_duration = model.rent_duration;
            modelSubCategory.securityAmount = model.securityAmount;
            modelSubCategory.minRentalDuration = model.minRentalDuration;
//            modelSubCategory.shipping_charges = model.shipping_charges;
//            modelSubCategory.created_on = model.created_on ;
            modelSubCategory.quantity_threeMo = model.quantity_threeMo;
            modelSubCategory.quantity_sixMo = model.quantity_sixMo;
            modelSubCategory.quantity_nineMo = model.quantity_nineMo;
            modelSubCategory.quantity_twelveMo = model.quantity_twelveMo;
            modelSubCategoryArrayList.add(modelSubCategory);
        }

        mAdapter = new PackageProductAdapter(getSupportFragmentManager(), PackageProductDetails.this, modelSubCategoryArrayList,model);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /*private void getDataFromDb() {
        DBInteraction dbInteraction = new DBInteraction(PackageProductDetails.this);
//        modelCategory = dbInteraction.getCategoryByName(model.category_desc);
        String temp[] = modelCategory.subcategory.split(",");
        for(int i=0;i<temp.length;i++) {
            if(temp[i].equalsIgnoreCase(model.subcategory))
                defaultFragment=i;
        }
        dbInteraction.close();
    }*/
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(PackageProductDetails.this, Category.class);
            i.putExtra("modelCategory", modelCategory);
            i.putExtra("defaultFragment", defaultFragment);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

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
                    if (!apref.IsLoginedByEmail(PackageProductDetails.this) && !apref.IsLoginedByGoogle(PackageProductDetails.this) && !apref.IsLoginedByFb(PackageProductDetails.this)) {
                        apref.setIsReadyForCheckout1(PackageProductDetails.this, true);
                        Intent i = new Intent(PackageProductDetails.this, Login.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(PackageProductDetails.this, Checkout1.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(PackageProductDetails.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }


    public void updateMenu()
    {
        menu_quantity.setText(String.valueOf(Cart.QUANTITY));
    }

    @Override
    protected void onStart() {
        super.onStart();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Product Details " + model.subCategoryName, new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
}
