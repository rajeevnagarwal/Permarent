package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.Category;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.fragment.SubCategory;
import com.furniture.appliances.rentals.model.Cat;
import com.furniture.appliances.rentals.model.Subcategory;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.ui.DividerItemDecoration;
import com.furniture.appliances.rentals.ui.RoundedBitmapView;
import com.furniture.appliances.rentals.util.SubCategoryUtil;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rajeev Nagarwal on 12/9/2016.
 */

public class  CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Cat> list;
    public CategoryAdapter(ArrayList<Cat> list, Context ctx)
    {
        this.list = list;
        this.context =ctx;

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView category_text;
        public RecyclerView sub_recycle;
        public MyViewHolder(View v)
        {
            super(v);
            category_text= (TextView)v.findViewById(R.id.own_text);
            sub_recycle = (RecyclerView)v.findViewById(R.id.recycle_sub);

        }

    }
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.rent_item,parent,false);
        CategoryAdapter.MyViewHolder vh = new CategoryAdapter.MyViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position)
    {
        holder.category_text.setText(list.get(position).name);

        synchronized (this) {
            fetchSubCategory(holder, list.get(position).id,position);
        }

    }
    private void fetchSubCategory(final CategoryAdapter.MyViewHolder holder,String id,final int pos)
    {
        RequestParams params = new RequestParams();
        final ArrayList<Subcategory> data = new ArrayList<>();
        params.put("categoryId",id);
        EndPonits.listSubCategories(params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Failure");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("Success");
                System.out.println(responseString);
                synchronized (this)
                {
                    try {
                        JSONArray array = new JSONArray(responseString);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            System.out.println("SUB" + obj.getString("subCategoryName"));
                            data.add(new Subcategory(obj.getString("subCategoryName"),obj.getString("subCategoryId")));
                        }
                        setView(holder,data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void setView(CategoryAdapter.MyViewHolder holder,ArrayList<Subcategory> data)
    {

            GridLayoutManager layoutManager = new GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false);
            holder.sub_recycle.setLayoutManager(layoutManager);
            SubCategoryRecycle adapter = new SubCategoryRecycle(context, data);
            System.out.println("Adapter");
            //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context.getResources().getDrawable(R.drawable.line_divider),context.getResources().getDrawable(R.drawable.line_divider),3);
            //holder.sub_recycle.addItemDecoration(dividerItemDecoration);
            holder.sub_recycle.setAdapter(adapter);





    }

   public int getItemViewType(int position)
   {
       return position;
   }
    public int getItemCount()
    {
        return list.size();
    }






}
