package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelReviews;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/14/2016.
 */

public class ReviewAdapter extends BaseAdapter {
    ArrayList<ModelReviews> data = new ArrayList<ModelReviews>();
    Context context;
    private LayoutInflater mInflater;
    public ReviewAdapter(Context ctx,ArrayList<ModelReviews> list)
    {
        this.context = ctx;
        this.mInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        final ReviewAdapter.ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.review_item, parent, false);
            holder = new ReviewAdapter.ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.review_name);
            holder.email = (TextView)view.findViewById(R.id.review_mail);
            holder.rating  = (TextView)view.findViewById(R.id.review_rating);
            holder.review = (TextView)view.findViewById(R.id.review_rv);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ReviewAdapter.ViewHolder) view.getTag();
        }
        final ModelReviews model = data.get(position);
        holder.name.setText(model.firstName+" "+model.lastName);
        holder.rating.setText(model.rating);
        holder.email.setText(model.mail);
        holder.review.setText(model.review);

        return view;
    }




    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        public TextView review;
        public TextView name;
        public TextView rating;
        public TextView email;
    }

}
