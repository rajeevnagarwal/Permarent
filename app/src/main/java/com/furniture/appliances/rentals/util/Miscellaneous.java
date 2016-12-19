package com.furniture.appliances.rentals.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Infinia on 30-06-2015.
 */
public class Miscellaneous {
    public static void disableSoftInputFromAppearing(MaterialEditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }

    //Function to check Email Validation
    public static boolean isEmailValid(CharSequence email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        System.out.println(inputStr);

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        System.out.println(matcher);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }
    public static boolean checkEmptyEditext(Context context,String value,String message)
    {
        if (value != null && value.length() == 0) {
            Toast.makeText(context, message+" can not be empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public int dp2px(Context context,int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
