package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.WishListAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelOptions;
import com.furniture.appliances.rentals.model.ModelProduct;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Exchanger;

/**
 * Created by Rajeev Nagarwal on 12/10/2016.
 */

public class WishList extends Fragment {
    public static final String TAG = "WishList";
    private AppPreferences apref = new AppPreferences();
    private RecyclerView wish_recycleview;
    private ArrayList<ModelProduct> data;
    private WishListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_wish, container, false);
        ((MainActivity)getActivity()).changeToolbar("WishList",false);
        if(!apref.IsLoginedByEmail(getActivity())&&!apref.IsLoginedByFb(getActivity())&&!apref.IsLoginedByGoogle(getActivity()))
        {
            Toast.makeText(getActivity().getApplicationContext(),"Please LoginIn first",Toast.LENGTH_SHORT).show();
        }
        else
        {

            if(new CheckInternetConnection(getActivity()).isConnectedToInternet()) {
                initialize(v);
                setView(wish_recycleview,data);
                fetchWishlist();



            }
            else {
                new CheckInternetConnection(getActivity()).showDialog();
            }



        }

        return v;
    }
    private void setView(RecyclerView view,ArrayList<ModelProduct> list)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        view.setLayoutManager(layoutManager);
        adapter = new WishListAdapter(list,getActivity());
        view.setAdapter(adapter);
    }

    private void initialize(View v)
    {
        data = new ArrayList<>();
        wish_recycleview = (RecyclerView)v.findViewById(R.id.wish_recyclerview);
    }


    private void fetchWishlist()
    {

        DBInteraction db = new DBInteraction(getActivity());
        String result =  db.getWishProducts(apref.readString(getActivity(),"email",""));
        result = result.trim();
        String[] p = result.split(" ");
        for(int i=0;i<p.length;i++) {
            System.out.println(p[i]);
            RequestParams params = new RequestParams();
        params.put("productId",p[i]);
        EndPonits.getProductDetails(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(responseString);
                if(!responseString.equals("Product Id Invalid")) {
                    data = parseResponse(responseString, data);
                    adapter.notifyDataSetChanged();
                }


            }
        });
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private ArrayList<ModelProduct> parseResponse(String response, ArrayList<ModelProduct> list)
    {
        try {
            JSONArray array = new JSONArray(response);
            for(int i=0;i<array.length();i++)
            {
                JSONObject obj = array.getJSONObject(i);
                String available="";
                if(obj.getInt("productAvailability")==1)
                {
                    available = "Available";
                }
                else
                {
                    available = "Not Available";
                }
                list.add(new ModelProduct(obj.getString("productName"),obj.getString("minRentalDuration"),String.valueOf(obj.getInt("newRating")),available,String.valueOf(obj.getInt("securityAmount")),String.valueOf(obj.getInt("retailPrice")),String.valueOf(obj.getInt("productUserLikesCount"))));

            }
        }
        catch(Exception e)
        {

        }
        return list;

    }

}

