package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.Category;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.GridAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelCategory;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;

import java.util.ArrayList;

/**
 * Created by Infinia on 22-09-2015.
 */
public class Home extends Fragment {

    ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
    public static final String TAG = "Home";
    TextView price, quantity;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    GridAdapter gridAdapter;
    String currentCity;
    RelativeLayout actual, dummy;
    public static boolean isFragmentOpened;
    AppPreferences apref = new AppPreferences();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        currentCity = apref.readString(getActivity(), "city", "Gurgaon");
        ((MainActivity) getActivity()).changeToolbar(currentCity, true);
        initView(v);
        getDataFromDb();
        setView();
        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    RocqAnalytics.initialize(getActivity());
                    RocqAnalytics.trackScreen(modelCategoryArrayList.get(recyclerView.getChildPosition(child)).heading_name, new ActionProperties());
                    Intent i = new Intent(getActivity(), Category.class);
                    i.putExtra("modelCategory", modelCategoryArrayList.get(recyclerView.getChildPosition(child)));
                    i.putExtra("defaultFragment",0);
                    startActivity(i);

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        return v;
    }

    private void getDataFromDb() {
        DBInteraction dbInteraction = new DBInteraction(getActivity());
        modelCategoryArrayList = dbInteraction.getAllCategories();
        dbInteraction.close();
    }



    private void initView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        actual = (RelativeLayout) v.findViewById(R.id.actual);
        dummy = (RelativeLayout) v.findViewById(R.id.dummy);
        if (!currentCity.equals("Gurgaon")) {
            actual.setVisibility(View.GONE);
            dummy.setVisibility(View.VISIBLE);
        } else {
            dummy.setVisibility(View.GONE);
            actual.setVisibility(View.VISIBLE);
        }
    }

    private void setView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        gridAdapter = new GridAdapter(getActivity(), modelCategoryArrayList);
        mRecyclerView.setAdapter(gridAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
