package com.furniture.appliances.rentals.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.model.ModelCategory;
import com.furniture.appliances.rentals.util.Config;
import com.furniture.appliances.rentals.util.Miscellaneous;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Edwin on 28/02/2015.
 */
public class GridAdapter  extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    Context context;
    ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
    private LayoutInflater mInflater;
    public GridAdapter(Context context,ArrayList<ModelCategory> modelCategoryArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.modelCategoryArrayList = modelCategoryArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ModelCategory modelCategory = modelCategoryArrayList.get(i);
        viewHolder.name.setText(modelCategory.heading_name);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        int height = Miscellaneous.convertDpToPixels(dpHeight-getStatusBarHeight()-34,context);
        height = height/ 3;
        ViewGroup.LayoutParams layoutParams = viewHolder.cardView.getLayoutParams();
        layoutParams.height = height; //this is in pixels
        viewHolder.cardView.setLayoutParams(layoutParams);
        String temp = Config.headingImage+ modelCategory.img_name;
        Picasso.with(viewHolder.image.getContext())
                .load(temp)
                .centerCrop()
                .resize(new Miscellaneous().dp2px(context, 155),
                        new Miscellaneous().dp2px(context, 87))
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {

        return modelCategoryArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView name;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
        }
    }

    public int getStatusBarHeight()
    {
        Rect rectangle = new Rect();
        Window window = ((MainActivity)context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarTop = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return  Math.abs(statusBarTop - contentViewTop);
    }


}
