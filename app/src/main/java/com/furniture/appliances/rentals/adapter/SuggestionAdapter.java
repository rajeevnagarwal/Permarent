package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.SearchCity;
import com.furniture.appliances.rentals.fragment.Account;
import com.furniture.appliances.rentals.fragment.CategoryFragment;
import com.furniture.appliances.rentals.util.AppPreferences;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/16/2016.
 */

public class SuggestionAdapter extends BaseAdapter implements Filterable {

    Context context;
    private LayoutInflater mInflater;
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> filteredList = new ArrayList<>();
    private SuggestionAdapter.FilterCity filterCity;
    AppPreferences apref = new AppPreferences();

    public SuggestionAdapter(Context context, ArrayList<String> cityList) {
        this.mInflater = LayoutInflater.from(context);
        this.cityList = cityList;
        this.context = context;
        this.filteredList = cityList;

    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final SuggestionAdapter.ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.search_item, parent, false);
            holder = new SuggestionAdapter.ViewHolder();
            holder.city = (TextView) view.findViewById(R.id.city);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (SuggestionAdapter.ViewHolder) view.getTag();
        }
        final String temp  = cityList.get(position);
        holder.city.setText(temp);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.tabhost.setCurrentTab(1);


            }
        });
        return view;
    }


    private class ViewHolder {
        public TextView city;
    }

    @Override
    public Filter getFilter() {
        if (filterCity == null) {
            filterCity = new SuggestionAdapter.FilterCity();
        }

        return filterCity;
    }




    //Filter class to search contacts
    private class FilterCity extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> tempList = new ArrayList<>();

                // search content in friend list
                for (String city : filteredList) {
                    if (city.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(city);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
            }

            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            cityList = (ArrayList<String>) results.values;
            notifyDataSetChanged();


        }
    }
}





