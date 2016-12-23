package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.Listeners.MyGestureListener;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelOptions;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.util.Config;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/10/2016.
 */

public class OptionsAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater mInflater;
    ArrayList<ModelOptions> data;
    public OptionsAdapter(Context context,ArrayList<ModelOptions> list)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = list;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        final OptionsAdapter.ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.row_options, parent, false);
            holder = new OptionsAdapter.ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.options_name);
            holder.image = (ImageView)view.findViewById(R.id.img_option);


            view.setTag(holder);
        } else {
            view = convertView;
            holder = (OptionsAdapter.ViewHolder) view.getTag();
        }
        holder.name.setText(data.get(position).name);
        Picasso.with(holder.image.getContext())
               .load(data.get(position).image)
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });



        return view;
    }
    private class ViewHolder {
       public TextView name;
       public ImageView image;
    }

}
