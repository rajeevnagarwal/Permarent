package com.furniture.appliances.rentals.asyncTask;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.AddNewAddress;
import com.furniture.appliances.rentals.Cart;
import com.furniture.appliances.rentals.Checkout1;
import com.furniture.appliances.rentals.Checkout2;
import com.furniture.appliances.rentals.Checkout3;
import com.furniture.appliances.rentals.Login;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.MobileVerification;
import com.furniture.appliances.rentals.OtpVerification;
import com.furniture.appliances.rentals.SignUp;
import com.furniture.appliances.rentals.ThankYou;
import com.furniture.appliances.rentals.Welcome;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.parser.ParseApi;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;
import com.furniture.appliances.rentals.util.GenerateOTP;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Infinia on 18-09-2015.
 */
public class HttpCall {

    boolean result;
    String[] details = new String[3];
    public boolean getAllCategory(final Context context, final Service mService)
    {
        EndPonits.getAllCategories(null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                result = new ParseApi().parseCategoryData(context, response);
                for (int i = 0; i < Cart.category.size(); i++)
                    new HttpCall().getSubCategories(context, Cart.category.get(i), null);
                if (mService != null)
                    mService.stopSelf();
            }

        });
        return result;
    }
    public boolean getSubCategories(final Context context,final String category,final Service mService)
    {
       final AppPreferences apref = new AppPreferences();
        RequestParams params = new RequestParams();
        params.put("category", category);
        EndPonits.getSubCategories(params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                result = new ParseApi().parseSubCategoryData(context, category, response);
                for (int i = 0; i < Cart.subcategory.size(); i++)
                    new HttpCall().getProductList(context, Cart.subcategory.get(i), true, null);
                apref.setIsLogined(context, true);
                if (mService != null)
                    mService.stopSelf();
            }

        });
        return result;
    }
    public boolean getProductList(final Context context, final String category, final boolean isRequestedFromService, final Service mService)
    {
        final ProgressDialog  progressDialog = new ProgressDialog(context);
        RequestParams params = new RequestParams();
        params.put("subcategory", category);
        EndPonits.getAllProducts(params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                if (!isRequestedFromService) {
                    progressDialog.setMessage("Getting details...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                result = new ParseApi().parseProductList(context, response);
                if (category.equals(Cart.subcategory.get(Cart.subcategory.size() - 1)))

                {
                    ((Welcome) context).removeDialog();
                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                }
                if (mService != null)
                    mService.stopSelf();
            }

        });
        return result;
    }
    public boolean signIn(final Context context, final String email,String password)
    {
        final ProgressDialog  progressDialog = new ProgressDialog(context);
        final AppPreferences apref = new AppPreferences();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password",password);
        EndPonits.checkUser(params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setMessage("Getting you back...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                result = new ParseApi().parseSignInData(context, response);
                if (result) {
                    new HttpCall().getUserData(context, email);

                } else
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }

        });
        return result;
    }
    public boolean getUserData(final Context context, final String email)
    {
        final ProgressDialog  progressDialog = new ProgressDialog(context);
        final AppPreferences apref = new AppPreferences();
        RequestParams params = new RequestParams();
        params.put("email", email);
        EndPonits.getUserInfo(params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setMessage("Loading your profile...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               System.out.println(responseString);


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                result = new ParseApi().parseGetUserData(context, response);
                if (result) {
                    getPreviousOrders(context,email);
                } else {
                    apref.setIsLogined(context, false);
                    Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return result;
    }

    public boolean getPreviousOrders(final Context context,String email) {
        final ProgressDialog  progressDialog = new ProgressDialog(context);
        final AppPreferences apref = new AppPreferences();
        RequestParams params = new RequestParams();
        params.put("email", email);
        EndPonits.getPreviousOrders(params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setMessage("Loading your profile...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                result = new ParseApi().parsePreviousOrdersData(context, response);
                apref.setIsLoginedByEmail(context, true);
                if (apref.IsReadyForCheckout1(context)) {
                    Intent i = new Intent(context, Checkout1.class);
                    context.startActivity(i);
                    ((Login) context).finish();
                } else {
                    Toast.makeText(context, "Welcome back", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                    ((Login) context).finish();
                }


            }
        });

        return result;
    }



    public boolean insertUserData(final Context context, final ModelUser modelUser)
    {
        final AppPreferences apref = new AppPreferences();
        final ProgressDialog  progressDialog = new ProgressDialog(context);
        RequestParams params = new RequestParams();
        params.put("firstName", modelUser.firstname);
        params.put("lastName", modelUser.lastname);
        params.put("email", modelUser.email);
        params.put("password", modelUser.password);
        params.put("address", modelUser.address);
        params.put("pincode", modelUser.pincode);
        params.put("mobileno", modelUser.mobileno);
        params.put("source", modelUser.source);
        params.put("sex",modelUser.sex);
        EndPonits.regNewUser(params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                if (modelUser.source.equals("permarent")) {
                    progressDialog.setMessage("Signing Up...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                System.out.println(response);
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.contains("Error: INSERT INTO")) {
                    if(context instanceof SignUp)
                    Toast.makeText(context, "User already exists with this Email Id!", Toast.LENGTH_SHORT).show();
                } else {
                    RocqAnalytics.initialize(context);
                    RocqAnalytics.identity(modelUser.firstname+" "+modelUser.lastname, new
                            ActionProperties("Email",modelUser.email));
                    result = new ParseApi().parseInsertUserData(context, response);
                    if (result) {
                        if (modelUser.source.equals("permarent")) {
                            Toast.makeText(context, "Registration Successfull", Toast.LENGTH_SHORT).show();
                            DBInteraction dbInteraction = new DBInteraction(context);
                            dbInteraction.insertUserDetail(modelUser);
                            dbInteraction.close();
                            apref.writeString(context, "name", modelUser.firstname + "" + modelUser.lastname);
                            apref.writeString(context, "image", "");
                            apref.writeString(context, "email", modelUser.email);
                            apref.setIsLoginedByEmail(context, true);
                            Intent i = new Intent(context, MainActivity.class);
                            context.startActivity(i);
                            if(context instanceof  Login)
                            ((Login) context).finish();
                            if(context instanceof SignUp)
                                ((SignUp) context).finish();
                        } else {
                            DBInteraction dbInteraction = new DBInteraction(context);
                            dbInteraction.insertUserDetail(modelUser);
                            dbInteraction.close();
                            apref.writeString(context, "name", modelUser.firstname);
                            apref.writeString(context, "image", modelUser.image);
                            apref.writeString(context, "email", modelUser.email);
                            if (modelUser.source.equals("google"))
                                apref.setIsLoginedByGoogle(context, true);
                            if (modelUser.source.equals("facebook"))
                                apref.setIsLoginedByFb(context, true);
                            Intent i = new Intent(context, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                            if(context instanceof  Login)
                            ((Login) context).finish();
                        }
                    } else
                        Toast.makeText(context, "Try again!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return result;
    }

    public boolean insertAddress(final Context context, final ModelAddress modelAddress, final ModelUser modelUser, final boolean isNoVerified)
    {
        final AppPreferences apref = new AppPreferences();
        final ProgressDialog  progressDialog = new ProgressDialog(context);
        RequestParams params = new RequestParams();
        params.put("email", modelUser.email);
        params.put("mobileno", modelUser.mobileno);
        params.put("pincode", modelAddress.pincode);
        params.put("address", modelAddress.detail);
        EndPonits.insertAddress(params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setMessage("Adding address...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                result = new ParseApi().parseInsertAddressData(context, response);
                if (result) {
                    Toast.makeText(context, "Address successfully added!", Toast.LENGTH_SHORT).show();
                    DBInteraction dbInteraction = new DBInteraction(context);
                    modelAddress.mobile_no = modelUser.mobileno;
                    dbInteraction.insertAddressDetail(modelAddress);
                    dbInteraction.close();
                    if (apref.IsReadyForCheckout2(context)) {
                        Intent i = new Intent(context, Checkout2.class);
                        context.startActivity(i);
                        if(context instanceof  OtpVerification)
                        ((OtpVerification) context).finish();
                    } else {
                        if (isNoVerified) {
                            Intent i = new Intent(context, MainActivity.class);
                            context.startActivity(i);
                            if(context instanceof  AddNewAddress)
                            ((AddNewAddress) context).finish();
                        } else {
                            Intent i = new Intent(context, MainActivity.class);
                            context.startActivity(i);
                            if(context instanceof  OtpVerification)
                            ((OtpVerification) context).finish();
                        }
                    }
                } else
                    Toast.makeText(context, "Try again!", Toast.LENGTH_SHORT).show();
            }

        });
        return result;
    }

    public boolean verifyNumber(final Context context,final ModelUser modelUser,final ModelAddress modelAddress)
    {
        final ProgressDialog  progressDialog = new ProgressDialog(context);
        final String otp = GenerateOTP.OTP(10000, 99999);
        RequestParams params = new RequestParams();
        params.put("method", "sms");
        params.put("api_key", Config.api_key);
        params.put("to", modelUser.mobileno);
        params.put("sender",Config.SMS_SENDER);
        params.put("message","Dear Customer, Your one-time-password (OTP) for verification is "+otp+". Thanks for signing up with permarent.");
        params.put("format","json");
        params.put("custom","1,2");
        params.put("flash","0");
        EndPonits.verifyNumber(params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.setMessage("Sending OTP...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                result = new ParseApi().parseVerifyNumberData(context, response);
                if (result) {
                    Intent i = new Intent(context, OtpVerification.class);
                    i.putExtra("modelAddress", modelAddress);
                    i.putExtra("modelUser", modelUser);
                    i.putExtra("otp", otp);
                    context.startActivity(i);
                    System.out.print("otp is " + otp);
                    if(context instanceof  MobileVerification)
                    ((MobileVerification) context).finish();
                } else
                    Toast.makeText(context, "Try again!", Toast.LENGTH_SHORT).show();
            }

        });
        return result;
    }

    public boolean insertOrderDetails(final Context context, final ModelOrder modelOrder)
    {
        final AppPreferences apref = new AppPreferences();
        final ProgressDialog  progressDialog = new ProgressDialog(context);
        RequestParams params = new RequestParams();
        params.put("orderid", modelOrder.orderid);
        params.put("productlist", modelOrder.productlist);
        params.put("paymentid", modelOrder.paymentid);
        params.put("email", modelOrder.email);
        params.put("name", modelOrder.name);
        params.put("address", modelOrder.address);
        params.put("pincode", modelOrder.pincode);
        params.put("mobileno", modelOrder.mobileno);
        params.put("amountpaid", modelOrder.amountpaid);
        params.put("securitypaid", modelOrder.securitypaid);
        params.put("rentpaid", modelOrder.rentpaid);
        params.put("transactionstatus", modelOrder.transactionstatus);
        params.put("invoiceid", modelOrder.invoiceid);
        params.put("deliverycharges",modelOrder.deliverycharges);
        EndPonits.insertOrderDetails(params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setMessage("Placing Order...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                result = new ParseApi().parseInsertOrderData(context, response);
                if (result) {

                    Cart.SECURITY_AMOUNT = 0;
                    Cart.QUANTITY = 0;
                    Cart.TOTAL_AMOUNT = 0;
                    DBInteraction dbInteraction = new DBInteraction(context);
                    dbInteraction.resetProducts(0, 0, 0);
                    dbInteraction.resetCart(0);
                    dbInteraction.insertMyOrdersDetail(modelOrder);
                    dbInteraction.close();
                    new HttpCall().sendOrderDetailsToUser(context, modelOrder);
                    new HttpCall().sendOrderDetailsToAdmin(context, modelOrder);
                    Intent i = new Intent(context, ThankYou.class);
                    context.startActivity(i);
                   /* Intent serviceIntent = new Intent(context, SMSService.class);
                    serviceIntent.putExtra("mobileNo", modelOrder.mobileno);
                    serviceIntent.putExtra("orderId", modelOrder.orderid);
                    context.startService(serviceIntent);*/
                    if(context instanceof  Checkout3)
                   ((Checkout3) context).finish();



                } else
                    Toast.makeText(context, "Try again!", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

        });
        return result;
    }

    public boolean sendOrderDetailsToUser(final Context context,final ModelOrder modelOrder)
    {
        RequestParams params = new RequestParams();
        params.put("method", "sms");
        params.put("api_key", Config.api_key);
        params.put("to", modelOrder.mobileno);
        params.put("sender",Config.SMS_SENDER);
        params.put("message","Hurray! Your order '"+modelOrder.orderid+"' has been successfully placed. Our representative will call you shortly. Happy Renting!");
        params.put("format","json");
        params.put("custom","1,2");
        params.put("flash","0");
        EndPonits.verifyNumber(params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                result = new ParseApi().parseVerifyNumberData(context, response);
                if (result) {

                } else
                {
                   // new HttpCall().sendOrderDetailsToUser(context, modelOrder);
                }

            }

        });
        return result;
    }

    public boolean sendOrderDetailsToAdmin(final Context context,final ModelOrder modelOrder)
    {
        RequestParams params = new RequestParams();
        params.put("method", "sms");
        params.put("api_key", Config.api_key);
        params.put("to", Config.adminNo);
        params.put("sender",Config.SMS_SENDER);
        params.put("message","Order '" + modelOrder.orderid +" "+modelOrder.invoiceid+" "+modelOrder.productlist+ "' Received from "+modelOrder.name+" "+modelOrder.mobileno+" !");
        params.put("format","json");
        params.put("custom","1,2");
        params.put("flash","0");
        EndPonits.verifyNumber(params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                result = new ParseApi().parseVerifyNumberData(context, response);
                if (result) {

                } else {
                   // new HttpCall().sendOrderDetailsToAdmin(context, modelOrder);
                }

            }

        });
        return result;
    }



}
