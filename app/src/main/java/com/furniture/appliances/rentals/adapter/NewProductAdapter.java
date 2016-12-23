package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelProduct;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/22/2016.
 */

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ModelProduct> list;
    AppPreferences apref = new AppPreferences();
    public NewProductAdapter(Context ctx,ArrayList<ModelProduct> list)
    {
        this.list = list;
        this.context = ctx;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView new_name;
        public TextView new_price;
        public ImageView new_img;
        public ImageView new_wish;
        public Button new_cart;
        public RatingBar new_ratingbar;
        public ViewHolder(View v)
        {
            super(v);
            new_name = (TextView)v.findViewById(R.id.new_name);
            new_price = (TextView)v.findViewById(R.id.new_price);
            new_img = (ImageView)v.findViewById(R.id.new_img);
            new_wish = (ImageView)v.findViewById(R.id.new_wish);
            new_cart = (Button)v.findViewById(R.id.new_cart);
            new_ratingbar = (RatingBar)v.findViewById(R.id.new_rating);
        }

    }
    public NewProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.row_new,parent,false);
        NewProductAdapter.ViewHolder vh = new NewProductAdapter.ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(final NewProductAdapter.ViewHolder holder, int position)
    {
        final ModelProduct model = list.get(position);
        Picasso.with(context).load(R.drawable.user).into(holder.new_img);
        holder.new_name.setText(model.pname);
        holder.new_price.setText("₹"+model.retail_price);
        holder.new_ratingbar.setRating(Float.parseFloat(model.rating));
        final DBInteraction db = new DBInteraction(context);
        if(db.checkWishProduct(model.id))
        {
            Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.new_wish);
            holder.new_wish.setClickable(false);
        }
        else
        {
            Picasso.with(context).load(R.drawable.ic_heart).into(holder.new_wish);
            holder.new_wish.setClickable(true);
        }
        holder.new_wish.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(db.insertWishItem(model.id,apref.readString(context,"email",""),apref.readString(context,"name","")))
                {
                    Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.new_wish);
                    holder.new_wish.setClickable(false);

                }

            }

        });

    }
    public int getItemCount()
    {
        return list.size();
    }


}
