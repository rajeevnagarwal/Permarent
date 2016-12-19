package com.furniture.appliances.rentals.Listeners;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.furniture.appliances.rentals.R;

/**
 * Created by Rajeev Nagarwal on 12/7/2016.
 */

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int MIN_DISTANCE = 10;
    private static final String TAG = "MyGestureListener";
    private RelativeLayout backLayout;
    private RelativeLayout frontLayout;
    private Animation inFromRight,outToRight,outToLeft,inFromLeft;


    public MyGestureListener(Context ctx, View convertView) {
        backLayout = (RelativeLayout) convertView.findViewById(R.id.layout_back);
        frontLayout = (RelativeLayout) convertView.findViewById(R.id.layout_front);
        inFromRight = AnimationUtils.loadAnimation(ctx, R.anim.in_from_right);
        outToRight = AnimationUtils.loadAnimation(ctx, R.anim.out_to_right);
        outToLeft = AnimationUtils.loadAnimation(ctx, R.anim.out_to_left);
        inFromLeft = AnimationUtils.loadAnimation(ctx, R.anim.in_from_left);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > MIN_DISTANCE) {
                if(diffX<0){

                    Log.v(TAG, "Swipe Right to Left");
                    if(backLayout.getVisibility()==View.GONE){
                        frontLayout.startAnimation(outToLeft);
                        backLayout.setVisibility(View.VISIBLE);
                       backLayout.startAnimation(inFromRight);
                        frontLayout.setVisibility(View.GONE);
                    }
                }else{
                    Log.v(TAG, "Swipe Left to Right");
                    if(backLayout.getVisibility()!=View.GONE){

                        backLayout.startAnimation(outToRight);
                        backLayout.setVisibility(View.GONE);
                        frontLayout.setVisibility(View.VISIBLE);
                        frontLayout.startAnimation(inFromLeft);
                    }
                }
            }
        }

        return true;
    }

}
