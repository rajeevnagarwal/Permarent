package com.furniture.appliances.rentals.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.furniture.appliances.rentals.fragment.SubCategory;
import com.furniture.appliances.rentals.model.ModelCategory;

import java.util.ArrayList;

/**
 * Created by Infinia on 11-10-2015.
 */
public class SubCategoryAdapter extends FragmentStatePagerAdapter {

    int count = 1;
    FragmentManager fragmentManager;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> code = new ArrayList<String>();
    ModelCategory category;

    public SubCategoryAdapter(FragmentManager fm, ArrayList<String> titles, ArrayList<String> code, ModelCategory modelCategory) {
        super(fm);
        fragmentManager = fm;
        this.titles=titles;
        this.code = code;
        count = titles.size();
        this.category = modelCategory;
    }

    public Fragment getItem(int num) {
        SubCategory dummyFragment = new SubCategory();
        dummyFragment.setTitle(titles.get(num));
        dummyFragment.setCode(code.get(num));
        dummyFragment.setCategory(category);
        return dummyFragment;

    }

    @Override
    public int getCount() {
        return count;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
