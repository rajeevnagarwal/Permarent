package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.furniture.appliances.rentals.AddNewAddress;
import com.furniture.appliances.rentals.Login;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.AddressAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;
import com.github.clans.fab.FloatingActionButton;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Infinia on 22-09-2015.
 */
public class Addresses extends Fragment {

    public static final String TAG = "Addresses";
    RelativeLayout rl1,rl2,rl3;
    Button login;
    ListView lv;
    FloatingActionButton add;
    AppPreferences apref = new AppPreferences();
    AddressAdapter addressAdapter;
    private ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<ModelAddress>();


    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_addresses, container, false);
        ((MainActivity)getActivity()).changeToolbar("Addresses",false);
        initView(v);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddNewAddress.class);
                startActivity(i);
            }
        });
        return v;
    }

    private void refreshAdapter(ArrayList<ModelAddress> result)
    {
        System.out.println("Size"+result.size());
        addressAdapter = new AddressAdapter(getActivity(),result);
        lv.setAdapter(addressAdapter);
    }

    private void getDataFromDb()
    {
       /* DBInteraction dbInteraction = new DBInteraction(getActivity());
        //modelAddressArrayList = dbInteraction.getAllAddress();
        dbInteraction.close();*/
        //modelAddressArrayList = Config.Addresses;
        refreshAdapter(modelAddressArrayList);
    }

    private void initView(View v)
    {
        rl1 = (RelativeLayout)v.findViewById(R.id.rl1);
        rl2 = (RelativeLayout)v.findViewById(R.id.rl2);
        rl3 = (RelativeLayout)v.findViewById(R.id.rl3);
        lv = (ListView)v.findViewById(R.id.lv);
        login = (Button )v.findViewById(R.id.login);
        add = (FloatingActionButton) v.findViewById(R.id.add);
        if(apref.IsLoginedByFb(getActivity()))
        {
            rl1.setVisibility(View.GONE);
            rl2.setVisibility(View.VISIBLE);
            getDataFromDb();
            System.out.println("ListSize"+modelAddressArrayList.size());
            fetchAddresses(apref.readString(getActivity(),"email",null));


        }

    }
    private void fetchAddresses(String mail)
    {
        if(mail!=null)
        {
            RequestParams params = new RequestParams();
            params.put("email",mail);
            EndPonits.getUserInfo(params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("Failure in Addresses");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    System.out.println("Success"+responseString);
                    try {
                        JSONArray array = new JSONArray(responseString);
                        for(int i=0;i<array.length();i++)
                        {

                            JSONObject obj = array.getJSONObject(i);
                            if(obj.has("shippingAddresses"))
                            {
                                System.out.println("Found");

                                JSONArray add = obj.getJSONArray("shippingAddresses");
                                for(int j=0;j<add.length();j++)
                                {
                                    ModelAddress model = new ModelAddress();
                                    JSONObject obj_add = add.getJSONObject(j);
                                    System.out.println(obj_add.getString("location"));
                                    model.location = obj_add.getString("location");
                                    model.city = obj_add.getString("city");
                                    model.state = obj_add.getString("state");
                                    model.houseNo = obj_add.getString("houseNo");
                                    model.localityName = obj_add.getString("localityName");
                                    model.pincode = obj_add.getString("pincode");
                                    model.others = obj_add.getString("others");
                                    modelAddressArrayList.add(model);

                                }


                            }
                        }
                        refreshAdapter(modelAddressArrayList);
                        if(modelAddressArrayList.size()!=0)
                        {
                            rl3.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                        }


                    }
                    catch(Exception e)
                    {

                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
