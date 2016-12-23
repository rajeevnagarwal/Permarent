package com.furniture.appliances.rentals.adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.fragment.SubCategory;
import com.furniture.appliances.rentals.model.Subcategory;
import com.furniture.appliances.rentals.ui.RoundedBitmapView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubCategoryRecycle extends RecyclerView.Adapter<SubCategoryRecycle.ViewHolder>
{
    public ArrayList<Subcategory> sub;
    public Context context;
    public SubCategoryRecycle(Context context,ArrayList<Subcategory> list)
    {
        this.context = context;
        this.sub = list;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView image;
        public TextView name;
        public ArrayList<Subcategory> data;
        public ViewHolder(View v)
        {
            super(v);
            image = (ImageView)v.findViewById(R.id.sub_image);
            name = (TextView)v.findViewById(R.id.sub_name);
            data = new ArrayList<Subcategory>();

        }
    }

    public SubCategoryRecycle.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.row_sub,parent,false);
        SubCategoryRecycle.ViewHolder vh = new SubCategoryRecycle.ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(final SubCategoryRecycle.ViewHolder holder, final int position)
    {
        System.out.println("Called");
        holder.name.setText(sub.get(position).getName());
        holder.data = sub;
       Picasso.with(context).load(R.drawable.user).into(holder.image,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable)holder.image.getDrawable()).getBitmap();
                holder.image.setImageDrawable(new RoundedBitmapView(context).createRoundedBitmapDrawableWithBorder(bitmap));

            }


            @Override
            public void onError() {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              SubCategory fragment = new SubCategory();
                fragment.setCode(holder.data.get(position).getId());
                fragment.setTitle(holder.data.get(position).getName());
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,SubCategory.TAG).addToBackStack(SubCategory.TAG).commit();




            }
        });

    }
    public int getItemCount()
    {
        return sub.size();
    }


}