package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.fragment.WishList;
import com.furniture.appliances.rentals.model.ModelProduct;
import com.furniture.appliances.rentals.model.Subcategory;
import com.furniture.appliances.rentals.ui.RoundedBitmapView;
import com.squareup.picasso.Picasso;

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
        public ImageView wish_img;
        public TextView wish_name;
        public TextView wish_dimen;
        public MyViewHolder(View v)
        {
            super(v);
            wish_name= (TextView)v.findViewById(R.id.wish_name);
            wish_img = (ImageView)v.findViewById(R.id.wish_img);
            wish_dimen = (TextView)v.findViewById(R.id.wish_dimen);



        }

    }
    public WishListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.row_wish,parent,false);
        WishListAdapter.MyViewHolder vh = new WishListAdapter.MyViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(final WishListAdapter.MyViewHolder holder, int position)
    {
        holder.wish_name.setText(list.get(position).pname);
        holder.wish_dimen.setText(list.get(position).dimensions);
        System.out.println(list.get(position).pname);
        System.out.println(list.get(position).dimensions);
        Picasso.with(context).load(R.drawable.user).into(holder.wish_img,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable)holder.wish_img.getDrawable()).getBitmap();
                holder.wish_img.setImageDrawable(new RoundedBitmapView(context).createRoundedBitmapDrawableWithBorder(bitmap));

            }

            @Override
            public void onError() {

            }
        });

    }
    public int getItemCount()
    {
        return list.size();
    }

}
