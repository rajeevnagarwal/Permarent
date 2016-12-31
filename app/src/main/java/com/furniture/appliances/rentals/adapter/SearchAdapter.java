package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.furniture.appliances.rentals.util.AppPreferences;

import java.util.ArrayList;

/**
 * Created by Infinia on 07-10-2015.
 */
public class SearchAdapter extends BaseAdapter implements Filterable {

    Context context;
    private LayoutInflater mInflater;
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> filteredList = new ArrayList<>();
    private FilterCity filterCity;
    AppPreferences apref = new AppPreferences();

    public SearchAdapter(Context context, ArrayList<String> cityList) {
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
        final ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.search_item, parent, false);
            holder = new ViewHolder();
            holder.city = (TextView) view.findViewById(R.id.city);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final String temp  = cityList.get(position);
        holder.city.setText(temp);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* apref.writeString(context, "city", temp);
                Toast.makeText(context, "City changed to " + temp, Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
                ((SearchCity)context).finish();
                //System.out.println("Search");
            }
        });*/
        /*holder.city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Search");
            }
        });*/

        return view;
    }


    private class ViewHolder {
        public TextView city;
    }

    @Override
    public Filter getFilter() {
        if (filterCity == null) {
            filterCity = new FilterCity();
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




