package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.furniture.appliances.rentals.Category;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.CategoryAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
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
    private RecyclerView furniture_recyclerview;
    private RecyclerView electronic_recyclerview;
    private RecyclerView package_recyclerview;
    private ArrayList<Subcategory> furniture;
    private ArrayList<Subcategory> electronic;
    private ArrayList<Subcategory> packages;
    private TextView furniture_text,electronics_text,packages_text;
    public static boolean isFragmentOpened;
    ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        ((MainActivity)getActivity()).changeToolbar("Categories",false);
        initialize(v);
       fetch_categories();
        //getDataFromDb();

        return v;
    }
    private void setView(ArrayList<Subcategory> list,RecyclerView view)
    {
        view.setLayoutManager(new GridLayoutManager(getActivity(),3));
        view.setAdapter(new CategoryAdapter(list,getActivity()));
        view.addItemDecoration(new DividerItemDecoration(getActivity().getResources().getDrawable(R.drawable.line_divider),getActivity().getResources().getDrawable(R.drawable.line_divider),3));

    }
    private void getDataFromDb() {
        DBInteraction dbInteraction = new DBInteraction(getActivity());
        modelCategoryArrayList = dbInteraction.getAllCategories();
        for(int i=0;i<modelCategoryArrayList.size();i++)
        {
            System.out.println(modelCategoryArrayList.get(i).heading_name);
        }
        dbInteraction.close();
    }
    private void fetch_categories()
    {
        EndPonits.getCategories(new RequestParams(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONArray array = new JSONArray(responseString);
                    modelCategoryArrayList = new ParseApi().parseCategoryList(getActivity(),array);
                    for(int i=0;i<modelCategoryArrayList.size();i++)
                    {
                        System.out.println(modelCategoryArrayList.get(i).categoryName);
                        populatelists(modelCategoryArrayList.get(i).categoryId,i);
                        break;
                    }

                }
                catch(Exception e)
                {

                }


            }
        });
    }



    private void populatelists(String id,int i)
    {
        if(new CheckInternetConnection(getActivity()).isConnectedToInternet())
        {
            fetch_furnitures(id,i);
            fetch_electronics(id,i);
            fetch_packages(id,i);

        }
        else{
            new CheckInternetConnection(getActivity()).showDialog();
        }
    }
    private void fetch_furnitures(String category,final int i)
    {
        RequestParams params = new RequestParams();
        params.put("categoryId",category);
        EndPonits.listSubCategories(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure");
                System.out.println(responseString);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(responseString);
                parseJSON(responseString,furniture,i);
                setView(furniture,furniture_recyclerview);

            }
        });

    }
    private void fetch_electronics(String category,final int i)
    {

        RequestParams params = new RequestParams();
        params.put("categoryId",category);
        EndPonits.listSubCategories(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(responseString);
                parseJSON(responseString,electronic,i);
                setView(electronic,electronic_recyclerview);

            }
        });
    }
    private void fetch_packages(String category,final int i)
    {
        RequestParams params = new RequestParams();
        params.put("categoryId",category);
        EndPonits.listSubCategories(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(responseString);
                parseJSON(responseString,packages,i);
                setView(packages,package_recyclerview);

            }
        });

    }
    private void parseJSON(String result,ArrayList<Subcategory> list,int i)
    {
        try {
            JSONArray array = new JSONArray(result);
            for (int j = 0; j < array.length(); j++) {
                JSONObject obj = array.getJSONObject(j);
                list.add(new Subcategory(obj.getString("subCategoryName")));
                modelCategoryArrayList.get(i).subcategory = modelCategoryArrayList.get(i).subcategory + obj.getString("subCategoryName")+",";
                modelCategoryArrayList.get(i).subcategoryid = modelCategoryArrayList.get(i).subcategoryid + obj.getString("subCategoryId")+",";


            }
        }
        catch(Exception e)
        {

        }
    }




    private void initialize(View v)
    {
        furniture_text = (TextView)v.findViewById(R.id.furniture);
        electronics_text = (TextView)v.findViewById(R.id.Electronics);
        packages_text = (TextView)v.findViewById(R.id.packages_text);
        furniture_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Category.class);
                i.putExtra("modelCategory", modelCategoryArrayList.get(0));
                i.putExtra("defaultFragment",0);
                startActivity(i);

            }
        });
        electronics_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Category.class);
                i.putExtra("modelCategory", modelCategoryArrayList.get(1));
                i.putExtra("defaultFragment",0);
                startActivity(i);

            }
        });
        packages_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Category.class);
                i.putExtra("modelCategory", modelCategoryArrayList.get(2));
                i.putExtra("defaultFragment",0);
                startActivity(i);

            }
        });
        furniture_recyclerview = (RecyclerView)v.findViewById(R.id.furniture_recyclerview);
        electronic_recyclerview = (RecyclerView)v.findViewById(R.id.electronic_recyclerview);
        package_recyclerview = (RecyclerView)v.findViewById(R.id.package_recyclerview);
        furniture = new ArrayList<>();
        electronic = new ArrayList<>();
        packages = new ArrayList<>();
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

}
