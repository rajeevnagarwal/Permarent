package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.furniture.appliances.rentals.fragment.PackageProduct;
import com.furniture.appliances.rentals.model.ModelSubCategory;

import java.util.ArrayList;

/**
 * Created by Infinia on 02-10-2015.
 */
public class PackageProductAdapter extends FragmentStatePagerAdapter {

    FragmentManager fragmentManager;
    ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<>();
    ModelSubCategory modelSubCategory = new ModelSubCategory();
    Context context;

    public PackageProductAdapter(FragmentManager fm,Context context,ArrayList<ModelSubCategory> modelSubCategoryArrayList,ModelSubCategory modelSubCategory) {
        super(fm);
        fragmentManager = fm;
        this.context = context;
        this.modelSubCategoryArrayList=modelSubCategoryArrayList;
        this.modelSubCategory=modelSubCategory;
    }

    public Fragment getItem(int num) {
        PackageProduct packageProduct = new PackageProduct();
        packageProduct.setModelSubCategory(modelSubCategoryArrayList.get(num),modelSubCategory,modelSubCategoryArrayList.size(),num);
        return packageProduct;

    }

    @Override
    public int getCount() {
        return modelSubCategoryArrayList.size();
    }


}
