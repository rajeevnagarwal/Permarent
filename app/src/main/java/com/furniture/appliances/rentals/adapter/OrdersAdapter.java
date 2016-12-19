package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.furniture.appliances.rentals.MyOrderDetails;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelOrder;

import java.util.ArrayList;

/**
 * Created by Infinia on 30-10-2015.
 */
public class OrdersAdapter extends BaseAdapter {

    ArrayList<ModelOrder> modelOrderArrayList = new ArrayList<ModelOrder>();
    Context context;
    private LayoutInflater mInflater;

    public OrdersAdapter(Context context,ArrayList<ModelOrder> modelOrderArrayList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.modelOrderArrayList = modelOrderArrayList;
    }
    @Override
    public int getCount() {
        return modelOrderArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelOrderArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        final ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.orders_item, parent, false);
            holder = new ViewHolder();
            holder.orderId = (TextView) view.findViewById(R.id.orderId);
            holder.amountpaid = (TextView) view.findViewById(R.id.amountpaid);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final ModelOrder model = modelOrderArrayList.get(position);
        holder.orderId.setText(model.orderid);
        holder.amountpaid.setText(context.getResources().getString(R.string.Rs)+model.amountpaid);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(context, MyOrderDetails.class);
                    i.putExtra("model", model);
                    context.startActivity(i);
            }
        });
        return view;
    }

    private class ViewHolder {
        public TextView orderId,amountpaid;
    }
}

