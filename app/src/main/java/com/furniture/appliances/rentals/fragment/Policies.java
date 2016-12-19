package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.PolicyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajeev Nagarwal on 12/14/2016.
 */

public class Policies extends Fragment {
    public static String TAG = "Policies";
    private ListView listView;
    private ArrayList<String> policies;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_policy, container, false);
        ((MainActivity) getActivity()).changeToolbar("Our Policies", false);
        initialize(v);
        return v;
    }
    private void initialize(View v)
    {
        listView = (ListView)v.findViewById(R.id.lv_policy);
        policies = new ArrayList<String>();
        policies.add("T&C");
        policies.add("Privacy Policy");
        policies.add("Return and Refund Policy");
        PolicyAdapter adapter = new PolicyAdapter(getActivity(),policies);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;
                if(position==0)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TermsAndConditions.TAG);
                    if(fragment==null)
                    {
                        fragment = new TermsAndConditions();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,TermsAndConditions.TAG).addToBackStack(TermsAndConditions.TAG).commit();


                }
                else if(position==1)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(PrivacyPolicy.TAG);
                    if(fragment==null)
                    {
                        fragment = new PrivacyPolicy();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,PrivacyPolicy.TAG).addToBackStack(PrivacyPolicy.TAG).commit();

                }
                else if(position==2)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ReturnAndRefund.TAG);
                    if(fragment==null)
                    {
                        fragment = new ReturnAndRefund();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,ReturnAndRefund.TAG).addToBackStack(ReturnAndRefund.TAG).commit();


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
}
