package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.Listeners.MyGestureListener;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Infinia on 13-11-2015.
 */
public class OrderDetailsAdapter extends BaseAdapter {

    ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<>();
    Context context;
    private LayoutInflater mInflater;

    public OrderDetailsAdapter(Context context,ArrayList<ModelSubCategory> modelSubCategoryArrayList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.modelSubCategoryArrayList = modelSubCategoryArrayList;
    }
    @Override
    public int getCount() {
        return modelSubCategoryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelSubCategoryArrayList.get(position);
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
            view = mInflater.inflate(R.layout.orderdetails_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.image = (ImageView) view.findViewById(R.id.image);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final ModelSubCategory model = modelSubCategoryArrayList.get(position);
        holder.name.setText(model.prod_name);
        String quantity="";
        if(model.quantity!=0)
            quantity = String.valueOf(model.quantity);
        if(model.quantity_monthly!=0)
            quantity = String.valueOf(model.quantity_monthly);
        if(model.quantity_quarterly!=0)
            quantity = String.valueOf(model.quantity_quarterly);
        holder.quantity.setText("Quantity: "+quantity);
        Picasso.with(holder.image.getContext())
                .load(Config.subCategoryImage + model.small_img)
                        //.placeholder(R.drawable.dummy)
                        //.error(R.drawable.dummy)
                .into(holder.image);
        holder.mDetector = new GestureDetectorCompat(context, new MyGestureListener(context,view));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v instanceof ViewGroup)
                {
                    ViewGroup g = (ViewGroup)v;
                    if(g.getChildAt(0).getVisibility()==View.VISIBLE) {
                        holder.mDetector.onTouchEvent(event);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else {

                    return true;
                }
            }
        });
        return view;
    }

    private class ViewHolder {
        public TextView name,quantity;
        public ImageView image;
        public GestureDetectorCompat mDetector;
    }
}

