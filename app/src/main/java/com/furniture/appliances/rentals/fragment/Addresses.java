package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.furniture.appliances.rentals.AddNewAddress;
import com.furniture.appliances.rentals.Login;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.AddressAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Infinia on 22-09-2015.
 */
public class Addresses extends Fragment {

    public static final String TAG = "Addresses";
    RelativeLayout rl1,rl2,rl3;
    Button login;
    ListView lv;
    FloatingActionButton add;
    AppPreferences apref = new AppPreferences();
    AddressAdapter addressAdapter;
    ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<ModelAddress>();


    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_addresses, container, false);
        ((MainActivity)getActivity()).changeToolbar("Addresses",false);
        initView(v);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddNewAddress.class);
                startActivity(i);
            }
        });
        return v;
    }

    private void refreshAdapter(ArrayList<ModelAddress> result)
    {
        addressAdapter = new AddressAdapter(getActivity(),result);
        lv.setAdapter(addressAdapter);
    }

    private void getDataFromDb()
    {
        DBInteraction dbInteraction = new DBInteraction(getActivity());
        modelAddressArrayList = dbInteraction.getAllAddress();
        dbInteraction.close();
        refreshAdapter(modelAddressArrayList);
    }

    private void initView(View v)
    {
        rl1 = (RelativeLayout)v.findViewById(R.id.rl1);
        rl2 = (RelativeLayout)v.findViewById(R.id.rl2);
        rl3 = (RelativeLayout)v.findViewById(R.id.rl3);
        lv = (ListView)v.findViewById(R.id.lv);
        login = (Button )v.findViewById(R.id.login);
        add = (FloatingActionButton) v.findViewById(R.id.add);
        if(apref.IsLoginedByGoogle(getActivity()) || apref.IsLoginedByEmail(getActivity()) || apref.IsLoginedByFb(getActivity()))
        {
            rl1.setVisibility(View.GONE);
            rl2.setVisibility(View.VISIBLE);
            getDataFromDb();
            if(modelAddressArrayList.size()!=0)
            {
                rl3.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
            }
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
}
