package com.furniture.appliances.rentals.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.util.Config;

import static android.R.id.message;
import static android.content.Intent.createChooser;

/**
 * Created by Rajeev Nagarwal on 12/12/2016.
 */

public class Contact extends Fragment {
    public static final String TAG = "ContactUs";
    private Button mCall,mMail;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        showBackButton();
        View v = inflater.inflate(R.layout.fragment_callmail, container, false);
        ((MainActivity) getActivity()).changeToolbar("ContactUs", false);
        initialize(v);
        return v;
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
    private void initialize(View v)
    {
        mCall = (Button)v.findViewById(R.id.call);
        mMail = (Button)v.findViewById(R.id.mail);
        mCall.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent dialer = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",Config.contactNo,null));
                startActivity(createChooser(dialer,"Select Dialing Software..."));


            }

        });
        mMail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "support@permarent.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Permarent");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi");
                startActivity(createChooser(emailIntent, "Send email..."));
            }

        });

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ab_cart).setVisible(true).setEnabled(true);
    }




}
