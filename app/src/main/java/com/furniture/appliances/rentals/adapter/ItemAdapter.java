package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.dq.rocq.models.Position;
import com.furniture.appliances.rentals.Cart;
import com.furniture.appliances.rentals.Category;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.PackageProductDetails;
import com.furniture.appliances.rentals.ProductDetails;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelCategory;
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
 * Created by Infinia on 16-09-2015.
 */
public class ItemAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater;
    AppPreferences apref = new AppPreferences();
    ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<>();
    private ModelCategory category;


    public ItemAdapter(Context context, ArrayList<ModelSubCategory> modelSubCategoryArrayList,ModelCategory category) {
        this.mInflater = LayoutInflater.from(context);
        this.modelSubCategoryArrayList = modelSubCategoryArrayList;
        this.context = context;
        this.category = category;

    }

    @Override
    public int getCount() {
        return modelSubCategoryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelSubCategoryArrayList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.booking_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.add = (Button)view.findViewById(R.id.add);
            holder.wish=(ImageView)view.findViewById(R.id.img_wish);
            holder.like = (ImageView)view.findViewById(R.id.img_like);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final ModelSubCategory model = modelSubCategoryArrayList.get(position);
        RequestParams params = new RequestParams();
        params.put("productId",model.prod_id);
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
                        if(obj.getString("email").equals(apref.readString(context,"email","")))
                        {
                            Picasso.with(context).load(R.drawable.ic_dislike).into(holder.like);
                            holder.like.setClickable(false);

                        }
                        else
                        {
                            Picasso.with(context).load(R.drawable.ic_likes).into(holder.like);

                        }
                    }

                }
                catch(Exception e)
                {

                }

            }
        });


        final DBInteraction db = new DBInteraction(context);
        if(db.checkWishProduct(model.prod_id))
        {
            Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.wish);
            holder.wish.setClickable(false);
        }
        else
        {
            Picasso.with(context).load(R.drawable.ic_heart_selected).into(holder.wish);
            holder.wish.setClickable(true);
        }
        if(model.prod_name.contains(model.subcategory_desc))
        {
            model.prod_name = model.prod_name.replace(model.subcategory_desc,"");
        }
        holder.name.setText(model.prod_name);
        System.out.println(model.prod_name);
        System.out.println(model.subcategory_code);
        System.out.println(model.subcategory_desc);
        holder.price.setText(context.getResources().getString(R.string.Rs)+" " + model.rent_amount+"/"+model.rent_duration);
        Picasso.with(holder.image.getContext())
                .load(Config.subCategoryImage + parseImg(model.small_img))
                        //.placeholder(R.drawable.dummy)
                        //.error(R.drawable.dummy)
                .into(holder.image);


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(context);
                ModelSubCategory temp = dbInteraction.getSubCategoryItemById(model.prod_id);
                dbInteraction.close();
                if(temp.quantity ==0 && temp.quantity_quarterly==0 && temp.quantity_monthly==0) {
                    RocqAnalytics.initialize(context);
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

                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
                }
                showAddView(temp);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RocqAnalytics.initialize(context);
                RocqAnalytics.trackEvent(model.subcategory_desc, new ActionProperties(""), Position.LEFT);
                if (model.category_desc.equals("Packages")) {
                    Intent i = new Intent(context, PackageProductDetails.class);
                    i.putExtra("category",category);
                    i.putExtra("model", model);
                    context.startActivity(i);
                    ((Category)context).finish();
                } else {
                    Intent i = new Intent(context, ProductDetails.class);
                    i.putExtra("category",category);
                    i.putExtra("model", model);
                    context.startActivity(i);
                    ((MainActivity)context).finish();
                }
            }
        });
        holder.wish.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(db.insertWishItem(model.prod_id,apref.readString(context,"email",""),apref.readString(context,"name","")))
                {
                    Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.wish);
                    holder.wish.setClickable(false);

                }

            }

        });
        holder.like.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                RequestParams params = new RequestParams();
                params.put("productId",model.prod_id);
                params.put("firstName",apref.readString(context,"name",""));
                params.put("lastName","");
                params.put("email",apref.readString(context,"email",""));
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
                                Picasso.with(context).load(R.drawable.ic_dislike).into(holder.like);
                                holder.like.setClickable(false);

                            }
                            else
                            {
                                Picasso.with(context).load(R.drawable.ic_likes).into(holder.like);
                            }
                        }
                        catch(Exception e)
                        {

                        }


                    }
                });

            }

        });
        return view;
    }

    private void showAddView(final ModelSubCategory modelSubCategory)
    {
        final TextView quantity_3,quantity_6,quantity_12,price_3,price_6,price_12;
        final RadioButton radio_3,radio_6,radio_12;
        ImageButton plus_3,minus_3,plus_6,minus_6,plus_12,minus_12;
        final LinearLayout btn_3,btn_6,btn_12;

        MaterialDialog dialog = new MaterialDialog.Builder(context)
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
        price_6.setText(context.getResources().getString(R.string.Rs)+" " +String.valueOf(temp));
        temp = Integer.parseInt(modelSubCategory.rent_amount);
        temp=temp+(temp*(Config.DISCOUNT_HALF_YEARLY)/100);
        price_3.setText(context.getResources().getString(R.string.Rs)+" " +String.valueOf(temp));
        temp = Integer.parseInt(modelSubCategory.rent_amount);
        temp=temp-(temp*(Config.DISCOUNT_YEARLY)/100);
        price_12.setText(context.getResources().getString(R.string.Rs)+" " +String.valueOf(temp));
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
                    DBInteraction dbInteraction = new DBInteraction(context);
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
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
                }
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_12.getText().toString());
                    DBInteraction dbInteraction = new DBInteraction(context);
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
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
                }
                if (Integer.parseInt(quantity_3.getText().toString()) == 0) {
                    DBInteraction dbInteraction = new DBInteraction(context);
                    modelSubCategory.quantity++;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 0, modelSubCategory.quantity);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 0);
                    dbInteraction.close();
                    quantity_3.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);


                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
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
                    DBInteraction dbInteraction = new DBInteraction(context);
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
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
                }
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_3.getText().toString());
                    RocqAnalytics.initialize(context);
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);
                    DBInteraction dbInteraction = new DBInteraction(context);
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
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();

                }
                if (Integer.parseInt(quantity_6.getText().toString()) == 0) {
                    RocqAnalytics.initialize(context);
                    RocqAnalytics.trackEvent("6 month added", new ActionProperties(""), Position.LEFT);
                    DBInteraction dbInteraction = new DBInteraction(context);
                    modelSubCategory.quantity_quarterly++;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 1, modelSubCategory.quantity_quarterly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 1);
                    dbInteraction.close();
                    quantity_6.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);

                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
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
                    DBInteraction dbInteraction = new DBInteraction(context);
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
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
                }
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    int temp = Integer.parseInt(quantity_3.getText().toString());
                    RocqAnalytics.initialize(context);
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);
                    DBInteraction dbInteraction = new DBInteraction(context);
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
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();

                }
                if (Integer.parseInt(quantity_12.getText().toString()) == 0) {
                    DBInteraction dbInteraction = new DBInteraction(context);
                    modelSubCategory.quantity_monthly++;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 2, modelSubCategory.quantity_monthly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 2);
                    dbInteraction.close();
                    quantity_12.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY++;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);

                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
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
                RocqAnalytics.initialize(context);
                RocqAnalytics.trackEvent("3 month added", new ActionProperties(""), Position.LEFT);
                DBInteraction dbInteraction = new DBInteraction(context);
                modelSubCategory.quantity++;
                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                dbInteraction.updateCartDetail(modelSubCategory.prod_id + 0, modelSubCategory.quantity);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 0);
                dbInteraction.close();
                quantity_3.setText(String.valueOf(modelCart.quantity));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);


                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();


            }
        });

        minus_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantity_3.getText().toString()) > 0) {
                    RocqAnalytics.initialize(context);
                    RocqAnalytics.trackEvent("3 month removed", new ActionProperties(""), Position.LEFT);
                    DBInteraction dbInteraction = new DBInteraction(context);
                    modelSubCategory.quantity--;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity, 0);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 0, modelSubCategory.quantity);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 0);
                    dbInteraction.close();
                    quantity_3.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();

                }
            }
        });

        plus_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RocqAnalytics.initialize(context);
                RocqAnalytics.trackEvent("6 month added", new ActionProperties(""), Position.LEFT);
                DBInteraction dbInteraction = new DBInteraction(context);
                modelSubCategory.quantity_quarterly++;
                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                dbInteraction.updateCartDetail(modelSubCategory.prod_id + 1, modelSubCategory.quantity_quarterly);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 1);
                dbInteraction.close();
                quantity_6.setText(String.valueOf(modelCart.quantity));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);

                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();


            }
        });

        minus_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RocqAnalytics.initialize(context);
                RocqAnalytics.trackEvent("6 month removed", new ActionProperties(""), Position.LEFT);
                if (Integer.parseInt(quantity_6.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(context);
                    modelSubCategory.quantity_quarterly--;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_quarterly, 1);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 1, modelSubCategory.quantity_quarterly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 1);
                    dbInteraction.close();
                    quantity_6.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
                }
            }
        });
        plus_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RocqAnalytics.initialize(context);
                RocqAnalytics.trackEvent("12 month added", new ActionProperties(""), Position.LEFT);
                DBInteraction dbInteraction = new DBInteraction(context);
                modelSubCategory.quantity_monthly++;
                dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                dbInteraction.updateCartDetail(modelSubCategory.prod_id + 2, modelSubCategory.quantity_monthly);
                ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 2);
                dbInteraction.close();
                quantity_12.setText(String.valueOf(modelCart.quantity));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(modelCart.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(modelCart.total_amount);

                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();

            }
        });

        minus_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RocqAnalytics.initialize(context);
                RocqAnalytics.trackEvent("12 month removed", new ActionProperties(""), Position.LEFT);
                if (Integer.parseInt(quantity_12.getText().toString()) > 0) {
                    DBInteraction dbInteraction = new DBInteraction(context);
                    modelSubCategory.quantity_monthly--;
                    dbInteraction.updateSubCategoryDetail(modelSubCategory.prod_id, modelSubCategory.quantity_monthly, 2);
                    dbInteraction.updateCartDetail(modelSubCategory.prod_id + 2, modelSubCategory.quantity_monthly);
                    ModelCart modelCart = dbInteraction.getCartItemById(modelSubCategory.prod_id + 2);
                    dbInteraction.close();
                    quantity_12.setText(String.valueOf(modelCart.quantity));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(modelCart.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(modelCart.total_amount);
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();
                }
            }
        });
        dialog.show();
    }
    private String parseImg(String json)
    {
        try
        {
            JSONArray array = new JSONArray(json);
            System.out.println("Image"+array.getString(0));
            return array.getString(0);
        }
        catch(Exception e)
        {
            return "";
        }
    }




    private class ViewHolder {
        public TextView name, price;
        public ImageView image,wish,like;
        public Button add;
    }
}

