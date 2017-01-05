package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.CategoryAdapter;
import com.furniture.appliances.rentals.adapter.RPAdapter;
import com.furniture.appliances.rentals.model.Cat;
import com.furniture.appliances.rentals.model.ModelProduct;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.Config;
import com.google.android.gms.analytics.ecommerce.Product;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/8/2016.
 */

public class OurServices extends Fragment {
    public static String TAG = "OurServices";
    public RecyclerView rent_recycle;
    ArrayList<Cat> categories;
    ArrayList<ModelProduct> list;
    public RecyclerView lv_prent;
    public static boolean isFragmentOpened;
    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_services, container, false);
        ((MainActivity)getActivity()).changeToolbar("Rent to own",false);
        initialize(v);
        fetchCategories();
        fetchpopular();
        return v;
    }
    private void fetchpopular()
    {
        EndPonits.getPopularProducts(null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Product"+responseString);
                try {
                    JSONArray array = new JSONArray(responseString);
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        String available="";
                        if(obj.getInt("productAvailability")==1)
                        {
                            available = "Available";
                        }
                        else
                        {
                            available = "Not Available";
                        }
                        String dim = obj.getJSONArray("productDesc").getJSONObject(0).getString("dimensions");
                        list.add(new ModelProduct(obj.getString("productId"),obj.getString("productName"),obj.getString("minRentalDuration"),String.valueOf(obj.getInt("newRating")),available,String.valueOf(obj.getInt("securityAmount")),String.valueOf(obj.getInt("retailPrice")),String.valueOf(obj.getInt("productUserLikesCount")),dim));

                    }
                    setProducts();
                }
                catch(Exception e)
                {

                }

            }
        });
    }
    private void setProducts()
    {
        RPAdapter adapter = new RPAdapter(getActivity(),list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        lv_prent.setLayoutManager(layoutManager);
        lv_prent.setAdapter(adapter);
    }


    private void fetchCategories()
    {
        EndPonits.getCategories(null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure in Categories");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Success");
                    try {
                        JSONArray array = new JSONArray(responseString);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            if(obj.getString("categoryName").equals("Living Room")||obj.getString("categoryName").equals("Appliances"))
                            {
                                categories.add(new Cat(obj.getString("categoryId"), obj.getString("categoryName")));

                            }
                        }
                        setCategoriesView();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        });
    }
    private void initialize(View v)
    {
        rent_recycle = (RecyclerView)v.findViewById(R.id.rent_recycle);
        categories = new ArrayList<Cat>();
        lv_prent = (RecyclerView) v.findViewById(R.id.lv_prent);
        list = new ArrayList<ModelProduct>();
    }
    private void setCategoriesView()
    {
        CategoryAdapter adapter = new CategoryAdapter(categories,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rent_recycle.setLayoutManager(layoutManager);
        rent_recycle.setAdapter(adapter);

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
    public void onResume() {
        super.onResume();
        isFragmentOpened = true;
        Config.OPENED_FRAGMENT = this;
        Config.CURRENT_ACTIVITY_CONTEXT = getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentOpened = false;
        Config.OPENED_FRAGMENT = null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ab_cart).setVisible(true).setEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.ic_logo);
    }

}
