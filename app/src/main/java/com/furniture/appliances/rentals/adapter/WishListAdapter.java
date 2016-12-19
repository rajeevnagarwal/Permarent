package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.fragment.WishList;
import com.furniture.appliances.rentals.model.ModelProduct;
import com.furniture.appliances.rentals.model.Subcategory;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/11/2016.
 */

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ModelProduct> list;
    public WishListAdapter(ArrayList<ModelProduct> list, Context ctx)
    {
        this.list = list;
        this.context =ctx;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView wish_name;
        public TextView wish_price;
        public TextView wish_security;
        public TextView wish_period;
        public MyViewHolder(View v)
        {
            super(v);
            wish_name= (TextView)v.findViewById(R.id.wish_name);
            wish_price= (TextView)v.findViewById(R.id.wish_price);
            wish_security= (TextView)v.findViewById(R.id.wish_security);

            wish_period= (TextView)v.findViewById(R.id.wish_period);



        }

    }
    public WishListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.row_wish,parent,false);
        WishListAdapter.MyViewHolder vh = new WishListAdapter.MyViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(WishListAdapter.MyViewHolder holder, int position)
    {
        holder.wish_name.setText(list.get(position).pname);
        holder.wish_period.setText("Minimum Rental Duration: "+list.get(position).minRentalDuration);
        holder.wish_security.setText("Security: "+"₹"+list.get(position).sec_amount);
        holder.wish_price.setText("MRP: "+"₹"+list.get(position).retail_price);

    }
    public int getItemCount()
    {
        return list.size();
    }

}
