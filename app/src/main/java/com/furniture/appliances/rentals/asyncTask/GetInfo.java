package com.furniture.appliances.rentals.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.furniture.appliances.rentals.AddNewAddress;
import com.furniture.appliances.rentals.restApi.RestIntraction;
import com.furniture.appliances.rentals.util.Config;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Infinia on 25-07-2015.
 */
public class GetInfo extends AsyncTask<Void, Void, Void> {
    Context context;
    ProgressDialog progressDialog;
    private RestIntraction restIntraction;
    private String response, placeId;
    boolean isOperationSuccessfull;
    RelativeLayout details;
    TextView name,address,initials;
    private ImageView image;
    String area,city,pincode,street;

    public GetInfo(Context context, String placeId) {
        this.context = context;
        this.placeId = placeId;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting data...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            String url = Config.getPlaceDetails + "placeid=" + placeId + "&key=" + Config.KEY;
            restIntraction = new RestIntraction(url);
            restIntraction.toString();
            restIntraction.Execute(0);
            response = restIntraction.getResponse();


            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject != null) {
                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("ok")) {
                        if (jsonObject.has("result")) {
                            JSONObject result = jsonObject.getJSONObject("result");
                            JSONArray address_components = result.getJSONArray("address_components");
                            for(int i=0;i<address_components.length();i++)
                            {
                                JSONObject child = address_components.getJSONObject(i);
                                String temp="";
                                JSONArray types = child.getJSONArray("types");
                                for(int j=0;j<types.length();j++)
                                    temp += types.getString(j);
                                if(temp.contains("postal_code"))
                                    pincode = child.getString("long_name");
                                if(temp.contains("locality"))
                                    city = child.getString("long_name");
                                if(temp.contains("sublocality_level_1"))
                                    area = child.getString("long_name");
                                if(temp.contains("sublocality_level_2"))
                                    street = child.getString("long_name");
                                isOperationSuccessfull = true;

                            }
                        }

                    } else
                        isOperationSuccessfull = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void Void) {
        super.onPostExecute(Void);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        if (isOperationSuccessfull) {

            ((AddNewAddress)context).setData(area,city,pincode,street);
        } else {

            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
        }

    }
}