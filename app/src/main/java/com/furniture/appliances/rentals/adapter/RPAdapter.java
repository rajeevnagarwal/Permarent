package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.ProductDetails;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelProduct;
import com.furniture.appliances.rentals.ui.RoundedBitmapView;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/22/2016.
 */

public class RPAdapter extends RecyclerView.Adapter<RPAdapter.ViewHolder> {
    public Context context;
    public ArrayList<ModelProduct> data;
    public LayoutInflater mInflater;
    AppPreferences apref = new AppPreferences();
    public RPAdapter(Context context,ArrayList<ModelProduct> data)
    {
        this.data = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public RPAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.rp_item,parent,false);
        RPAdapter.ViewHolder vh = new RPAdapter.ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(final RPAdapter.ViewHolder holder, int position)
    {
        final ModelProduct model = data.get(position);
        holder.name.setText(model.pname);
        holder.dim.setText(model.dimensions);
        holder.sec.setText(model.sec_amount);
        final DBInteraction db = new DBInteraction(context);
        if(db.checkWishProduct(model.id))
        {
            Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.wish);
            holder.wish.setClickable(false);
        }
        else
        {
            Picasso.with(context).load(R.drawable.ic_heart).into(holder.wish);
            holder.wish.setClickable(true);
        }
        holder.wish.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(db.insertWishItem(model.id, AppPreferences.readString(context,"email",""), AppPreferences.readString(context,"name","")))
                {
                    Picasso.with(context).load(R.drawable.ic_heart_disable).into(holder.wish);
                    holder.wish.setClickable(false);

                }

            }

        });
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
        holder.add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(context, ProductDetails.class);
                i.putExtra("productId",model.id);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        });

    }


    public int getItemCount()
    {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView dim;
        public TextView sec;
        public ImageView wish;
        public Button add;
        public ImageView image;
        public ViewHolder(View v)
        {
            super(v);
            name = (TextView)v.findViewById(R.id.rp_name);
            dim = (TextView)v.findViewById(R.id.rp_dim);
            sec = (TextView)v.findViewById(R.id.rp_sec);
            image = (ImageView)v.findViewById(R.id.rp_img);
            wish = (ImageView)v.findViewById(R.id.rp_wish);
            add = (Button)v.findViewById(R.id.rp_add);

        }

    }

}
