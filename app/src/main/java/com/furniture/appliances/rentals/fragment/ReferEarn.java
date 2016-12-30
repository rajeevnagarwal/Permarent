package com.furniture.appliances.rentals.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.furniture.appliances.rentals.Login;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Rajeev Nagarwal on 12/12/2016.
 */

public class ReferEarn extends Fragment {
    public static final String TAG = "Refer&Earn";
    private Button invite_whatsapp;
    private Button invite_facebook;
    private TextView code;
    private CallbackManager callbackManager;
    ShareDialog shareDialog;
    AppPreferences apref = new AppPreferences();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v=null;
        ((MainActivity) getActivity()).changeToolbar("Refer & Earn", false);


        if (apref.IsLoginedByFb(getActivity()) || apref.IsLoginedByGoogle(getActivity()) || apref.IsLoginedByEmail(getActivity())) {
            if (apref.readString(getActivity(), "email", "") != null) {
                if (new CheckInternetConnection(getActivity()).isConnectedToInternet()) {
                    v = inflater.inflate(R.layout.fragment_refer, container, false);
                    initialize(v);
                    fetch_code(apref.readString(getActivity(), "email", ""));
                } else {
                    new CheckInternetConnection(getActivity()).showDialog();
                }
            } else {
                Toast.makeText(getActivity(), "Couldn't fetch your code", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please login first", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void fetch_code(String mail) {
        RequestParams params = new RequestParams();
        params.put("email", mail);
        EndPonits.getUserInfo(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure");
                System.out.println(responseString);
                setdefaultView();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Success"+responseString);
                try {
                    JSONArray array = new JSONArray(responseString);
                    JSONObject object = array.getJSONObject(array.length()-1);
                    if(object.has("userReferralCode"))
                    {
                        System.out.println("Refer"+object.get("userReferralCode"));
                        if(!object.getString("userReferralCode").equals("null")) {
                            System.out.println("OK");
                            setView(object.getString("userReferralCode"));
                        }
                        else
                        {
                            setdefaultView();
                        }

                    }
                    else {
                        System.out.println("Not Refer");
                        setdefaultView();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();

                }


            }
        });
    }

    private void setdefaultView() {
        //invite.setClickable(false);
        code.setText("RajeevPo45");
    }

    private void setView(String referral_code) {
        code.setText(referral_code);
        invite_whatsapp.setClickable(true);
        invite_facebook.setClickable(true);


    }
    private void facebookInitialize()
    {
        FacebookSdk.sdkInitialize(getActivity());
        shareDialog = new ShareDialog(getActivity());
        callbackManager = CallbackManager.Factory.create();

    }


    private void initialize(View v) {

        facebookInitialize();
        invite_whatsapp = (Button) v.findViewById(R.id.invite_whatsapp);
        invite_facebook = (Button) v.findViewById(R.id.invite_facebook);
        code = (TextView) v.findViewById(R.id.referral_code);
        invite_whatsapp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PackageManager pm = getActivity().getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = Config.browserUrl + "&referrer=" + code.getText().toString();

                   PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch(Exception e)
                {

                }
            }

        });
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();

            }
        });
        invite_facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Hi,Guys! Please use below link to download app")
                            .setContentDescription(Config.browserUrl+"&referrer="+code.getText())
                            .setContentUrl(Uri.parse("www.permarent.com"))
                            .build();

                    shareDialog.show(linkContent);


            }



    });


}
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ab_cart).setVisible(true).setEnabled(true);
    }
}
