package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;

/**
 * Created by Rajeev Nagarwal on 12/8/2016.
 */

public class OurServices extends Fragment {
    public static String TAG = "OurServices";
    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_services, container, false);
        ((MainActivity)getActivity()).changeToolbar("Our Services",false);
        return v;
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
