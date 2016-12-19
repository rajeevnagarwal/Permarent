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
import com.furniture.appliances.rentals.Category;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Infinia on 21-09-2015.
 */
public class CartAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater mInflater;
    ArrayList<ModelCart> modelCartArrayList = new ArrayList<ModelCart>();


    public CartAdapter(Context context, ArrayList<ModelCart> modelSubCategoryList) {
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
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final ModelCart model = modelCartArrayList.get(position);
        holder.name.setText(model.item_name);
        if(model.rent_type.equals("0")){
            holder.type.setText("3 months");
            int temp = Integer.parseInt(model.rent_amount);
            temp=temp+(temp*(Config.DISCOUNT_HALF_YEARLY)/100);
            holder.rent.setText("Rent amount "+context.getResources().getString(R.string.Rs)+" "+String.valueOf(temp));
        }
        else if(model.rent_type.equals("1")){
            holder.type.setText("6 months");
            int temp = Integer.parseInt(model.rent_amount);
            holder.rent.setText("Rent amount "+context.getResources().getString(R.string.Rs)+" "+String.valueOf(temp));
        }
        else if(model.rent_type.equals("2")){
            holder.type.setText("12 months");
            int temp = Integer.parseInt(model.rent_amount);
            temp=temp-(temp*(Config.DISCOUNT_YEARLY)/100);
            holder.rent.setText("Rent amount "+context.getResources().getString(R.string.Rs)+" "+String.valueOf(temp));
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
                dbInteraction.updateSubCategoryDetail(model.prod_id, model.quantity, Integer.parseInt(model.rent_type));
                dbInteraction.updateCartDetail(model.item_id, model.quantity);
                dbInteraction.close();
                holder.quantity.setText(String.valueOf(model.quantity));
                Cart.QUANTITY++;
                Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT + Integer.valueOf(model.security_amount);
                Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT + Integer.valueOf(model.total_amount);
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();



            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(holder.quantity.getText().toString()) > 1) {
                    DBInteraction dbInteraction = new DBInteraction(context);
                    model.quantity--;
                    dbInteraction.updateSubCategoryDetail(model.prod_id, model.quantity, Integer.parseInt(model.rent_type));
                    dbInteraction.updateCartDetail(model.item_id, model.quantity);
                    dbInteraction.close();
                    holder.quantity.setText(String.valueOf(model.quantity));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(model.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(model.total_amount);
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();


                } else if (Integer.parseInt(holder.quantity.getText().toString()) == 1) {
                    DBInteraction dbInteraction = new DBInteraction(context);
                    model.quantity--;
                    dbInteraction.updateSubCategoryDetail(model.prod_id, model.quantity, Integer.parseInt(model.rent_type));
                    dbInteraction.updateCartDetail(model.item_id, model.quantity);
                    dbInteraction.close();
                    modelCartArrayList.remove(model);
                    holder.quantity.setText(String.valueOf(model.quantity));
                    Cart.QUANTITY--;
                    Cart.SECURITY_AMOUNT = Cart.SECURITY_AMOUNT - Integer.valueOf(model.security_amount);
                    Cart.TOTAL_AMOUNT = Cart.TOTAL_AMOUNT - Integer.valueOf(model.total_amount);
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).setData();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).getDataFromDb();
                    ((Category) Config.CURRENT_ACTIVITY_CONTEXT).updateMenu();



                }


            }
        });
        return view;
    }



    private class ViewHolder {
        public TextView name, type, quantity,rent,security;
        public ImageView image;
        public ImageButton plus, minus;
    }
}


