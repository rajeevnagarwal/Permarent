package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.dq.rocq.models.Position;
import com.furniture.appliances.rentals.Cart;
import com.furniture.appliances.rentals.ProductDetails;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelProduct;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.furniture.appliances.rentals.R.id.ratingBar;

/**
 * Created by Rajeev Nagarwal on 12/20/2016.
 */

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ModelProduct> list;
    AppPreferences apref = new AppPreferences();
    public PopularAdapter(Context ctx,ArrayList<ModelProduct> list)
    {
        this.list = list;
        this.context =ctx;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView popular_name;
        public TextView popular_price;
        public ImageView popular_img;
        public ImageView popular_wish;
        public Button popular_cart;
        public RatingBar ratingbar;
        public MyViewHolder(View v)
        {
            super(v);
            popular_name = (TextView)v.findViewById(R.id.popular_name);
            popular_price = (TextView)v.findViewById(R.id.popular_price);
            popular_img = (ImageView)v.findViewById(R.id.popular_img);
            popular_wish = (ImageView)v.findViewById(R.id.popular_wish);
            popular_cart = (Button)v.findViewById(R.id.popular_cart);
            ratingbar = (RatingBar)v.findViewById(R.id.popular_rating);


        }

    }
    public PopularAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.row_popular,parent,false);
        PopularAdapter.MyViewHolder vh = new PopularAdapter.MyViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(final PopularAdapter.MyViewHolder holder, int position)
    {
        final ModelProduct model = list.get(position);
        Picasso.with(context).load(R.drawable.user).into(holder.popular_img);
        holder.popular_name.setText(model.pname);
        holder.popular_price.setText("₹"+model.retail_price);
        holder.ratingbar.setRating(Float.parseFloat(model.rating));
        final DBInteraction db = new DBInteraction(context);
        if(db.checkWishProduct(model.id))
        {
            Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.popular_wish);
            holder.popular_wish.setClickable(false);
        }
        else
        {
            Picasso.with(context).load(R.drawable.ic_heart).into(holder.popular_wish);
            holder.popular_wish.setClickable(true);
        }
        holder.popular_wish.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(db.insertWishItem(model.id, AppPreferences.readString(context,"email",""), AppPreferences.readString(context,"name","")))
                {
                    Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.popular_wish);
                    holder.popular_wish.setClickable(false);

                }

            }

        });
        holder.popular_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetails.class);
                i.putExtra("productId",model.id);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);


            }
        });

    }
    public int getItemCount()
    {
        return list.size();
    }




}
