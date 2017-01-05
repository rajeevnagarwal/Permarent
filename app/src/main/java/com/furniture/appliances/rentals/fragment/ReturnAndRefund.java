package com.furniture.appliances.rentals.fragment;

/**
 * Created by Rajeev Nagarwal on 12/5/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;

public class ReturnAndRefund extends Fragment {

public static final String TAG = "ReturnAndRefund";
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        showBackButton();
        View v = inflater.inflate(R.layout.fragment_return, container, false);
        ((MainActivity)getActivity()).changeToolbar("Return and Refund Policy",false);
        return v;
        }

@Override
public void onAttach(Activity activity) {
        super.onAttach(activity);
        }
        public void showBackButton()
        {
                if (getActivity() instanceof AppCompatActivity) {
                        System.out.println("YES");
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch(item.getItemId())
                {
                        case android.R.id.home:
                                ((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStack();
                                return true;
                }
                return super.onOptionsItemSelected(item);
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



