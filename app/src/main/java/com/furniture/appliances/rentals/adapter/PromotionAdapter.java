package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.util.Config;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/8/2016.
 */

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {
    private ArrayList<String> list;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView promotions;
                public MyViewHolder(View v)
        {
            super(v);
            promotions = (TextView)v.findViewById(R.id.promo_line);
        }



    }
    public PromotionAdapter(ArrayList<String> list,Context context)
    {
        this.list = list;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promotion_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.promotions.setText(list.get(position));
        System.out.println(position);
        //Picasso.with(context).load("http://88.198.151.27/images/homeslider/slider4.jpg")
        if(position==0)
        {
            Picasso.with(context).load("http://88.198.151.27/images/homeslider/slide3.jpg").into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    System.out.println("complete");
                    holder.promotions.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    System.out.println("fail");

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
        else if(position==1){
            Picasso.with(context).load("http://88.198.151.27/images/homeslider/slide4.jpg").into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    System.out.println("complete");
                    holder.promotions.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    System.out.println("fail");

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        }

    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }



}
