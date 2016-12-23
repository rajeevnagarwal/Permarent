package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
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
import com.furniture.appliances.rentals.util.AppPreferences;

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
            getDataFromDb();
            if(modelOrderArrayList.size()!=0)
            {
                rl3.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
            }
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
        modelOrderArrayList = dbInteraction.getAllOrders();
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