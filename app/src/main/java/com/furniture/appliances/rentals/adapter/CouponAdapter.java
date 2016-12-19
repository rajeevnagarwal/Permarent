package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/14/2016.
 */

public class CouponAdapter extends BaseAdapter {
    ArrayList<String> data = new ArrayList<String>();
    Context context;
    private LayoutInflater mInflater;
    public CouponAdapter(Context ctx,ArrayList<String> list)
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
        final CouponAdapter.ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.coupon_item, parent, false);
            holder = new CouponAdapter.ViewHolder();
            holder.code = (TextView) view.findViewById(R.id.coupon_code);
            holder.offer = (TextView)view.findViewById(R.id.coupon_offer);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (CouponAdapter.ViewHolder) view.getTag();
        }
        final String model = data.get(position);
        holder.code.setText(parseCode(model));
        holder.offer.setText(parseOffer(model));

        return view;
    }
    private String parseCode(String text)
    {
        try
        {
            JSONObject obj = new JSONObject(text);
            return "Code: "+obj.getString("couponCode");

        }
        catch (Exception e) {
            return "";
        }
    }
    private String parseOffer(String text)
    {
        try
        {
            JSONObject obj = new JSONObject(text);
            String result = "Offer(All quantities in Rupees): \n";
            result = result+"Monthly Rent: "+obj.getJSONObject("discountApplicable").getInt("monthlyRent")+"\n"+"Security: "+obj.getJSONObject("discountApplicable").getInt("security")+"\n"+"Shipping Charges: "+obj.getJSONObject("discountApplicable").getInt("shippingCharges")+"\n"+"Floor Charges: "+obj.getJSONObject("discountApplicable").getInt("floorCharges")+"\n"+"Installation Charges: "+obj.getJSONObject("discountApplicable").getInt("installationCharges")+"\n"+"Applicable Months: "+obj.getJSONObject("discountApplicable").getInt("applicableMonths")+"\n"+"Offer Start: "+obj.getString("offerStartDate")+"\n"+"Offer End: "+obj.getString("offerEndDate");

            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "";

        }
    }




    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        public TextView code;
        public TextView offer;
    }

}
