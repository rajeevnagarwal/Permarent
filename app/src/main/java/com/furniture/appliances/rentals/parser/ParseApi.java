package com.furniture.appliances.rentals.parser;

import android.content.Context;
import android.graphics.PorterDuff;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.furniture.appliances.rentals.Cart;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelCategory;
import com.furniture.appliances.rentals.model.ModelComplaintOrder;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Infinia on 18-09-2015.
 */
public class ParseApi {

    public ArrayList<ModelSubCategory> parselistproduct(Context context, JSONArray array)
    {
        ArrayList<ModelSubCategory> list = new ArrayList<>();
        try
        {
            System.out.println("array"+array.length());
            for(int i=0;i<array.length();i++) {
            JSONObject obj = array.getJSONObject(i);
            ModelSubCategory model = new ModelSubCategory();
            model.prod_id = obj.getString("productId");
            model.prod_name = obj.getString("productName");
            model.vendor_code = obj.getJSONArray("vendorIds").toString();
            System.out.println(model.vendor_code);
            model.category_code = obj.getString("categoryId");
            model.category_desc = obj.getString("categoryName");
            model.subcategory_code = obj.getString("subCategoryId");
            model.subcategory_desc = obj.getString("subCategoryName");
            model.small_img = obj.getJSONArray("smallImages").toString();
            model.big_img = obj.getJSONArray("largeImages").toString();
            model.min_rent_period = obj.getString("minRentalDuration");
            model.security_deposit = obj.getString("securityAmount");
            model.rent_to_own = String.valueOf(obj.getInt("rentToOwn"));
            model.productDesc = obj.getJSONArray("productDesc").toString();
            model.retailPrice = String.valueOf(obj.getInt("retailPrice"));
            model.popularRating = String.valueOf(obj.getInt("popularityRating"));
            model.relatedProducts = obj.getJSONArray("relatedProducts").toString();
            model.newRating = String.valueOf(obj.getInt("newRating"));
            model.productAvailability  = String.valueOf(obj.getInt("productAvailability"));
            model.rentalAmount = obj.getJSONArray("rentalAmount").toString();
            model.setValues();
            list.add(model);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        System.out.println("Bye"+list.size());
        return list;
    }

    public ArrayList<ModelCategory> parseCategoryList(Context context,JSONArray response)
    {
        ArrayList<ModelCategory> list = new ArrayList<>();
        try
        {
            for(int i=0;i<response.length();i++)
            {
                JSONObject obj = response.getJSONObject(i);
                ModelCategory category = new ModelCategory();
                category.categoryId = obj.getString("categoryId");
                category.categoryName = obj.getString("categoryName");
                category.subcategory="";
                category.subcategoryid="";
                list.add(category);

            }

        }
        catch(Exception e)
        {

        }
        return list;
    }

    public boolean parseProductList(Context context, JSONArray response) {
        ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<>();
        ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject child = response.getJSONObject(i);
                ModelSubCategory modelSubCategory = new ModelSubCategory();
                modelSubCategory.prod_code = child.getString("prod_code");
                modelSubCategory.prod_id = child.getString("prod_id");
                modelSubCategory.prod_name=child.getString("prod_name");
                modelSubCategory.vendor_code = child.getString("vendor_code");
                modelSubCategory.category_code = child.getString("category_code");
                modelSubCategory.category_desc = child.getString("category_desc");
                modelSubCategory.subcategory_code = child.getString("subcategory_code");
                modelSubCategory.subcategory_desc = child.getString("subcategory_desc");
                modelSubCategory.small_img = child.getString("small_img");
                modelSubCategory.big_img = child.getString("big_img");
                modelSubCategory.rent_to_own=child.getString("rent_to_own");
                String description = child.getString("description");
                if (modelSubCategory.category_desc.equals("Packages")) {
                    String temp_quantity = "", temp_material = "", temp_dimensions = "", temp_color = "", temp_type = "", temp_brand = "", temp_other = "";
                    JSONArray temp = new JSONArray(description.substring(description.indexOf("["), description.lastIndexOf("]") + 1));
                    for (int j = 0; j < temp.length(); j++) {
                        JSONObject jsonObject = temp.getJSONObject(j);
                        temp_quantity = temp_quantity + jsonObject.getString("Quantity") + "%";
                        temp_material = temp_material + jsonObject.getString("Material") + "%";
                        temp_dimensions = temp_dimensions + jsonObject.getString("Dimensions") + "%";
                        temp_color = temp_color + jsonObject.getString("Color") + "%";
                        temp_type = temp_type + jsonObject.getString("Type") + "%";
                        if(jsonObject.has("Brand"))
                        temp_brand = temp_brand + jsonObject.getString("Brand") + "%";
                        temp_other = temp_other + jsonObject.getString("Other Description") + "%";

                    }
                    modelSubCategory.material = temp_material;
                    modelSubCategory.dimensions = temp_dimensions;
                    modelSubCategory.color = temp_color;
                    modelSubCategory.type = temp_type;
                    modelSubCategory.brand = temp_brand;
                    modelSubCategory.other_description = temp_other;
                    modelSubCategory.max_quantity = temp_quantity;
                    modelSubCategory.package_products = String.valueOf(temp.length());

                } else {
                    JSONObject temp = new JSONObject(description.substring(description.indexOf("{"), description.lastIndexOf("}") + 1));
                    modelSubCategory.material = temp.getString("Material");
                    modelSubCategory.dimensions = temp.getString("Dimensions");
                    modelSubCategory.color = temp.getString("Color");
                    modelSubCategory.type = temp.getString("Type");
                    if(temp.has("Brand"))
                    modelSubCategory.brand = temp.getString("Brand");
                    modelSubCategory.other_description = temp.getString("Other Description");
                    modelSubCategory.max_quantity = "0";
                    modelSubCategory.package_products = "0";
                }
                modelSubCategory.rent_amount = child.getString("rent_amount");
                modelSubCategory.rent_duration = child.getString("rent_duration");
                modelSubCategory.security_deposit = child.getString("security_deposit");
                modelSubCategory.min_rent_period = child.getString("min_rent_period");
                modelSubCategory.shipping_charges = child.getString("shipping_charges");
                modelSubCategory.created_on = child.getString("created_on");
                modelSubCategory.quantity = 0;
                for(int j=0;j<3;j++)
                {
                    ModelCart modelCart = new ModelCart();
                    modelCart.quantity=0;
                    modelCart.prod_id=modelSubCategory.prod_id;
                    modelCart.small_img=modelSubCategory.small_img;
                    modelCart.item_name=modelSubCategory.prod_name;
                    modelCart.rent_amount = modelSubCategory.rent_amount;
                    modelCart.rent_type = String.valueOf(j);
                    modelCart.item_id = modelSubCategory.prod_id+j;
                    modelCart.security_amount=modelSubCategory.security_deposit;
                    if(j==0) {
                        int temp = Integer.parseInt(modelSubCategory.rent_amount);
                        temp=temp+(temp*(Config.DISCOUNT_HALF_YEARLY)/100);
                        temp=temp+Integer.parseInt(modelSubCategory.security_deposit);
                        modelCart.total_amount = String.valueOf(temp);
                    }
                    else if(j==1) {
                        int temp = Integer.parseInt(modelSubCategory.rent_amount);
                        temp=temp+Integer.parseInt(modelSubCategory.security_deposit);
                        modelCart.total_amount = String.valueOf(temp);

                    }
                    else {
                        int temp = Integer.parseInt(modelSubCategory.rent_amount);
                        temp=temp-(temp*(Config.DISCOUNT_YEARLY)/100);
                        temp=temp+Integer.parseInt(modelSubCategory.security_deposit);
                        modelCart.total_amount = String.valueOf(temp);
                    }
                    modelCartArrayList.add(modelCart);
                }
                modelSubCategoryArrayList.add(modelSubCategory);
            }
            DBInteraction dbInteraction = new DBInteraction(context);
            dbInteraction.insertSubCategoryDetail(modelSubCategoryArrayList);
            dbInteraction.insertCartDetail(modelCartArrayList);
            dbInteraction.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean parseCategoryData(Context context, JSONArray response) {
        ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject child = response.getJSONObject(i);
                ModelCategory modelCategory = new ModelCategory();
                modelCategory.heading_name=child.getString("heading_name");
                modelCategory.img_name=child.getString("img_name");
                modelCategory.subcategory="";
                modelCategoryArrayList.add(modelCategory);
                Cart.category.add(modelCategory.heading_name);
            }
            DBInteraction dbInteraction = new DBInteraction(context);
            dbInteraction.insertCategoryDetail(modelCategoryArrayList);
            dbInteraction.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean parseSubCategoryData(Context context, String category,JSONArray response) {
        ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
        try {
            String temp="";
            for (int i = 0; i < response.length(); i++) {
                temp=temp+response
                        .getString(i)+",";
                Cart.subcategory.add(response.getString(i));
            }
            temp = temp.substring(0,temp.length()-1);
            DBInteraction dbInteraction = new DBInteraction(context);
            dbInteraction.updateCategoryDetail(category, temp);
            dbInteraction.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseInsertUserData(Context context, String response) {
        try {
            if (response.equals("New record created successfully"))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseInsertAddressData(Context context, String response) {
        try {
            if (response.contains("New record created successfully"))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseSignInData(Context context, String response) {
        try {
            return response.equals("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseInsertOrderData(Context context, String response) {
        try {
            return response.equals("New record created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseGetUserData(Context context, JSONObject response) {
        try {
            final AppPreferences apref = new AppPreferences();
            ModelUser modelUser = new ModelUser();
            ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<>();
            modelUser.firstname = response.getString("firstname");
            modelUser.lastname = response.getString("lastname");
            modelUser.email = response.getString("email");
            String mobile = response.getString("mobileno");
            String address = response.getString("address");
            String pincode = response.getString("pincode");
            if (address.contains("{")) {
                JSONObject temp_address = new JSONObject(address.substring(address.indexOf("{"), address.lastIndexOf("}") + 1));
                JSONObject temp_mobile = new JSONObject(mobile.substring(address.indexOf("{"), mobile.lastIndexOf("}") + 1));
                JSONObject temp_pincode = new JSONObject(pincode.substring(address.indexOf("{"), pincode.lastIndexOf("}") + 1));
                for (int i = 0; i < temp_address.length(); i++) {
                    String temp_a = temp_address.getString(String.valueOf(i));
                    String temp_aa[] = temp_a.split(",");
                    String temp_p = temp_pincode.getString(String.valueOf(i));
                    String temp_m = temp_mobile.getString(String.valueOf(i));
                    ModelAddress modelAddress = new ModelAddress();
                    if (temp_aa.length == 1) {
                        modelAddress.name = "";
                        modelAddress.detail = temp_a;
                        modelAddress.city = "";
                        modelAddress.house = "";
                        modelAddress.street = "";
                        modelAddress.area = "";
                        modelAddress.mobile_no=temp_m;
                        modelAddress.pincode = temp_p;
                        modelAddressArrayList.add(modelAddress);
                    } else {
                        modelAddress.name = temp_aa[0];
                        modelAddress.detail = temp_a;
                        modelAddress.city = "";
                        modelAddress.house = "";
                        modelAddress.street = "";
                        modelAddress.area = "";
                        modelAddress.mobile_no=temp_m;
                        modelAddress.pincode = temp_p;
                        modelAddressArrayList.add(modelAddress);
                    }

                }
                modelUser.mobileno = "";
            } else {
                modelUser.mobileno = mobile;
                modelUser.address = "";
                modelUser.pincode = "";
                modelUser.image = "";
                ModelAddress modelAddress = new ModelAddress();
                modelAddress.name = "Home";
                modelAddress.detail = address;
                modelAddress.city = "";
                modelAddress.house = "";
                modelAddress.street = "";
                modelAddress.area = "";
                modelAddress.mobile_no=modelUser.mobileno;
                modelAddress.pincode = pincode;
                if(!address.equals(""))
                modelAddressArrayList.add(modelAddress);
            }
            RocqAnalytics.initialize(context);
            RocqAnalytics.identity(modelUser.firstname+" "+modelUser.lastname, new
                    ActionProperties("Email",modelUser.email));
            apref.writeString(context, "name", modelUser.firstname + "" + modelUser.lastname);
            apref.writeString(context, "image", "");
            apref.writeString(context, "email", modelUser.email);
            DBInteraction dbInteraction = new DBInteraction(context);
            dbInteraction.insertUserDetail(modelUser);
            for (int i = 0; i < modelAddressArrayList.size(); i++)
                dbInteraction.insertAddressDetail(modelAddressArrayList.get(i));
            dbInteraction.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseVerifyNumberData(Context context, JSONObject response) {
        try {
            String status = response.getString("status");
            if (status != null && status.equals("OK")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public ArrayList<String> parseCityData(Context context) {
        ArrayList<String> cityList = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                cityList.add(jsonObject.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityList;

    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public boolean parsePreviousOrdersData(Context context, JSONArray response) {
        try {
            ArrayList<ModelOrder> modelOrderArrayList = new ArrayList<>();
           for(int i=0;i<response.length();i++)
           {
               ModelOrder modelOrder = new ModelOrder();
               JSONObject jsonObject = response.getJSONObject(i);
               modelOrder.orderid = jsonObject.getString("order_id");
               modelOrder.productlist = jsonObject.getString("prod_list");
               modelOrder.paymentid = jsonObject.getString("payment_id");
               modelOrder.email = jsonObject.getString("email");
               modelOrder.name = jsonObject.getString("name");
               modelOrder.address = jsonObject.getString("address");
               modelOrder.pincode = jsonObject.getString("pincode");
               modelOrder.mobileno = jsonObject.getString("phone");
               modelOrder.amountpaid = jsonObject.getString("amount_paid");
               modelOrder.securitypaid = jsonObject.getString("security_paid");
               modelOrder.rentpaid = jsonObject.getString("rent_paid");
               modelOrder.transactionstatus = jsonObject.getString("transaction_status");
               modelOrder.invoiceid = jsonObject.getString("invoice_id");
               modelOrder.deliverycharges = jsonObject.getString("delivery_charges");
               modelOrderArrayList.add(modelOrder);

           }
            DBInteraction dbInteraction = new DBInteraction(context);
            for(int i=0;i<modelOrderArrayList.size();i++)
            dbInteraction.insertMyOrdersDetail(modelOrderArrayList.get(i));
            dbInteraction.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
