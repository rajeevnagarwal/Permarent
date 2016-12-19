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
 * Created by Infinia on 07-11-2015.
 */
public class TermsAndConditions extends Fragment {

    public static final String TAG = "TermsAndConditions";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        ((MainActivity)getActivity()).changeToolbar("T&C",false);
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
