package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelComplaintOrder;
import com.furniture.appliances.rentals.model.ModelComplaintProduct;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class ComplaintProductAdapter extends RecyclerView.Adapter<ComplaintProductAdapter.MyViewHolder> {
    private ArrayList<ModelComplaintProduct> data;
    private Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView status;
        TextView date;
        TextView reason;
        ModelComplaintProduct order;
        Context context;

        public MyViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.productName = (TextView) itemView.findViewById(R.id.product_complaint_name);
            this.status = (TextView) itemView.findViewById(R.id.product_complaint_status);
            this.date = (TextView) itemView.findViewById(R.id.product_complaint_date);
            this.reason = (TextView) itemView.findViewById(R.id.product_complaint_reason);
            this.context = ctx;
        }

        public void bindOrder(ModelComplaintProduct order) {
            this.order = order;
            productName.append(order.getProduct_name());
            status.append(order.getStatus());
            date.append(order.getComplaint_date());
            reason.append(order.getReason());
        }
    }
    public ComplaintProductAdapter(ArrayList<ModelComplaintProduct> data,Context context)
    {
        this.data = data;
        this.context = context;
    }
    public ComplaintProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productcomplaint,parent,false);
        ComplaintProductAdapter.MyViewHolder v = new ComplaintProductAdapter.MyViewHolder(view,parent.getContext());
        return v;
    }
    public void onBindViewHolder(final ComplaintProductAdapter.MyViewHolder holder, final int position)
    {
        holder.bindOrder(data.get(position));
        if(data.get(position).getStatus().equals("unresolved"))
        {
            holder.itemView.setBackgroundColor(context
            .getResources().getColor(R.color.red));
        }
        else
        {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
    }
    public int getItemCount()
    {
        return data.size();
    }

}
