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
import android.widget.Toast;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.ComplaintOrderAdapter;
import com.furniture.appliances.rentals.model.ModelComplaintOrder;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/7/2016.
 */

public class ComplaintsFragment extends Fragment {

    public static final String TAG = "Complaints";
    AppPreferences apref = new AppPreferences();
    private RecyclerView recyclerView;
    private ArrayList<ModelComplaintOrder> list;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_complaint, container, false);
        ((MainActivity)getActivity()).changeToolbar("Your Complaints",false);
        if(!apref.IsLoginedByEmail(getActivity())&&!apref.IsLoginedByFb(getActivity())&&!apref.IsLoginedByGoogle(getActivity()))
        {
            Toast.makeText(getActivity().getApplicationContext(),"Please LoginIn first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            initialize(v);
            if(new CheckInternetConnection(getActivity()).isConnectedToInternet()) {
                populatelist();
            }
            else {
                new CheckInternetConnection(getActivity()).showDialog();
            }



        }
        return v;
    }
    private void setView()
    {
        System.out.println("hello1 "+list.size());
        ComplaintOrderAdapter adapter = new ComplaintOrderAdapter(list,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void initialize(View v)
    {
        list = new ArrayList<ModelComplaintOrder>();
        recyclerView = (RecyclerView)v.findViewById(R.id.order_recyclerView);

    }

    private void populatelist()
    {
        fetchComplaints("");
    }

    private void fetchComplaints(String email)
    {
        RequestParams params = new RequestParams();
        EndPonits.getOrderDetails(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //System.out.println(responseString);
                try {


                    JSONArray array = new JSONArray(responseString);
                    System.out.println(array.length());
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject order = (JSONObject) array.get(i);
                        String complaints = order.getString("complaintDetails");
                        System.out.println(complaints);
                        Integer complaint_count = (order.getInt("totalComplaintsCount"));
                        String orderId = (order.getString("orderId"));
                        String orderDate = (order.getString("orderDate"));
                        list.add(new ModelComplaintOrder(orderId,complaints,complaint_count,orderDate));
                        System.out.println("Hello 2"+list.size());
                        setView();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();

                }

            }
        });

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
        menu.findItem(R.id.ab_cart).setVisible(true).setEnabled(true);
    }

}
