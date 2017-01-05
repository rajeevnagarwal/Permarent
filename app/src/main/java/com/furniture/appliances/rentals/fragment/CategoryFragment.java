package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.furniture.appliances.rentals.Category;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.CategoryAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.Cat;
import com.furniture.appliances.rentals.model.ModelCategory;
import com.furniture.appliances.rentals.model.Subcategory;
import com.furniture.appliances.rentals.parser.ParseApi;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.ui.DividerItemDecoration;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class CategoryFragment extends Fragment {
    public static String TAG = "CategoryFragment";
    RecyclerView recycle_category;
    ArrayList<Cat> categories;
    ScrollView catscroll;
    public static boolean isFragmentOpened;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        ((MainActivity)getActivity()).changeToolbar("Categories",false);
        initialize(v);
        fetchCategories();



        return v;
    }
    private void initialize(View v)
    {
        catscroll = (ScrollView)v.findViewById(R.id.catscroll);
        categories = new ArrayList<>();
        recycle_category = (RecyclerView)v.findViewById(R.id.recycle_categories);
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
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        categories.add(new Cat(obj.getString("categoryId"),obj.getString("categoryName")));
                        System.out.println(categories.get(i).name);
                    }
                    setCategoriesView();

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
    private void setCategoriesView()
    {
        CategoryAdapter adapter = new CategoryAdapter(categories,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recycle_category.setLayoutManager(layoutManager);
        recycle_category.setAdapter(adapter);


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
