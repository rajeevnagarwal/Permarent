package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furniture.appliances.rentals.Category;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.Subcategory;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Subcategory> list;
    public CategoryAdapter(ArrayList<Subcategory> list, Context ctx)
    {
        this.list = list;
        this.context =ctx;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView category_text;
        public MyViewHolder(View v)
        {
            super(v);
            category_text= (TextView)v.findViewById(R.id.own_text);

        }

    }
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.rent_item,parent,false);
        CategoryAdapter.MyViewHolder vh = new CategoryAdapter.MyViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position)
    {
        holder.category_text.setText(list.get(position).getName());

    }
    public int getItemCount()
    {
        return list.size();
    }
}
