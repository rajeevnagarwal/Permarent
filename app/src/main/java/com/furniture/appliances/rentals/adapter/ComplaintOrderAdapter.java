package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furniture.appliances.rentals.ProductComplaintActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelComplaintOrder;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class ComplaintOrderAdapter extends RecyclerView.Adapter<ComplaintOrderAdapter.MyViewHolder> {
    private ArrayList<ModelComplaintOrder> data;
    private Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView orderId;
        TextView complaint_count;
        TextView orderDate;
        ModelComplaintOrder order;
        Context context;
        public MyViewHolder(View itemView,Context ctx)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            this.orderId = (TextView)itemView.findViewById(R.id.corderid);
            this.orderDate = (TextView)itemView.findViewById(R.id.corderdate);
            this.complaint_count = (TextView)itemView.findViewById(R.id.complaint_count);
            this.context = ctx;
        }
        public void bindOrder(ModelComplaintOrder order)
        {
            this.order = order;
            orderId.append(order.getOrderId());
            orderDate.append(order.getOrderDate());
            complaint_count.append(String.valueOf(order.getComplaint_count()));
        }

        public void onClick(View v)
        {
            Intent intent = new Intent(context,ProductComplaintActivity.class);
            intent.putExtra("order",order);
            context.startActivity(intent);

        }


    }
    public ComplaintOrderAdapter(ArrayList<ModelComplaintOrder> data,Context context)
    {
        this.data = data;
        this.context = context;
    }
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_complaintorder,parent,false);
        MyViewHolder v = new MyViewHolder(view,parent.getContext());
        return v;
    }
    public void onBindViewHolder(final MyViewHolder holder,final int position)
    {
        holder.bindOrder(data.get(position));
    }
    public int getItemCount()
    {
        return data.size();
    }





}
