package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.furniture.appliances.rentals.AddNewAddress;
import com.furniture.appliances.rentals.Login;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.BrowseCategoryAdapter;
import com.furniture.appliances.rentals.adapter.NewProductAdapter;
import com.furniture.appliances.rentals.adapter.PopularAdapter;
import com.furniture.appliances.rentals.adapter.PromotionAdapter;
import com.furniture.appliances.rentals.adapter.RentAdapter;
import com.furniture.appliances.rentals.adapter.SearchAdapter;
import com.furniture.appliances.rentals.adapter.SuggestionAdapter;
import com.furniture.appliances.rentals.adapter.WishListAdapter;
import com.furniture.appliances.rentals.model.Cat;
import com.furniture.appliances.rentals.model.ModelProduct;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/8/2016.
 */

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private RecyclerView image_gallery;
    private ArrayList<String> glidelist;
    private ArrayList<String> ownlist;
    private ArrayList<ModelProduct> newlist;
    private PromotionAdapter promoadapter;
    private RecyclerView product_recyclerview;
    private String currentCity="";
    private AppPreferences apref = new AppPreferences();
    private ListView list;
    private SearchView searchView;
    private SuggestionAdapter searchAdapter;
    private ArrayList<String> data;
    private LinearLayout view;
    private ArrayList<ModelProduct> product;
    private RecyclerView lv_browse;
    private ArrayList<Cat> categories;
    PopularAdapter adapter;
    private RecyclerView new_recycle;
    public static boolean isFragmentOpened;
    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
        currentCity = apref.readString(getActivity(), "city", "Gurgaon");
        ((MainActivity) getActivity()).changeToolbar(currentCity, true);
        initialize(v);
        fetchNames();
        fetchpopular();
        fetchCategories();
        fetchnew();
        //populatelist();
        fetchsuggestions();
        setView(product_recyclerview,product);

        return v;
    }
    private void setnewview()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        new_recycle.setLayoutManager(layoutManager);
        NewProductAdapter adapter = new NewProductAdapter(getActivity(),newlist);
        new_recycle.setAdapter(adapter);
    }

    private void fetchnew()
    {
        EndPonits.getPopularProducts(null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure in new products");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Success"+responseString);
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
                        newlist.add(new ModelProduct(obj.getString("productId"),obj.getString("productName"),obj.getString("minRentalDuration"),String.valueOf(obj.getInt("newRating")),available,String.valueOf(obj.getInt("securityAmount")),String.valueOf(obj.getInt("retailPrice")),String.valueOf(obj.getInt("productUserLikesCount")),""));

                    }
                    setnewview();
                }
                catch(Exception e)
                {

                }

            }
        });
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
                try {
                    JSONArray array = new JSONArray(responseString);
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        categories.add(new Cat(obj.getString("categoryId"),obj.getString("categoryName")));
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        lv_browse.setLayoutManager(layoutManager);
        BrowseCategoryAdapter categoryAdapter= new BrowseCategoryAdapter(getActivity(),categories);
        lv_browse.setAdapter(categoryAdapter);
        lv_browse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    private void setView(RecyclerView view,ArrayList<ModelProduct> list)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        view.setLayoutManager(layoutManager);
        adapter = new PopularAdapter(getActivity(),list);
        view.setAdapter(adapter);
    }
    private void fetchsuggestions()
    {
        searchAdapter = new SuggestionAdapter(getActivity(),data);
        list.setAdapter(searchAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                {
                    view.setVisibility(View.GONE);
                }
                else {
                    view.setVisibility(View.VISIBLE);
                }
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void populatelist()
    {
        data.add("Rajeev");
        data.add("Rajeev Nagarwal");
        data.add("Harshit");
        data.add("Himaneesh");
        data.add("Kunal");
    }
    private void fetchNames()
    {
        EndPonits.getProductNames(new RequestParams(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                data = parseJSON(responseString,data);

            }
        });
    }
    private ArrayList<String> parseJSON(String string,ArrayList<String> data)
    {
        try {
            JSONArray array = new JSONArray(string);
            for(int i=0;i<array.length();i++)
            {
                data.add(array.getJSONObject(i).getString("productName"));
            }
        }
        catch(Exception e)
        {

        }

        return data;
    }
    private void fetchpopular()
    {
        EndPonits.getPopularProducts(new RequestParams(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure in popular");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(!responseString.equals("Product Id Invalid")) {
                    product = parseResponse(responseString, product);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }




    private void initialize(View v)
    {
        newlist = new ArrayList<ModelProduct>();
        new_recycle = (RecyclerView)v.findViewById(R.id.new_recycle);
        categories = new ArrayList<Cat>();
        lv_browse = (RecyclerView) v.findViewById(R.id.lv_browse);
        product = new ArrayList<ModelProduct>();
        data = new ArrayList<String>();
        view = (LinearLayout)v.findViewById(R.id.suggestion_layout);
        image_gallery = (RecyclerView)v.findViewById(R.id.image_gallery);
        list = (ListView)v.findViewById(R.id.suggestions);
        searchView = (SearchView)v.findViewById(R.id.simpleSearchView);
        product_recyclerview = (RecyclerView)v.findViewById(R.id.product_recyclerview);
        glidelist = new ArrayList<String>();
        glidelist.add("CONTROL. NAVIGATE. BE ORGANIZED");
        glidelist.add("Enjoy the Wide Possibilities with Venedor");
        ownlist=new ArrayList<String>();
        ownlist.add("BedRoom");
        ownlist.add("Office");
        ownlist.add("Kitchen");
        ownlist.add("Hall");
        promoadapter = new PromotionAdapter(glidelist,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        image_gallery.setLayoutManager(layoutManager);
        image_gallery.setAdapter(promoadapter);
        product_recyclerview.setAdapter(new PopularAdapter(getActivity(),product));



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
    private ArrayList<ModelProduct> parseResponse(String response, ArrayList<ModelProduct> list)
    {
        try {
            JSONArray array = new JSONArray(response);
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
                list.add(new ModelProduct(obj.getString("productId"),obj.getString("productName"),obj.getString("minRentalDuration"),String.valueOf(obj.getInt("newRating")),available,String.valueOf(obj.getInt("securityAmount")),String.valueOf(obj.getInt("retailPrice")),String.valueOf(obj.getInt("productUserLikesCount")),""));

            }
        }
        catch(Exception e)
        {

        }
        return list;

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ab_cart).setVisible(true).setEnabled(true);
    }

}
