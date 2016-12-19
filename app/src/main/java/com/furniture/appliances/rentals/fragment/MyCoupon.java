package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.CouponAdapter;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/14/2016.
 */

public class MyCoupon extends Fragment {
    public static String TAG = "MyCoupon";
    private ListView coupons;
    private ArrayList<String> data;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_coupon, container, false);
        ((MainActivity) getActivity()).changeToolbar("My Coupon", false);
        initialize(v);
        if(new CheckInternetConnection(getActivity()).isConnectedToInternet()) {
            fetch_coupons();

        }
        else
        {
            new CheckInternetConnection(getActivity()).showDialog();
        }
        return v;
    }
    private void fetch_coupons()
    {
        EndPonits.getCoupons(new RequestParams(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG,"Failure in Coupon");
                setDefaultView();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(responseString);
                setView(responseString);

            }
        });
    }
    private void setDefaultView()
    {
        data.add("No offers:");
        CouponAdapter adapter = new CouponAdapter(getActivity(),data);
        coupons.setAdapter(adapter);
    }


    private void setView(String response)
    {
        data = parseJSON(data,response);
        CouponAdapter adapter = new CouponAdapter(getActivity(),data);
        coupons.setAdapter(adapter);

    }
    private ArrayList<String> parseJSON(ArrayList<String> list,String response)
    {
        try {
            JSONArray array = new JSONArray(response);
            for(int i=0;i<array.length();i++)
            {
                JSONObject object = array.getJSONObject(i);
               data.add(object.toString());
                System.out.println(data.get(i));


            }
            return data;

        }
        catch(Exception e)
        {
            return list;
        }
    }



    private void initialize(View v)
    {
        coupons = (ListView)v.findViewById(R.id.lv_coupon);
        data = new ArrayList<String>();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
