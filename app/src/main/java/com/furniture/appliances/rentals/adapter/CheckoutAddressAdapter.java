package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.furniture.appliances.rentals.Checkout2;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.util.AppPreferences;

import java.util.ArrayList;

/**
 * Created by Infinia on 30-09-2015.
 */
public class CheckoutAddressAdapter extends BaseAdapter {

    ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<>();
    Context context;
    private LayoutInflater mInflater;
    Integer selected_position=-1;
    AppPreferences apref = new AppPreferences();
    public CheckoutAddressAdapter(Context context, ArrayList<ModelAddress> modelAddressArrayList)
    {
        this.context= context;
        this.modelAddressArrayList=modelAddressArrayList;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return modelAddressArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelAddressArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.checkout_address_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.detail = (TextView) view.findViewById(R.id.detail);
            holder.mobile = (TextView) view.findViewById(R.id.mobile);
            holder.cb = (CheckBox) view.findViewById(R.id.cb);
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            holder.cb.setButtonDrawable(id);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final ModelAddress modelAddress = modelAddressArrayList.get(position);
        holder.name.setText(modelAddress.house + ", "+modelAddress.houseNo + ", "+modelAddress.location+", "+modelAddress.street+", "+modelAddress.localityName);
        holder.detail.setText(modelAddress.pincode);
        System.out.println("Mobile"+modelAddress.mobile_no);
        holder.mobile.setText(modelAddress.mobile_no);
        //holder.mobile.setText(apref.readString(context,"mobile",null));
        if(modelAddressArrayList.size()==1)
        {
            holder.cb.setChecked(true);
            selected_position = position;
            ((Checkout2) context).setData(modelAddressArrayList.get(position));
        }
        else {
            if (position == selected_position) {
                holder.cb.setChecked(true);
            } else {
                holder.cb.setChecked(false);
            }
        }
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selected_position = position;
                    ((Checkout2) context).setData(modelAddressArrayList.get(position));
                } else {
                    selected_position = -1;
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private class ViewHolder {
        public TextView name, detail,mobile;
        CheckBox cb;
    }
}

