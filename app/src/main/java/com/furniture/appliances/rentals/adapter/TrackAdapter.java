package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.furniture.appliances.rentals.MyOrderDetails;
import com.furniture.appliances.rentals.OrderStatus;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.fragment.Track;
import com.furniture.appliances.rentals.model.ModelOrder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Rajeev Nagarwal on 12/29/2016.
 */

public class TrackAdapter extends BaseAdapter {
    private ArrayList<ModelOrder> list;
    private Context context;
    private LayoutInflater inflater;
    String[] array = {"InProgress","Placed","Dispatched","Delivered"};
    public TrackAdapter(ArrayList<ModelOrder> list,Context context)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        final TrackAdapter.ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.row_trackorder, parent, false);
            holder = new TrackAdapter.ViewHolder();
            holder.orderId = (TextView) view.findViewById(R.id.track_corderid);
            holder.productName = (TextView) view.findViewById(R.id.track_products);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (TrackAdapter.ViewHolder) view.getTag();
        }
        holder.orderId.setText("Order ID: ");
        holder.productName.setText("Products:\n");
        final ModelOrder model = list.get(position);
        holder.orderId.append(model.orderid);
        try
        {
            JSONArray array = new JSONArray(model.productName);
            for(int i = 0;i<array.length();i++)
            {
                JSONObject object = array.getJSONObject(i);
                String sen = object.getString("productName");
                holder.productName.append("Products: "+sen.split("_")[0]+", Quantity: "+object.getString("quantity"));
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OrderStatus.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("status",model.orderStatus);
                context.startActivity(i);

            }
        });
        return view;
    }
    private class ViewHolder {
        public TextView orderId;
        public TextView productName;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}
