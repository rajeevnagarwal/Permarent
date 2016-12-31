package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.furniture.appliances.rentals.Login;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.OrdersAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelOrder;
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
 * Created by Infinia on 22-09-2015.
 */
public class MyOrders extends Fragment {
    public static final String TAG = "MyOrders";
    RelativeLayout rl1,rl2,rl3;
    AppPreferences apref = new AppPreferences();
    Button login;
    ListView lv;
    ArrayList<ModelOrder> modelOrderArrayList = new ArrayList<ModelOrder>();
    OrdersAdapter ordersAdapter;
    LinearLayout linearlayout;

    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ((MainActivity)getActivity()).changeToolbar("My Orders", false);
        initView(v);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
            }
        });
        return v;
    }
    private void fetchOrders()
    {
        if(new CheckInternetConnection(getActivity()).isConnectedToInternet())
        {
            RequestParams params = new RequestParams();
            EndPonits.getOrderDetails(params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("Failure in Orders");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    System.out.println("Success"+responseString);
                    try {
                        JSONArray array = new JSONArray(responseString);
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject object = array.getJSONObject(i);
                            if(AppPreferences.readString(getActivity(),"email",null).equals(object.getString("email")))
                            {
                                String orderId = object.getString("orderId");
                                String amount = object.getString("totalRental");
                               // String productName = object.getString("productName");
                                ModelOrder order = new ModelOrder();
                                order.orderid = orderId;
                                order.totalRental = amount;
                                order.totalSecurity = object.getString("totalSecurity");
                                order.productName = object.getJSONArray("items").toString();
                                //order.productName = productName;
                                modelOrderArrayList.add(order);
                            }
                        }
                        if(modelOrderArrayList.size()!=0)
                        {
                            rl3.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                        }
                        refreshAdapter(modelOrderArrayList);

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });

        }
        else
        {
            new CheckInternetConnection(getActivity()).showDialog();
        }
    }

    private void initView(View v)
    {
        linearlayout = (LinearLayout)v.findViewById(R.id.lv_complaint);
        rl1 = (RelativeLayout)v.findViewById(R.id.rl1);
        rl2 = (RelativeLayout)v.findViewById(R.id.rl2);
        rl3 = (RelativeLayout)v.findViewById(R.id.rl3);
        lv = (ListView)v.findViewById(R.id.lv);
        login = (Button )v.findViewById(R.id.login);
        if(apref.IsLoginedByGoogle(getActivity()) || apref.IsLoginedByEmail(getActivity()) || apref.IsLoginedByFb(getActivity()))
        {
            rl1.setVisibility(View.GONE);
            rl2.setVisibility(View.VISIBLE);
            fetchOrders();
            //getDataFromDb();
            /*if(modelOrderArrayList.size()!=0)
            {
                rl3.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
            }*/
        }
        linearlayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ComplaintsFragment.TAG);
                if(fragment==null)
                {
                    fragment = new ComplaintsFragment();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,ComplaintsFragment.TAG).addToBackStack(ComplaintsFragment.TAG).commit();
            }

        });

    }
    private void getDataFromDb()
    {
        DBInteraction dbInteraction = new DBInteraction(getActivity());
        //modelOrderArrayList = dbInteraction.getAllOrders();
        dbInteraction.close();
        refreshAdapter(modelOrderArrayList);
    }
    private void refreshAdapter(ArrayList<ModelOrder> result)
    {
        ordersAdapter = new OrdersAdapter(getActivity(),result);
        lv.setAdapter(ordersAdapter);
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