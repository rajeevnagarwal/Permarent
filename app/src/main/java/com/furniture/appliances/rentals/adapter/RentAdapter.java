package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/8/2016.
 */

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> list;
    public RentAdapter(ArrayList<String> list,Context ctx)
    {
        this.list = list;
        this.context =ctx;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView rent_text;
        public MyViewHolder(View v)
        {
            super(v);
            rent_text= (TextView)v.findViewById(R.id.own_text);

        }

    }
    public RentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.rent_item,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(final MyViewHolder holder,int position)
    {
        holder.rent_text.setText(list.get(position));
        if(position==0)
        {
            Picasso.with(context).load("http://88.198.151.27/images/img-ro-bedroom.jpg").into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    System.out.println("complete");
                    holder.rent_text.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bitmap));
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
        else if(position==1)
        {
            Picasso.with(context).load("http://88.198.151.27/images/img-ro-hall.jpg").into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    System.out.println("complete");
                    holder.rent_text.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bitmap));
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
        else if(position==2)
        {
            Picasso.with(context).load("http://88.198.151.27/images/img-ro-kitchen.jpg").into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    System.out.println("complete");
                    holder.rent_text.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bitmap));
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
        else if(position==3)
        {
            Picasso.with(context).load("http://88.198.151.27/images/img-ro-office.jpg").into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    System.out.println("complete");
                    holder.rent_text.setBackgroundDrawable(new BitmapDrawable(context.getResources(),bitmap));
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
    public int getItemCount()
    {
        return list.size();
    }




}
