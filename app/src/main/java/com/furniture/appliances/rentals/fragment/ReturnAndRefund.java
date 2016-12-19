package com.furniture.appliances.rentals.fragment;

/**
 * Created by Rajeev Nagarwal on 12/5/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;

public class ReturnAndRefund extends Fragment {

public static final String TAG = "ReturnAndRefund";
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_return, container, false);
        ((MainActivity)getActivity()).changeToolbar("Return and Refund Policy",false);
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



