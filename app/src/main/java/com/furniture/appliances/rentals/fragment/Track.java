package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.TrackAdapter;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/28/2016.
 */

public class Track extends Fragment {
    public static final String TAG = "Track Your Order";
    ArrayList<ModelOrder> modelOrderArrayList;
    ListView track_recycle;
    TrackAdapter adapter;
    AppPreferences apref = new AppPreferences();
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_track, container, false);
        ((MainActivity) getActivity()).changeToolbar("Track Your Order", false);
        getActivity().setTitle("Track Your Orders");
        if(AppPreferences.readString(getActivity(),"email",null)==null)
        {
            Toast.makeText(getActivity(),"Please login first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            initialization(v);
            fetchOrders();
            setView();
        }
        return v;

    }
    private void setView()
    {
        adapter = new TrackAdapter(modelOrderArrayList,getActivity());
        track_recycle.setAdapter(adapter);
    }

    private void initialization(View v)
    {
        modelOrderArrayList  = new ArrayList<>();
        track_recycle = (ListView)v.findViewById(R.id.track_recyclerView);
    }

    private void fetchOrders()
    {
        if(new CheckInternetConnection(getActivity()).isConnectedToInternet())
        {
            EndPonits.getOrderDetails(null, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("Failure in Track");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    System.out.println("Success"+responseString);
                    try {
                        JSONArray array = new JSONArray(responseString);
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject obj = array.getJSONObject(i);
                            if(AppPreferences.readString(getActivity(),"email",null).equals(obj.getString("email")))
                            {
                                String orderId = obj.getString("orderId");
                                String productName = obj.getJSONArray("items").toString();
                                String orderStatus = obj.getString("orderStatus");
                                ModelOrder order = new ModelOrder();
                                order.orderid = orderId;
                                order.productName = productName;
                                order.orderStatus = orderStatus;
                                modelOrderArrayList.add(order);
                                adapter.notifyDataSetChanged();


                            }
                        }

                    }
                    catch(Exception e)
                    {

                    }

                }
            });

        }
        else
        {
            new CheckInternetConnection(getActivity()).showDialog();
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
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

    }
}
