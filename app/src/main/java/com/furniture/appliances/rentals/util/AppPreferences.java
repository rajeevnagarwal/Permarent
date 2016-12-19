package com.furniture.appliances.rentals.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Shared Prefences saved in local files
 */
public class AppPreferences  {

    public static final String PREF_NAME = "permarent";

    @SuppressWarnings("deprecation")
    public static final int MODE = Context.MODE_WORLD_WRITEABLE;
    private String KEY_IS_LOGINED = "isLogined";
    private String KEY_IS_LOGINED_BY_FB = "isLoginedByFb";
    private String KEY_IS_LOGINED_BY_GOOGLE = "isLoginedByGoogle";
    private String KEY_IS_LOGINED_BY_EMAIL= "isLoginedByEmail";
    private String KEY_IS_READY_FOR_CHECKOUT1 ="isReadyForCheckout1";
    private String KEY_IS_READY_FOR_CHECKOUT2 ="isReadyForCheckout2";

    public boolean IsLogined(Context context) {
        return getPreferences(context).getBoolean(KEY_IS_LOGINED, false);
    }

    public void setIsLogined(Context context, boolean value) {
        getEditor(context).putBoolean(KEY_IS_LOGINED, value).commit();
    }
    public void setIsLoginedByEmail(Context context, boolean value) {
        getEditor(context).putBoolean(KEY_IS_LOGINED_BY_EMAIL, value).commit();
    }

    public boolean IsLoginedByEmail(Context context) {
        return getPreferences(context).getBoolean(KEY_IS_LOGINED_BY_EMAIL, false);
    }

    public void setIsLoginedByGoogle(Context context, boolean value) {
        getEditor(context).putBoolean(KEY_IS_LOGINED_BY_GOOGLE, value).commit();
    }

    public boolean IsLoginedByFb(Context context) {
        return getPreferences(context).getBoolean(KEY_IS_LOGINED_BY_FB, false);
    }
    public void setIsLoginedByFb(Context context, boolean value) {
        getEditor(context).putBoolean(KEY_IS_LOGINED_BY_FB, value).commit();
    }

    public boolean IsLoginedByGoogle(Context context) {
        return getPreferences(context).getBoolean(KEY_IS_LOGINED_BY_GOOGLE, false);
    }

    public void setIsReadyForCheckout1(Context context, boolean value) {
        getEditor(context).putBoolean(KEY_IS_READY_FOR_CHECKOUT1, value).commit();
    }

    public boolean IsReadyForCheckout1(Context context) {
        return getPreferences(context).getBoolean(KEY_IS_READY_FOR_CHECKOUT1, false);
    }
    public void setIsReadyForCheckout2(Context context, boolean value) {
        getEditor(context).putBoolean(KEY_IS_READY_FOR_CHECKOUT2, value).commit();
    }

    public boolean IsReadyForCheckout2(Context context) {
        return getPreferences(context).getBoolean(KEY_IS_READY_FOR_CHECKOUT2, false);
    }


    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeInt(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInt(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

}

