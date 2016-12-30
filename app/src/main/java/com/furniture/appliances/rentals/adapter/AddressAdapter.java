package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelAddress;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Infinia on 30-09-2015.
 */
public class AddressAdapter extends BaseAdapter {

    ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<ModelAddress>();
    Context context;
    private LayoutInflater mInflater;

    public AddressAdapter(Context context,ArrayList<ModelAddress> modelAddressArrayList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.modelAddressArrayList = modelAddressArrayList;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        final ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.address_item, parent, false);
            holder = new ViewHolder();
            holder.location= (TextView) view.findViewById(R.id.location);
            holder.city = (TextView) view.findViewById(R.id.city);
            holder.state = (TextView)view.findViewById(R.id.state);
            holder.houseNo = (TextView)view.findViewById(R.id.houseNo);
            holder.localityName = (TextView)view.findViewById(R.id.localityName);
            holder.others = (TextView)view.findViewById(R.id.others);
            holder.pinCode = (TextView)view.findViewById(R.id.pinCode);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final ModelAddress model = modelAddressArrayList.get(position);
        System.out.println(model.city);
        holder.city.setText(model.city+",");
        holder.localityName.setText(model.localityName+",");
        holder.location.setText(model.location+",");
        holder.pinCode.setText(model.pincode);
        holder.others.setText(model.others+",");
        holder.houseNo.setText(model.houseNo+",");
        holder.state.setText(model.state+",");
        return view;
    }

    private class ViewHolder {
        public TextView location,city,state,houseNo,localityName,pinCode,others;
    }
}
