package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

/**
 * Created by Rajeev Nagarwal on 12/14/2016.
 */

public class MyWallet extends Fragment {
    public static String TAG = "My Wallet";
    private TextView wallet_text;
    private AppPreferences apref = new AppPreferences();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        ((MainActivity) getActivity()).changeToolbar("My Wallet", false);
        initialize(v);
        fetch_wallet_amount();
        return v;
    }
    private void initialize(View v)
    {
        wallet_text = (TextView)v.findViewById(R.id.wallet_text);

    }
    private void fetch_wallet_amount()
    {
        if(apref.IsLoginedByFb(getActivity())||apref.IsLoginedByGoogle(getActivity())||apref.IsLoginedByEmail(getActivity()))
        {
            if(new CheckInternetConnection(getActivity()).isConnectedToInternet()) {


                fetch_amount(apref.readString(getActivity(), "email", ""));
            }
            else
            {
                new CheckInternetConnection(getActivity()).showDialog();
            }

        }
        else
        {
            Toast.makeText(getActivity(),"Please Login first",Toast.LENGTH_SHORT).show();
        }
    }
    private void fetch_amount(String email)
    {
        RequestParams params = new RequestParams();
        params.put("email",email);
        EndPonits.getWallet(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG,"Failure in Wallet");
                setDefaultView();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Hello");
                System.out.println(responseString);
                setView(parseJSON(responseString));

            }
        });
    }
    private void setDefaultView()
    {
        wallet_text.setText("Could not fetch wallet. Please check internet connection");
    }
    private String parseJSON(String response)
    {
        try {
            JSONArray array = new JSONArray(response);
            String result = String.valueOf(array.getJSONObject(0).getInt("walletCash"));
            return result;
        }catch(Exception e)
        {
            return "0";
        }
    }

    private void setView(String amount)
    {
        wallet_text.append(amount+" ₹");
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
