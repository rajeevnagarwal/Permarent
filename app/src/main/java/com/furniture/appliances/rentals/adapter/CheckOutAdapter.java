package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.Cart;
import com.furniture.appliances.rentals.Checkout1;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Infinia on 22-09-2015.
 */
public class CheckOutAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater;
    ArrayList<ModelCart> modelCartArrayList = new ArrayList<ModelCart>();
    AppPreferences apref = new AppPreferences();


    public CheckOutAdapter(Context context, ArrayList<ModelCart> modelSubCategoryList) {
        this.mInflater = LayoutInflater.from(context);
        this.modelCartArrayList = modelSubCategoryList;
        this.context = context;

    }

    @Override
    public int getCount() {
        return modelCartArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelCartArrayList.get(position);

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
            view = mInflater.inflate(R.layout.checkout_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.quantity = (TextView)view.findViewById(R.id.quantity);
            holder.type = (TextView) view.findViewById(R.id.type);
            holder.rent = (TextView)view.findViewById(R.id.rent);
            holder.security = (TextView) view.findViewById(R.id.security);
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.plus = (ImageButton) view.findViewById(R.id.plus);
            holder.minus = (ImageButton) view.findViewById(R.id.minus);
            holder.wish = (ImageView)view.findViewById(R.id.cart_wish);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final ModelCart model = modelCartArrayList.get(position);
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
        holder.name.setText(model.item_name);
        if(model.rent_type.equals("0")){
            holder.type.setText("3 months");
            int temp = Integer.parseInt(model.rent_amount);
            holder.rent.setText("Rent amount "+context.getResources().getString(R.string.Rs)+" "+temp);
        }
        else if(model.rent_type.equals("1")){
            holder.type.setText("6 months");
            int temp = Integer.parseInt(model.rent_amount);
            holder.rent.setText("Rent amount "+context.getResources().getString(R.string.Rs)+" "+temp);
        }
        else if(model.rent_type.equals("2")){
            holder.type.setText("9 months");
            int temp = Integer.parseInt(model.rent_amount);
            holder.rent.setText("Rent amount "+context.getResources().getString(R.string.Rs)+" "+temp);
        }
        else if(model.rent_type.equals("3"))
        {
            holder.type.setText("12 months");
            holder.rent.setText("Rent amount "+context.getResources().getString(R.string.Rs)+" "+model.rent_amount);
        }

        holder.security.setText("Security amount "+context.getResources().getString(R.string.Rs)+" " + model.security_amount);
        holder.quantity.setText(String.valueOf(model.quantity));
        Picasso.with(holder.image.getContext())
                .load(Config.subCategoryImage + model.small_img)
                        //.placeholder(R.drawable.dummy)
                        //.error(R.drawable.dummy)
                .into(holder.image);
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBInteraction dbInteraction = new DBInteraction(context);
                model.quantity++;
               // dbInteraction.updateSubCategoryDetail(model.prod_id, model.quantity, Integer.parseInt(model.rent_type));
                dbInteraction.updateCartDetail(model.item_id, model.quantity);
                dbInteraction.close();
                holder.quantity.setText(String.valueOf(model.quantity));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(model.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(model.total_amount);
                ((Checkout1)context).setText();
                ((Checkout1) context).getDataFromDb();




            }
        });
        holder.wish.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                if(db.insertWishItem(model.prod_id,apref.readString(context,"email",""),apref.readString(context,"name","")))
                {
                    Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.wish);
                    holder.wish.setClickable(false);

                }
            }

        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(holder.quantity.getText().toString()) > 1) {
                    DBInteraction dbInteraction = new DBInteraction(context);
                    model.quantity--;
                    //dbInteraction.updateSubCategoryDetail(model.prod_id, model.quantity, Integer.parseInt(model.rent_type));
                    dbInteraction.updateCartDetail(model.item_id, model.quantity);
                    dbInteraction.close();
                    holder.quantity.setText(String.valueOf(model.quantity));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(model.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(model.total_amount);
                    ((Checkout1) context).setText();
                    ((Checkout1) context).getDataFromDb();


                } else if (Integer.parseInt(holder.quantity.getText().toString()) == 1) {
                    DBInteraction dbInteraction = new DBInteraction(context);
                    model.quantity--;
                    //dbInteraction.updateSubCategoryDetail(model.prod_id, model.quantity, Integer.parseInt(model.rent_type));
                    dbInteraction.updateCartDetail(model.item_id, model.quantity);
                    dbInteraction.close();
                    modelCartArrayList.remove(model);
                    holder.quantity.setText(String.valueOf(model.quantity));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(model.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(model.total_amount);
                    ((Checkout1)context).setText();
                    ((Checkout1) context).getDataFromDb();


                }


            }
        });
        return view;
    }



    private class ViewHolder {
        public TextView name, type, quantity,rent,security;
        public ImageView image,wish;
        public ImageButton plus, minus;
    }
}


