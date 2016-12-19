package com.furniture.appliances.rentals.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.ItemAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.parser.ParseApi;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Infinia on 11-10-2015.
 */
public class SubCategory extends Fragment {

    public static final String TAG = "SubCategory";
    ListView lv;
    ItemAdapter itemAdapter;
    ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<>();
    public static boolean isFragmentOpened;
    private String title;
    private String code;
    public void setTitle(String title){

        this.title=title;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment, container, false);
        initView(v);
        getDataFromDb(title);
        if(!title.equals("")) {
            RocqAnalytics.initialize(getActivity());
            RocqAnalytics.trackScreen(title, new ActionProperties());
        }

        return v;
    }
    private void initView(View v)
    {
        lv = (ListView)v.findViewById(R.id.lv);
    }

    public void getDataFromDb(String category) {

        DBInteraction dbInteraction = new DBInteraction(getActivity());
        modelSubCategoryArrayList = dbInteraction.getSubCategories(category);
        dbInteraction.close();
        addCarsToList(modelSubCategoryArrayList);
    }
    public void addCarsToList(ArrayList<ModelSubCategory> result) {
        itemAdapter = new ItemAdapter(getActivity(), result);
        lv.setAdapter(itemAdapter);


    }
   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentOpened = true;
        Config.OPENED_FRAGMENT = this;
        Config.CURRENT_ACTIVITY_CONTEXT=getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentOpened = false;
        Config.OPENED_FRAGMENT = null;
    }

}
