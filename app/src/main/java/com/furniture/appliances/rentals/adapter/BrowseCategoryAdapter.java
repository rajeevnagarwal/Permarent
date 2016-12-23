package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.Cat;
import com.furniture.appliances.rentals.model.ModelReviews;
import com.furniture.appliances.rentals.ui.RoundedBitmapView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajeev Nagarwal on 12/21/2016.
 */

public class BrowseCategoryAdapter extends RecyclerView.Adapter<BrowseCategoryAdapter.ViewHolder> {
    ArrayList<Cat> list;
    Context ctx;
    private LayoutInflater mInflater;
    public BrowseCategoryAdapter(Context ctx, ArrayList<Cat> list)
    {
        this.ctx = ctx;
        this.mInflater = LayoutInflater.from(ctx);
        this.list = list;
    }
    public BrowseCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View v = LayoutInflater.from(ctx).inflate(R.layout.row_category,parent,false);
        BrowseCategoryAdapter.ViewHolder vh = new BrowseCategoryAdapter.ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(final BrowseCategoryAdapter.ViewHolder holder, int position)
    {
       holder.name.setText(list.get(position).name);
       Picasso.with(ctx).load(R.drawable.user).into(holder.image,new com.squareup.picasso.Callback() {
           @Override
           public void onSuccess() {
               Bitmap bitmap = ((BitmapDrawable)holder.image.getDrawable()).getBitmap();
               holder.image.setImageDrawable(new RoundedBitmapView(ctx).createRoundedBitmapDrawableWithBorder(bitmap));

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

    public class ViewHolder extends RecyclerView.ViewHolder{
       public TextView name;
       public ImageView image;
        public ViewHolder(View v)
        {
            super(v);
            name = (TextView)v.findViewById(R.id.cat_name);
            image = (ImageView)v.findViewById(R.id.cat_image);
        }

    }



}
