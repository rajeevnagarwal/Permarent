package com.furniture.appliances.rentals.util;

/**
 * Created by Infinia on 11-06-2015.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.furniture.appliances.rentals.R;


public class CustomTextView extends TextView {
    String type;
    public CustomTextView(Context context) {
        super(context);
        setFont();
    }
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a=getContext().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView);
        type = a.getString(R.styleable.CustomTextView_type);
        setFont();
    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");;
        if(type.equals("bold")){
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");}
        if(type.equals("regular")){
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");}
        if(type.equals("light")){
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");}
        setTypeface(font, Typeface.NORMAL);
    }
}
