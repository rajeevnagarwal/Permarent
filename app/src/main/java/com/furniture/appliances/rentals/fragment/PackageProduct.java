package com.furniture.appliances.rentals.fragment;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.dq.rocq.models.Position;
import com.furniture.appliances.rentals.Cart;
import com.furniture.appliances.rentals.PackageProductDetails;
import com.furniture.appliances.rentals.ProductDetails;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Infinia on 02-10-2015.
 */
public class PackageProduct extends Fragment implements View.OnClickListener{
    TextView rent_amount,security_deposit,min_rent_period,quantity_package,material,dimensions,color,type,brand,other_description;
    ImageView image;
    RelativeLayout rl_dimensions,rl_material,rl_color,rl_type,rl_brand,rl_other;
    ModelSubCategory parentItem = new ModelSubCategory();

    private List<ImageView> dots;
    private int totalSlides=0;
    LinearLayout dotLayout;
    private int slideNumber;

    public void setModelSubCategory(ModelSubCategory modelSubCategory, ModelSubCategory parentItem,int totalSlides,int slideNumber) {
        this.modelSubCategory = modelSubCategory;
        this.parentItem=parentItem;
        this.totalSlides = totalSlides;
        this.slideNumber=slideNumber;
    }

    ModelSubCategory modelSubCategory = new ModelSubCategory();
    Button add;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_package_product, container, false);
        initView(view);
        setData();
        return view;
    }
    private void initView(View view)
    {
        image = (ImageView)view.findViewById(R.id.image);
        rent_amount = (TextView)view.findViewById(R.id.rent_amount);
        security_deposit = (TextView)view.findViewById(R.id.security_deposit);
        min_rent_period = (TextView)view.findViewById(R.id.min_rent_period);
        quantity_package = (TextView)view.findViewById(R.id.packagequantity);
        material = (TextView)view.findViewById(R.id.material);
        dimensions = (TextView)view.findViewById(R.id.dimensions);
        color = (TextView)view.findViewById(R.id.color);
        type = (TextView)view.findViewById(R.id.type);
        brand = (TextView)view.findViewById(R.id.brand);
        other_description = (TextView)view.findViewById(R.id.other_description);
        rl_material = (RelativeLayout)view.findViewById(R.id.rl_material);
        rl_dimensions = (RelativeLayout)view.findViewById(R.id.rl_dimensions);
        rl_color = (RelativeLayout)view.findViewById(R.id.rl_color);
        rl_type = (RelativeLayout)view.findViewById(R.id.rl_type);
        rl_brand = (RelativeLayout)view.findViewById(R.id.rl_brand);
        rl_other = (RelativeLayout)view.findViewById(R.id.rl_other);
        dotLayout = (LinearLayout) view.findViewById(R.id.dotLayout);
        add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(this);
        loadDots();
        selectDot(slideNumber);
    }
    private void loadDots() {
        dots = new ArrayList<>();

        for (int i = 0; i < totalSlides; i++) {
            ImageView dot = new ImageView(getActivity());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.indicator_dot_grey));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            dotLayout.addView(dot, params);

            dots.add(dot);
        }

        selectDot(slideNumber);
    }
    public void selectDot(int index) {
        Resources res = getResources();
        for (int i = 0; i < totalSlides ; i++) {
            int drawableId = (i == index) ? (R.drawable.indicator_dot_white) : (R.drawable.indicator_dot_grey);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }
    private void setData()
    {
        Picasso.with(getActivity())
                .load(Config.subCategoryImage+modelSubCategory.firstLarge())
                        //.placeholder(R.drawable.dummy)
                        //.error(R.drawable.dummy)
                .into(image);
        rent_amount.setText(" " + modelSubCategory.getTwelve()+"/month");
        security_deposit.setText(" "+modelSubCategory.securityAmount);
        min_rent_period.setText(modelSubCategory.minRentalDuration+" months");
        quantity_package.setText(" "+modelSubCategory.max_quantity);
        if(modelSubCategory.material.equals(""))
            rl_material.setVisibility(View.GONE);
        else
            material.setText(modelSubCategory.material);
        if(modelSubCategory.dimensions.equals(""))
            rl_dimensions.setVisibility(View.GONE);
        else
            dimensions.setText(modelSubCategory.dimensions);
        if(modelSubCategory.color.equals(""))
            rl_color.setVisibility(View.GONE);
        else
            color.setText(modelSubCategory.color);
        if(modelSubCategory.type.equals(""))
            rl_type.setVisibility(View.GONE);
        else
            type.setText(modelSubCategory.type);
        if(modelSubCategory.brand.equals(""))
            rl_brand.setVisibility(View.GONE);
        else
            brand.setText(modelSubCategory.brand);
        if(modelSubCategory.otherDesc.equals(""))
            rl_other.setVisibility(View.GONE);
        else
            other_description.setText(modelSubCategory.otherDesc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                DBInteraction dbInteraction = new DBInteraction(getActivity());
                //ModelSubCategory temp = dbInteraction.getSubCategoryItemById(modelSubCategory.prod_id);
                ModelSubCategory temp = modelSubCategory;
                dbInteraction.close();
                if(temp.quantity_threeMo == 0 && temp.quantity_sixMo == 0 && temp.quantity_nineMo == 0 && temp.quantity_twelveMo == 0) {
                    /*RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackEvent("6 month added", new ActionProperties(""), Position.LEFT);*/
                    temp.quantity_sixMo++;
                    modelSubCategory.quantity_sixMo++;
                    //dbInteraction.updateSubCategoryDetail(temp.prod_id, temp.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(temp.productId + 1, temp.quantity_sixMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(temp.productId + 1);
                    dbInteraction.close();
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                }
                showAddView(temp);
                break;


        }
    }

    private void showAddView(final ModelSubCategory modelSubCategory)
    {
        final TextView quantity_3,quantity_6,quantity_9,quantity_12,price_3,price_6,price_9,price_12;
        final RadioButton radio_3,radio_6,radio_9,radio_12;
        ImageButton plus_3,minus_3,plus_6,minus_6,plus_9,minus_9,plus_12,minus_12;
        final LinearLayout btn_3,btn_6,btn_9,btn_12;


        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Add products")
                .customView(R.layout.dialog_add, true)
                .positiveText("Done")
                .build();


        btn_3 = (LinearLayout)dialog.getCustomView().findViewById(R.id.btn_3);
        btn_6 = (LinearLayout)dialog.getCustomView().findViewById(R.id.btn_6);
        btn_9 = (LinearLayout)dialog.getCustomView().findViewById(R.id.btn_9);
        btn_12 = (LinearLayout)dialog.getCustomView().findViewById(R.id.btn_12);
        radio_3 = (RadioButton)dialog.getCustomView().findViewById(R.id.radio_3);
        radio_6 = (RadioButton)dialog.getCustomView().findViewById(R.id.radio_6);
        radio_9 = (RadioButton)dialog.getCustomView().findViewById(R.id.radio_9);
        radio_12 = (RadioButton)dialog.getCustomView().findViewById(R.id.radio_12);
        quantity_3 = (TextView) dialog.getCustomView().findViewById(R.id.quantity_3);
        quantity_6 = (TextView) dialog.getCustomView().findViewById(R.id.quantity_6);
        quantity_9 = (TextView) dialog.getCustomView().findViewById(R.id.quantity_9);
        quantity_12 = (TextView) dialog.getCustomView().findViewById(R.id.quantity_12);
        price_3 = (TextView) dialog.getCustomView().findViewById(R.id.price_3);
        price_6 = (TextView) dialog.getCustomView().findViewById(R.id.price_6);
        price_9 = (TextView) dialog.getCustomView().findViewById(R.id.price_9);
        price_12 = (TextView) dialog.getCustomView().findViewById(R.id.price_12);
        plus_3 = (ImageButton)dialog.getCustomView().findViewById(R.id.plus_3);
        plus_6 = (ImageButton)dialog.getCustomView().findViewById(R.id.plus_6);
        plus_9 = (ImageButton)dialog.getCustomView().findViewById(R.id.plus_9);
        plus_12 = (ImageButton)dialog.getCustomView().findViewById(R.id.plus_12);
        minus_3 = (ImageButton)dialog.getCustomView().findViewById(R.id.minus_3);
        minus_6 = (ImageButton)dialog.getCustomView().findViewById(R.id.minus_6);
        minus_9 = (ImageButton)dialog.getCustomView().findViewById(R.id.minus_9);
        minus_12 = (ImageButton)dialog.getCustomView().findViewById(R.id.minus_12);
        quantity_3.setText(String.valueOf(modelSubCategory.quantity_threeMo));
        quantity_6.setText(String.valueOf(modelSubCategory.quantity_sixMo));
        quantity_9.setText(String.valueOf(modelSubCategory.quantity_nineMo));
        quantity_12.setText(String.valueOf(modelSubCategory.quantity_twelveMo));
        if(modelSubCategory.quantity_threeMo>0) {
            radio_3.setChecked(true);
            btn_3.setVisibility(View.VISIBLE);
            radio_6.setChecked(false);
            btn_6.setVisibility(View.GONE);
            radio_9.setChecked(false);
            btn_9.setVisibility(View.GONE);
            radio_12.setChecked(false);
            btn_12.setVisibility(View.GONE);
        }
        else if(modelSubCategory.quantity_sixMo>0) {
            radio_3.setChecked(false);
            btn_3.setVisibility(View.GONE);
            radio_6.setChecked(true);
            btn_6.setVisibility(View.VISIBLE);
            radio_9.setChecked(false);
            btn_9.setVisibility(View.GONE);
            radio_12.setChecked(false);
            btn_12.setVisibility(View.GONE);
        }
        else if(modelSubCategory.quantity_nineMo>0) {
            radio_3.setChecked(false);
            btn_3.setVisibility(View.GONE);
            radio_6.setChecked(false);
            btn_6.setVisibility(View.GONE);
            radio_9.setChecked(true);
            btn_9.setVisibility(View.VISIBLE);
            radio_12.setChecked(false);
            btn_12.setVisibility(View.GONE);
        }
        else if(modelSubCategory.quantity_twelveMo>0) {
            radio_3.setChecked(false);
            btn_3.setVisibility(View.GONE);
            radio_6.setChecked(false);
            btn_6.setVisibility(View.GONE);
            radio_9.setChecked(false);
            btn_9.setVisibility(View.GONE);
            radio_12.setChecked(true);
            btn_12.setVisibility(View.VISIBLE);
        }
        radio_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_3.getText().toString()) == 0) {
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_threeMo++;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 0, modelSubCategory.quantity_threeMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 0);
                    dbInteraction.close();
                    quantity_3.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_6.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_6.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_sixMo = 0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 1, modelSubCategory.quantity_sixMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 1);
                    dbInteraction.close();
                    quantity_6.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_9.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_9.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_nineMo = 0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 2, modelSubCategory.quantity_nineMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 2);
                    dbInteraction.close();
                    quantity_9.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_12.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_twelveMo=0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 3, modelSubCategory.quantity_twelveMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 3);
                    dbInteraction.close();
                    quantity_12.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }


                radio_3.setChecked(true);
                btn_3.setVisibility(View.VISIBLE);
                radio_6.setChecked(false);
                btn_6.setVisibility(View.GONE);
                radio_9.setChecked(false);
                btn_9.setVisibility(View.GONE);
                radio_12.setChecked(false);
                btn_12.setVisibility(View.GONE);

            }
        });
        radio_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_3.getText().toString());
                    /*RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);*/
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_threeMo=0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 0, modelSubCategory.quantity_threeMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 0);
                    dbInteraction.close();
                    quantity_3.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_6.getText().toString()) == 0) {
                    /*RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackEvent("6 month added", new ActionProperties(""), Position.LEFT);*/
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_sixMo++;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 1, modelSubCategory.quantity_sixMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 1);
                    dbInteraction.close();
                    quantity_6.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_9.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_9.getText().toString());
                    /*RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);*/
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_nineMo = 0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 2, modelSubCategory.quantity_nineMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 2);
                    dbInteraction.close();
                    quantity_9.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_12.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_twelveMo = 0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 3 , modelSubCategory.quantity_twelveMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 3);
                    dbInteraction.close();
                    quantity_12.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();

            }

                radio_3.setChecked(false);
                btn_3.setVisibility(View.GONE);
                radio_6.setChecked(true);
                btn_6.setVisibility(View.VISIBLE);
                radio_9.setChecked(false);
                btn_9.setVisibility(View.GONE);
                radio_12.setChecked(false);
                btn_12.setVisibility(View.GONE);
            }
        });
        radio_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_3.getText().toString());
                    /*RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);*/
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_threeMo=0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 0, modelSubCategory.quantity_threeMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 0);
                    dbInteraction.close();
                    quantity_3.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_6.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_6.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_sixMo = 0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 1, modelSubCategory.quantity_sixMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 1);
                    dbInteraction.close();
                    quantity_6.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_9.getText().toString()) == 0) {
                    /*RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackEvent("6 month added", new ActionProperties(""), Position.LEFT);*/
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_nineMo++;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 2, modelSubCategory.quantity_nineMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 2);
                    dbInteraction.close();
                    quantity_9.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_12.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_twelveMo = 0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 3 , modelSubCategory.quantity_twelveMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 3);
                    dbInteraction.close();
                    quantity_12.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }

                radio_3.setChecked(false);
                btn_3.setVisibility(View.GONE);
                radio_6.setChecked(false);
                btn_6.setVisibility(View.GONE);
                radio_9.setChecked(true);
                btn_9.setVisibility(View.VISIBLE);
                radio_12.setChecked(false);
                btn_12.setVisibility(View.GONE);
            }
        });
        radio_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_3.getText().toString());
                    /*RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);*/
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_threeMo=0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 0, modelSubCategory.quantity_threeMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 0);
                    dbInteraction.close();
                    quantity_3.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_6.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_6.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_sixMo = 0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 1, modelSubCategory.quantity_sixMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 1);
                    dbInteraction.close();
                    quantity_6.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_9.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_9.getText().toString());
                    /*RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);*/
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_nineMo = 0;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 2, modelSubCategory.quantity_nineMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 2);
                    dbInteraction.close();
                    quantity_9.setText("0");
                    for (int i = 0; i < temp; i++) {
                        Cart.QUANTITY--;
                        Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                        Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    }
                    ((PackageProductDetails)getActivity()).updateMenu();
                }
                if (Integer.parseInt(quantity_12.getText().toString()) == 0) {
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_twelveMo++;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 3, modelSubCategory.quantity_twelveMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 3);
                    dbInteraction.close();
                    quantity_12.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                    ((PackageProductDetails)getActivity()).updateMenu();

                }

                radio_3.setChecked(false);
                btn_3.setVisibility(View.GONE);
                radio_6.setChecked(false);
                btn_6.setVisibility(View.GONE);
                radio_9.setChecked(false);
                btn_9.setVisibility(View.GONE);
                radio_12.setChecked(true);
                btn_12.setVisibility(View.VISIBLE);
            }
        });
        plus_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(getActivity());
                modelSubCategory.quantity_threeMo++;
                //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                dbInteraction.updateCartDetail(modelSubCategory.productId + 0, modelSubCategory.quantity_threeMo);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 0);
                dbInteraction.close();
                quantity_3.setText(String.valueOf(modelSubCategory.quantity_threeMo));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                ((PackageProductDetails)getActivity()).updateMenu();

            }
        });
        minus_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_threeMo--;
                    //dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 0, modelSubCategory.quantity_threeMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 0);
                    dbInteraction.close();
                    quantity_3.setText(String.valueOf(modelSubCategory.quantity_threeMo));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    ((PackageProductDetails)getActivity()).updateMenu();

                }
            }
        });

        plus_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(getActivity());
                modelSubCategory.quantity_sixMo++;
//                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                dbInteraction.updateCartDetail(modelSubCategory.productId + 1, modelSubCategory.quantity_sixMo);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 1);
                dbInteraction.close();
                quantity_6.setText(String.valueOf(modelSubCategory.quantity_sixMo));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                ((PackageProductDetails)getActivity()).updateMenu();

            }
        });
        minus_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_6.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_sixMo--;
//                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 1, modelSubCategory.quantity_sixMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 1);
                    dbInteraction.close();
                    quantity_6.setText(String.valueOf(modelSubCategory.quantity_sixMo));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    ((PackageProductDetails)getActivity()).updateMenu();

                }
            }
        });

        plus_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(getActivity());
                modelSubCategory.quantity_nineMo++;
//                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                dbInteraction.updateCartDetail(modelSubCategory.productId + 2, modelSubCategory.quantity_nineMo);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 2);
                dbInteraction.close();
                quantity_9.setText(String.valueOf(modelSubCategory.quantity_nineMo));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                ((PackageProductDetails)getActivity()).updateMenu();

            }
        });
        minus_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_9.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_nineMo--;
//                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 2, modelSubCategory.quantity_nineMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 2);
                    dbInteraction.close();
                    quantity_9.setText(String.valueOf(modelSubCategory.quantity_sixMo));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    ((PackageProductDetails)getActivity()).updateMenu();

                }
            }
        });

        plus_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(getActivity());
                modelSubCategory.quantity_twelveMo++;
//                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                dbInteraction.updateCartDetail(modelSubCategory.productId + 3, modelSubCategory.quantity_twelveMo);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 3);
                dbInteraction.close();
                quantity_12.setText(String.valueOf(modelSubCategory.quantity_twelveMo));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);
                ((PackageProductDetails)getActivity()).updateMenu();

            }
        });
        minus_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    modelSubCategory.quantity_twelveMo--;
//                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.productId + 3, modelSubCategory.quantity_twelveMo);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.productId + 3);
                    dbInteraction.close();
                    quantity_12.setText(String.valueOf(modelSubCategory.quantity_twelveMo));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    ((PackageProductDetails)getActivity()).updateMenu();

                }
            }
        });
        dialog.show();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ab_cart).setVisible(true).setEnabled(true);
    }
}
