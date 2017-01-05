package com.furniture.appliances.rentals.fragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by Rajeev Nagarwal on 12/6/2016.
 */

public class ContactUs extends Fragment {

    public static final String TAG = "ContactUs";
    private Button submit;
    private EditText edit_firstname,edit_lastname,edit_mobile,edit_message,edit_mail;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        showBackButton();
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        ((MainActivity)getActivity()).changeToolbar("Send Feedback",false);
        initialize(v);
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                if(edit_firstname.getText().toString().length()>0&&edit_lastname.getText().toString().length()>0&&edit_mobile.getText().toString().length()>0&&edit_mail.getText().toString().length()>0&&edit_message.getText().toString().length()>0)
                {
                    int flag = 0;
                    if(!edit_mail.getText().toString().contains("@"))
                    {
                        Toast.makeText(getContext(),"Please enter email in valid format",Toast.LENGTH_LONG).show();
                        flag = 1;
                    }
                    if(!edit_mobile.getText().toString().matches("[0-9]+")&&edit_mobile.getText().toString().length()!=10)
                    {
                        Toast.makeText(getContext(),"Please enter mobile number in valid format",Toast.LENGTH_SHORT).show();
                        flag = 1;
                    }
                    if(flag==0)
                    {
                        if(new CheckInternetConnection(getContext()).isConnectedToInternet())
                        {
                            RequestParams param = new RequestParams();
                            param.add("firstName",edit_firstname.getText().toString());
                            param.add("lastName",edit_lastname.getText().toString());
                            param.add("email",edit_mail.getText().toString());
                            param.add("feedback",edit_message.getText().toString());
                            sendFeedback(param);

                        }
                        else
                        {
                            System.out.println("Not connected");
                            new CheckInternetConnection(getContext()).showDialog();
                        }

                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }

            }

        });
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
    private void sendFeedback(RequestParams params)
    {
        EndPonits.sendFeedback(params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("Failure in feedback");

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Toast.makeText(getContext(),"Thank you for you feedback",Toast.LENGTH_SHORT).show();

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

    private void initialize(View v)
    {
        submit = (Button)v.findViewById(R.id.submit);
        edit_firstname=(EditText) v.findViewById(R.id.edit_firstname);
        edit_lastname = (EditText)v.findViewById(R.id.edit_lastname);
        edit_mobile = (EditText)v.findViewById(R.id.edit_mobile);
        edit_message = (EditText)v.findViewById(R.id.edit_message);
        edit_mail = (EditText)v.findViewById(R.id.edit_mail);

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ab_cart).setVisible(true).setEnabled(true);
    }

}

