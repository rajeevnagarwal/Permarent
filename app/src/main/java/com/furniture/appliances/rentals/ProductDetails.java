package com.furniture.appliances.rentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.dq.rocq.models.Position;
import com.furniture.appliances.rentals.adapter.CouponAdapter;
import com.furniture.appliances.rentals.adapter.ReviewAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelCategory;
import com.furniture.appliances.rentals.model.ModelReviews;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Infinia on 18-09-2015.
 */
public class ProductDetails extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    TextView rent_amount, menu_quantity, security_deposit, min_rent_period, material, dimensions, color, type, brand, other_description,rto;
    ImageView image,wish,like;
    Button add;
    RelativeLayout rl_dimensions, rl_material, rl_color, rl_type, rl_brand, rl_other;
    ModelCategory modelCategory = new ModelCategory();
    ModelSubCategory model = new ModelSubCategory();
    AppPreferences apref = new AppPreferences();
    ViewPager viewPager;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    int defaultFragment;
    Boolean liked=false;
    ListView lv;
    public static final String[] IMAGE_NAME = new String[2];
    ArrayList<ModelReviews> reviews;
    private RatingBar ratingbar;
    private TextView rating_text;
    private EditText edit_review;
    Button button_review;
    ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (ModelSubCategory) getIntent().getSerializableExtra("model");
        modelCategory = (ModelCategory)getIntent().getSerializableExtra("category");
        setContentView(R.layout.activity_product_details);
        IMAGE_NAME[0] = model.big_img;
        IMAGE_NAME[1] = "singlebed000003_big.jpg";
        initView();
        fetchproduct(model.prod_id);
        setData();
        getDataFromDb();
        setUpToolbar();
        viewPager.setAdapter(imageFragmentPagerAdapter);
    }
    private void fetchproduct(String id)
    {
        RequestParams params = new RequestParams();
        params.put("productId",id);
        EndPonits.getProductDetails(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONArray array = new JSONArray(responseString);
                    JSONObject object = array.getJSONObject(0);
                    JSONArray users = object.getJSONArray("productUserLikesList");
                    for(int i=0;i<users.length();i++)
                    {
                        JSONObject obj = users.getJSONObject(i);
                        if(obj.getString("email").equals(apref.readString(getApplicationContext(),"email","")))
                        {
                            liked = true;
                            break;

                        }
                    }
                    JSONArray r = object.getJSONArray("productReviews");
                    for(int i=0;i<r.length();i++)
                    {
                        JSONObject obj = r.getJSONObject(i);
                        System.out.println(obj);
                        reviews.add(new ModelReviews(obj.getString("firstName"),obj.getString("lastName"),obj.getString("email"),String.valueOf(obj.getInt("rating")),obj.getString("review")));

                    }
                    System.out.println(reviews.size());
                    setlist(reviews);



                }
                catch(Exception e)
                {
                    e.printStackTrace();

                }
                if(liked)
                {
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_dislike).into(like);
                    like.setClickable(false);
                }
                else
                {
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_likes).into(like);
                }

            }
        });

    }
    private void setlist(ArrayList<ModelReviews> list)
    {
        System.out.println(list.size());
        adapter = new ReviewAdapter(this,list);
        lv.setAdapter(adapter);
    }



    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getDataFromDb() {
        /*DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
        modelCategory = dbInteraction.getCategoryByName(model.category_desc);*/
        String temp[] = modelCategory.subcategory.split(",");
        for(int i=0;i<temp.length;i++) {
            if(temp[i].equalsIgnoreCase(model.subcategory_desc))
                defaultFragment=i;
        }
       // dbInteraction.close();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(!model.category_desc.equals("Packages")) {
                Intent i = new Intent(ProductDetails.this, Category.class);
                i.putExtra("modelCategory", modelCategory);
                i.putExtra("defaultFragment",defaultFragment);
                startActivity(i);
                finish();
            }

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
                    if (!apref.IsLoginedByEmail(ProductDetails.this) && !apref.IsLoginedByGoogle(ProductDetails.this) && !apref.IsLoginedByFb(ProductDetails.this)) {
                        apref.setIsReadyForCheckout1(ProductDetails.this, true);
                        Intent i = new Intent(ProductDetails.this, Login.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(ProductDetails.this, Checkout1.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(ProductDetails.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void initView() {
        edit_review = (EditText)findViewById(R.id.edit_review);
        ratingbar = (RatingBar)findViewById(R.id.ratingBar);
        rating_text = (TextView)findViewById(R.id.rating);
        rating_text.setText(String.valueOf(1));
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_text.setText(String.valueOf(Math.round(rating)));

            }
        });
        reviews = new ArrayList<ModelReviews>();
        lv = (ListView)findViewById(R.id.lv_reviews);
        //setListViewHeightBasedOnChildren(lv);
        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        viewPager = (ViewPager)findViewById(R.id.pager);
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        image = (ImageView) findViewById(R.id.image);
        rent_amount = (TextView) findViewById(R.id.rent_amount);
        security_deposit = (TextView) findViewById(R.id.security_deposit);
        min_rent_period = (TextView) findViewById(R.id.min_rent_period);
        material = (TextView) findViewById(R.id.material);
        dimensions = (TextView) findViewById(R.id.dimensions);
        color = (TextView) findViewById(R.id.color);
        type = (TextView) findViewById(R.id.type);
        brand = (TextView) findViewById(R.id.brand);
        rto = (TextView)findViewById(R.id.rto);
        other_description = (TextView) findViewById(R.id.other_description);
        rl_material = (RelativeLayout) findViewById(R.id.rl_material);
        rl_dimensions = (RelativeLayout) findViewById(R.id.rl_dimensions);
        rl_color = (RelativeLayout) findViewById(R.id.rl_color);
        rl_type = (RelativeLayout) findViewById(R.id.rl_type);
        rl_brand = (RelativeLayout) findViewById(R.id.rl_brand);
        rl_other = (RelativeLayout) findViewById(R.id.rl_other);
        wish = (ImageView)findViewById(R.id.wish_image);
        like = (ImageView)findViewById(R.id.like_image);
        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(this);


    }
    public void onReview(View v)
    {
        if(edit_review.getText().toString().length()>0)
        {
            final String r = rating_text.getText().toString();
            final String rev = edit_review.getText().toString();
            RequestParams params = new RequestParams();
            params.put("productId",model.prod_id);
            params.put("firstName",apref.readString(this,"name",""));
            params.put("lastName","");
            params.put("email",apref.readString(this,"email",""));
            params.put("rating",r);
            params.put("review",rev);
            EndPonits.insertRating(params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("Failure");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        JSONObject obj = new JSONObject(responseString);
                        if(obj.getString("success").equals("true"))
                        {
                            reviews.add(new ModelReviews(apref.readString(getApplicationContext(),"name",""),"",apref.readString(getApplicationContext(),"email",""),r,rev));
                            edit_review.setText("");
                            adapter.notifyDataSetChanged();
                        }

                    }
                    catch(Exception e)
                    {

                    }


                }
            });
        }

    }

    public void onWish(View v)
    {
        if(apref.IsLoginedByEmail(this)||apref.IsLoginedByGoogle(this)||apref.IsLoginedByFb(this)) {
            DBInteraction db = new DBInteraction(this);
            System.out.println(apref.readString(this, "email", ""));
            System.out.println(apref.readString(this, "name", ""));
            if (db.insertWishItem("P05", apref.readString(this, "email", ""), apref.readString(this, "name", ""))) {
                //Toast.makeText(this, "Product added to wishlist", Toast.LENGTH_SHORT).show();
                Picasso.with(this).load(R.drawable.ic_heart_disable).into(wish);
                wish.setClickable(false);
            } else {
                Toast.makeText(this, "Product already exists", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this,"Please Login first",Toast.LENGTH_SHORT).show();
        }

    }
    public void onLike(View v)
    {
        RequestParams params = new RequestParams();
        params.put("productId",model.prod_id);
        params.put("firstName",apref.readString(this,"name",""));
        params.put("lastName","");
        params.put("email",apref.readString(this,"email",""));
        EndPonits.incrementLikes(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject obj = new JSONObject(responseString);
                    if(obj.getString("success").equals("true"))
                    {
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_dislike).into(like);
                        like.setClickable(false);

                    }
                    else
                    {
                        Picasso.with(getApplicationContext()).load(R.drawable.ic_likes).into(like);
                    }
                }
                catch(Exception e)
                {

                }


            }
        });
    }



    private void setData() {
        DBInteraction db = new DBInteraction(this);
        if(db.checkWishProduct(model.prod_id))
        {
            Picasso.with(this).load(R.drawable.ic_heart_disable).into(wish);
            wish.setClickable(false);
        }
        else
        {
            System.out.println("HiPO1");
            Picasso.with(this).load(R.drawable.ic_heart_selected).into(wish);
            wish.setClickable(true);
        }
        System.out.println(model.big_img);
       /* Picasso.with(ProductDetails.this)
                .load(Config.subCategoryImage + model.big_img)
                        //.placeholder(R.drawable.dummy)
                        //.error(R.drawable.dummy)
                .into(image);*/
        rent_amount.setText(" " + model.rent_amount+"/"+model.rent_duration);
        security_deposit.setText(" " + model.security_deposit + "/" + model.rent_duration);
        System.out.println("Period"+model.min_rent_period);
        if(model.min_rent_period.equals("month"))
        min_rent_period.setText(model.min_rent_period+" months");
        else
            min_rent_period.setText(model.min_rent_period+" "+model.rent_duration);
        if(!model.rent_to_own.equals("0"))
        rto.setText(18+" months");
        else
            rto.setText("NA");
        if (model.material.equals(""))
            rl_material.setVisibility(View.GONE);
        else
            material.setText(model.material);
        if (model.dimensions.equals(""))
            rl_dimensions.setVisibility(View.GONE);
        else
            dimensions.setText(model.dimensions);
        if (model.color.equals(""))
            rl_color.setVisibility(View.GONE);
        else
            color.setText(model.color);
        if (model.type.equals(""))
            rl_type.setVisibility(View.GONE);
        else
            type.setText(model.type);
        if (model.brand.equals(""))
            rl_brand.setVisibility(View.GONE);
        else
            brand.setText(model.brand);
        if (model.other_description.equals(""))
            rl_other.setVisibility(View.GONE);
        else
            other_description.setText(model.other_description);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                ModelSubCategory temp = dbInteraction.getSubCategoryItemById(model.prod_id);
                dbInteraction.close();
                if(temp.quantity ==0 && temp.quantity_quarterly==0 && temp.quantity_monthly==0) {
                    RocqAnalytics.initialize(ProductDetails.this);
                    RocqAnalytics.trackEvent("6 month added", new ActionProperties(""), Position.LEFT);
                    temp.quantity_quarterly++;
                    model.quantity_quarterly++;
                    dbInteraction.updateSubCategoryDetail(temp.prod_id, temp.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(temp.prod_id + 1, temp.quantity_quarterly);
                    ModelCart modelCart = dbInteraction.getCartItemById(temp.prod_id + 1);
                    dbInteraction.close();
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));

                }
                showAddView(temp);
                break;
        }
    }

    private void showAddView(final ModelSubCategory modelSubCategory)
    {
        final TextView quantity_3,quantity_6,quantity_12,price_3,price_6,price_12;
        final RadioButton radio_3,radio_6,radio_12;
        ImageButton plus_3,minus_3,plus_6,minus_6,plus_12,minus_12;
        final LinearLayout btn_3,btn_6,btn_12;


        MaterialDialog dialog = new MaterialDialog.Builder(ProductDetails.this)
                .title("Add products")
                .customView(R.layout.dialog_add, true)
                .positiveText("Done")
                .build();

        btn_3 = (LinearLayout)dialog.getCustomView().findViewById(R.id.btn_3);
        btn_6 = (LinearLayout)dialog.getCustomView().findViewById(R.id.btn_6);
        btn_12 = (LinearLayout)dialog.getCustomView().findViewById(R.id.btn_12);
        radio_3 = (RadioButton)dialog.getCustomView().findViewById(R.id.radio_3);
        radio_6 = (RadioButton)dialog.getCustomView().findViewById(R.id.radio_6);
        radio_12 = (RadioButton)dialog.getCustomView().findViewById(R.id.radio_12);
        quantity_3 = (TextView) dialog.getCustomView().findViewById(R.id.quantity_3);
        quantity_6 = (TextView) dialog.getCustomView().findViewById(R.id.quantity_6);
        quantity_12 = (TextView) dialog.getCustomView().findViewById(R.id.quantity_12);
        price_3 = (TextView) dialog.getCustomView().findViewById(R.id.price_3);
        price_6 = (TextView) dialog.getCustomView().findViewById(R.id.price_6);
        price_12 = (TextView) dialog.getCustomView().findViewById(R.id.price_12);
        plus_3 = (ImageButton)dialog.getCustomView().findViewById(R.id.plus_3);
        plus_6 = (ImageButton)dialog.getCustomView().findViewById(R.id.plus_6);
        plus_12 = (ImageButton)dialog.getCustomView().findViewById(R.id.plus_12);
        minus_3 = (ImageButton)dialog.getCustomView().findViewById(R.id.minus_3);
        minus_6 = (ImageButton)dialog.getCustomView().findViewById(R.id.minus_6);
        minus_12 = (ImageButton)dialog.getCustomView().findViewById(R.id.minus_12);
        quantity_3.setText(String.valueOf(modelSubCategory.quantity));
        quantity_6.setText(String.valueOf(modelSubCategory.quantity_quarterly));
        quantity_12.setText(String.valueOf(modelSubCategory.quantity_monthly));
        int temp = Integer.parseInt(modelSubCategory.rent_amount);
        price_6.setText(ProductDetails.this.getResources().getString(R.string.Rs)+" " +String.valueOf(temp));
        temp = Integer.parseInt(modelSubCategory.rent_amount);
        temp=temp+(temp*(Config.DISCOUNT_HALF_YEARLY)/100);
        price_3.setText(ProductDetails.this.getResources().getString(R.string.Rs) + " " + String.valueOf(temp));
        temp = Integer.parseInt(modelSubCategory.rent_amount);
        temp=temp-(temp*(Config.DISCOUNT_YEARLY)/100);
        price_12.setText(ProductDetails.this.getResources().getString(R.string.Rs) + " " + String.valueOf(temp));
        if(modelSubCategory.quantity>0) {
            radio_3.setChecked(true);
            btn_3.setVisibility(View.VISIBLE);
            radio_6.setChecked(false);
            btn_6.setVisibility(View.GONE);
            radio_12.setChecked(false);
            btn_12.setVisibility(View.GONE);
        }
        else if(modelSubCategory.quantity_quarterly>0) {
            radio_3.setChecked(false);
            btn_3.setVisibility(View.GONE);
            radio_6.setChecked(true);
            btn_6.setVisibility(View.VISIBLE);
            radio_12.setChecked(false);
            btn_12.setVisibility(View.GONE);
        }
        else if(modelSubCategory.quantity_monthly>0) {
            radio_3.setChecked(false);
            btn_3.setVisibility(View.GONE);
            radio_6.setChecked(false);
            btn_6.setVisibility(View.GONE);
            radio_12.setChecked(true);
            btn_12.setVisibility(View.VISIBLE);
        }
        radio_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(quantity_6.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_6.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity_quarterly = 0;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 1, modelSubCategory.quantity_quarterly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 1);
                    dbInteraction.close();
                    quantity_6.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));

                }
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_12.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity_monthly=0;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 2, modelSubCategory.quantity_monthly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 2);
                    dbInteraction.close();
                    quantity_12.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));
                }
                if (Integer.parseInt(quantity_3.getText().toString()) == 0) {
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity++;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 0, modelSubCategory.quantity);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 0);
                    dbInteraction.close();
                    quantity_3.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);


                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));

                }

                radio_3.setChecked(true);
                btn_3.setVisibility(View.VISIBLE);
                radio_6.setChecked(false);
                btn_6.setVisibility(View.GONE);
                radio_12.setChecked(false);
                btn_12.setVisibility(View.GONE);

            }
        });
        radio_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_12.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity_monthly=0;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 2, modelSubCategory.quantity_monthly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 2);
                    dbInteraction.close();
                    quantity_12.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));
                }
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_3.getText().toString());
                    RocqAnalytics.initialize(ProductDetails.this);
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity=0;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 0, modelSubCategory.quantity);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 0);
                    dbInteraction.close();
                    quantity_3.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));


                }
                if (Integer.parseInt(quantity_6.getText().toString()) == 0) {
                    RocqAnalytics.initialize(ProductDetails.this);
                    RocqAnalytics.trackEvent("6 month added", new ActionProperties(""), Position.LEFT);
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity_quarterly++;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 1, modelSubCategory.quantity_quarterly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 1);
                    dbInteraction.close();
                    quantity_6.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);

                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));

                }
                radio_3.setChecked(false);
                btn_3.setVisibility(View.GONE);
                radio_6.setChecked(true);
                btn_6.setVisibility(View.VISIBLE);
                radio_12.setChecked(false);
                btn_12.setVisibility(View.GONE);
            }
        });
        radio_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_6.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_6.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity_quarterly = 0;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 1, modelSubCategory.quantity_quarterly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 1);
                    dbInteraction.close();
                    quantity_6.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));

                }
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_3.getText().toString());
                    RocqAnalytics.initialize(ProductDetails.this);
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity=0;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 0, modelSubCategory.quantity);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 0);
                    dbInteraction.close();
                    quantity_3.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));


                }
                if (Integer.parseInt(quantity_12.getText().toString()) == 0) {
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity_monthly++;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 2, modelSubCategory.quantity_monthly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 2);
                    dbInteraction.close();
                    quantity_12.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);

                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));

                }

                radio_3.setChecked(false);
                btn_3.setVisibility(View.GONE);
                radio_6.setChecked(false);
                btn_6.setVisibility(View.GONE);
                radio_12.setChecked(true);
                btn_12.setVisibility(View.VISIBLE);
            }
        });
               plus_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                modelSubCategory.quantity++;
                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                dbInteraction.updateCartDetail(modelSubCategory.prod_id + 0, modelSubCategory.quantity);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 0);
                dbInteraction.close();
                quantity_3.setText(String.valueOf(modelSubCategory.quantity));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                menu_quantity.setText(String.valueOf(Cart.QUANTITY));


            }
        });

        minus_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity--;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 0, modelSubCategory.quantity);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 0);
                    dbInteraction.close();
                    quantity_3.setText(String.valueOf(modelSubCategory.quantity));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));
                }
            }
        });

        plus_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                modelSubCategory.quantity_quarterly++;
                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                dbInteraction.updateCartDetail(modelSubCategory.prod_id + 1, modelSubCategory.quantity_quarterly);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 1);
                dbInteraction.close();
                quantity_6.setText(String.valueOf(modelSubCategory.quantity_quarterly));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                menu_quantity.setText(String.valueOf(Cart.QUANTITY));


            }
        });

        minus_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_6.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity_quarterly--;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 1, modelSubCategory.quantity_quarterly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 1);
                    dbInteraction.close();
                    quantity_6.setText(String.valueOf(modelSubCategory.quantity_quarterly));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));
                }
            }
        });
        plus_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                modelSubCategory.quantity_monthly++;
                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                dbInteraction.updateCartDetail(modelSubCategory.prod_id + 2, modelSubCategory.quantity_monthly);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 2);
                dbInteraction.close();
                quantity_12.setText(String.valueOf(modelSubCategory.quantity_monthly));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                menu_quantity.setText(String.valueOf(Cart.QUANTITY));


            }
        });

        minus_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(ProductDetails.this);
                    modelSubCategory.quantity_monthly--;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 2, modelSubCategory.quantity_monthly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 2);
                    dbInteraction.close();
                    quantity_12.setText(String.valueOf(modelSubCategory.quantity_monthly));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    menu_quantity.setText(String.valueOf(Cart.QUANTITY));
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Product Details "+model.subcategory_desc, new ActionProperties());
    }

    @Override
    protected void onStop() {
        super.onStop();
        RocqAnalytics.stopScreen(this);
    }
    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }

    public static class SwipeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.image);
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            String imageFileName = IMAGE_NAME[position];
            /*int imgResId = getResources().getIdentifier(imageFileName, "drawable", "com.javapapers.android.swipeimageslider");
            imageView.setImageResource(imgResId);*/
            Picasso.with(getContext()).load(Config.subCategoryImage + imageFileName).into(imageView);
            return swipeView;
        }

        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }

}
