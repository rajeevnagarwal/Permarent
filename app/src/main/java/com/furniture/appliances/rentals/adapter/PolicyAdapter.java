package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelAddress;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/14/2016.
 */

public class PolicyAdapter extends BaseAdapter {
    ArrayList<String> data = new ArrayList<String>();
    Context context;
    private LayoutInflater mInflater;
    public PolicyAdapter(Context context,ArrayList<String> modelAddressArrayList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = modelAddressArrayList;
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
        final PolicyAdapter.ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.policy_item, parent, false);
            holder = new PolicyAdapter.ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.policy_text);
            holder.image = (ImageView)view.findViewById(R.id.policy_image);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (PolicyAdapter.ViewHolder) view.getTag();
        }
        final String model = data.get(position);
        holder.name.setText(model);
        if(model.equals("T&C"))
        {
            holder.image.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ic_star_grey600_24dp));

        }
        else if(model.equals("Privacy Policy"))
        {
            holder.image.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.ic_list_grey600_24dp));

        }
        else if(model.equals("Return and Refund Policy"))
        {
            holder.image.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_return));

        }
        return view;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        public TextView name;
        public ImageView image;
    }
}
