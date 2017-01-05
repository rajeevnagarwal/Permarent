package com.furniture.appliances.rentals.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.ItemAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCategory;
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
    private ModelCategory category;
    public void setTitle(String title){

        this.title=title;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public void setCategory(ModelCategory category)
    {
        this.category = category;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment, container, false);
        showBackButton();
        System.out.println("SubCategory"+code+title);
        ((MainActivity)getActivity()).changeToolbar("Products",false);
        initView(v);
        //getDataFromDb(title);
        fetchproductdata(code);
        if(!title.equals("")) {
            RocqAnalytics.initialize(getActivity());
            RocqAnalytics.trackScreen(title, new ActionProperties());
        }

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

    private void initView(View v)
    {
        lv = (ListView)v.findViewById(R.id.lv);
    }
    private void fetchproductdata(String code)
    {
        RequestParams params = new RequestParams();
        params.put("subCategoryId",code);
        EndPonits.listProducts(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure in product");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Success"+responseString);
                if (!responseString.equals("\"Sub Category Id Invalid\"")) {

                    try {
                        JSONArray array = new JSONArray(responseString);
                        modelSubCategoryArrayList = new ParseApi().parseProductList(getActivity(), array);
                        for (int i = 0; i < modelSubCategoryArrayList.size(); i++) {
                           // System.out.println("Product " + modelSubCategoryArrayList.get(i).big_img);
                        }
                        addCarsToList(modelSubCategoryArrayList);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    public void getDataFromDb(String category) {

        DBInteraction dbInteraction = new DBInteraction(getActivity());
        //modelSubCategoryArrayList = dbInteraction.getSubCategories(category);
        dbInteraction.close();
        addCarsToList(modelSubCategoryArrayList);
    }
    public void addCarsToList(ArrayList<ModelSubCategory> result) {
        itemAdapter = new ItemAdapter(getActivity(), result,category);
        lv.setAdapter(itemAdapter);


    }
   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ab_cart).setVisible(true).setEnabled(true);
    }

}
