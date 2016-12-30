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

    public ArrayList<ModelSubCategory> parselistproduct(Context context, JSONArray array) {
        ArrayList<ModelSubCategory> list = new ArrayList<>();
       /* try
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
        System.out.println("Bye"+list.size());*/
        return list;
    }

    public ArrayList<ModelCategory> parseCategoryList(Context context, JSONArray response) {
        ArrayList<ModelCategory> list = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                ModelCategory category = new ModelCategory();
                category.categoryId = obj.getString("categoryId");
                category.categoryName = obj.getString("categoryName");
                category.subcategory = "";
                category.subcategoryid = "";
                list.add(category);

            }

        } catch (Exception e) {

        }
        return list;
    }

    public ArrayList<ModelSubCategory> parseProductList(Context context, JSONArray response) {
        ArrayList<ModelSubCategory> modelSubCategoryArrayList = new ArrayList<>();
        ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject child = response.getJSONObject(i);
                ModelSubCategory modelSubCategory = new ModelSubCategory();
                //modelSubCategory.prod_code = child.getString("prod_code");
                modelSubCategory.productId = child.getString("productId");
                modelSubCategory.productName = child.getString("productName");
                modelSubCategory.vendorId = child.getJSONArray("vendorIds").toString();
                modelSubCategory.categoryId = child.getString("categoryId");
                modelSubCategory.categoryName = child.getString("categoryName");
                modelSubCategory.subCategoryId = child.getString("subCategoryId");
                modelSubCategory.subCategoryName = child.getString("subCategoryName");
                modelSubCategory.smallImages = child.getJSONArray("smallImages").toString();
                modelSubCategory.largeImages = child.getJSONArray("largeImages").toString();
                modelSubCategory.rentToOwn = child.getString("rentToOwn");
                JSONArray productDesc = child.getJSONArray("productDesc");
                if (modelSubCategory.categoryName.equals("Packages")) {   //Later
                    String temp_capacity = "", temp_briefDesc = "", temp_weight = "", temp_quantity = "", temp_material = "", temp_dimensions = "", temp_color = "", temp_type = "", temp_brand = "", temp_other = "";
                    //JSONArray temp = new JSONArray(description.substring(description.indexOf("["), description.lastIndexOf("]") + 1));
                    for (int j = 0; j < productDesc.length(); j++) {
                        JSONObject jsonObject = productDesc.getJSONObject(j);
                        temp_briefDesc = temp_briefDesc + jsonObject.getString("briefDesc") + "%";
                        temp_quantity = temp_quantity + jsonObject.getString("quantity") + "%";
                        temp_material = temp_material + jsonObject.getString("material") + "%";
                        temp_dimensions = temp_dimensions + jsonObject.getString("dimensions") + "%";
                        temp_color = temp_color + jsonObject.getString("color") + "%";
                        temp_weight = temp_weight + jsonObject.getString("weight") + "%";
                        temp_type = temp_type + jsonObject.getString("type") + "%";
                        if (jsonObject.has("capacity"))
                            temp_capacity = temp_capacity + jsonObject.getString("capacity") + "%";
                        if (jsonObject.has("brand"))
                            temp_brand = temp_brand + jsonObject.getString("brand") + "%";
                        if (jsonObject.has("otherDesc"))
                            temp_other = temp_other + jsonObject.getString("otherDesc") + "%";

                    }
                    modelSubCategory.briefDesc = temp_briefDesc;
                    modelSubCategory.capacity = temp_capacity;
                    modelSubCategory.weight = temp_weight;
                    modelSubCategory.material = temp_material;
                    modelSubCategory.dimensions = temp_dimensions;
                    modelSubCategory.color = temp_color;
                    modelSubCategory.type = temp_type;
                    modelSubCategory.brand = temp_brand;
                    modelSubCategory.otherDesc = temp_other;
                    modelSubCategory.max_quantity = temp_quantity;
                    modelSubCategory.package_products = String.valueOf(productDesc.length());

                } else {
                    //JSONObject temp = new JSONObject(description.substring(description.indexOf("{"), description.lastIndexOf("}") + 1));
                    JSONObject temp = productDesc.getJSONObject(0);
                    modelSubCategory.briefDesc = temp.getString("briefDesc");
                    modelSubCategory.dimensions = temp.getString("dimensions");
                    modelSubCategory.color = temp.getString("color");
                    modelSubCategory.type = temp.getString("type");
                    if (temp.has("weight"))
                        modelSubCategory.weight = temp.getString("weight");
                    if (temp.has("material"))
                        modelSubCategory.material = temp.getString("material");
                    if (temp.has("capacity"))
                        modelSubCategory.capacity = temp.getString("capacity");
                    if (temp.has("brand"))
                        modelSubCategory.brand = temp.getString("brand");
                    if (temp.has("otherDesc"))
                        modelSubCategory.otherDesc = temp.getString("otherDesc");
                    modelSubCategory.max_quantity = "0";
                    modelSubCategory.package_products = "0";
                }
                modelSubCategory.rentalAmount = child.getJSONArray("rentalAmount").toString();
                //modelSubCategory.min = child.getString("rent_duration");
                modelSubCategory.securityAmount = child.getString("securityAmount");
                modelSubCategory.minRentalDuration = child.getString("minRentalDuration");
                //modelSubCategory.shipping_charges = child.getString("shipping_charges");
                //modelSubCategory.created_on = child.getString("created_on");
                //modelSubCategory.quantity = 0;
                for (int j = 0; j < 4; j++) {
                    ModelCart modelCart = new ModelCart();
                    modelCart.quantity = 0;
                    modelCart.prod_id = modelSubCategory.productId;
                    modelCart.small_img = modelSubCategory.firstSmall();
                    modelCart.item_name = modelSubCategory.productName;
                    modelCart.rent_type = String.valueOf(j);
                    modelCart.item_id = modelSubCategory.productId + j;
                    modelCart.security_amount = modelSubCategory.securityAmount;
                    if (j == 0)
                        modelCart.rent_amount = String.valueOf(modelSubCategory.getThree());
                    else if (j == 1)
                        modelCart.rent_amount = String.valueOf(modelSubCategory.getSix());
                    else if (j == 2)
                        modelCart.rent_amount = String.valueOf(modelSubCategory.getNine());
                    else
                        modelCart.rent_amount = String.valueOf(modelSubCategory.getTwelve());
                    modelCart.total_amount = String.valueOf(Integer.parseInt(modelCart.rent_amount) + Integer.parseInt(modelCart.security_amount));
                    modelCartArrayList.add(modelCart);
                }
                modelSubCategoryArrayList.add(modelSubCategory);
            }
            DBInteraction dbInteraction = new DBInteraction(context);
            //dbInteraction.insertSubCategoryDetail(modelSubCategoryArrayList);
            dbInteraction.insertCartDetail(modelCartArrayList);
            dbInteraction.close();
            return modelSubCategoryArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean parseCategoryData(Context context, JSONArray response) {
        ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject child = response.getJSONObject(i);
                ModelCategory modelCategory = new ModelCategory();
                modelCategory.heading_name = child.getString("heading_name");
                modelCategory.img_name = child.getString("img_name");
                modelCategory.subcategory = "";
                modelCategoryArrayList.add(modelCategory);
                Cart.categoryIds.add(modelCategory.heading_name);
            }
            /*DBInteraction dbInteraction = new DBInteraction(context);
            dbInteraction.insertCategoryDetail(modelCategoryArrayList);
            dbInteraction.close();*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseSubCategoryData(Context context, String category, JSONArray response) {
        ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
        try {
            String temp = "";
            for (int i = 0; i < response.length(); i++) {
                temp = temp + response
                        .getString(i) + ",";
                Cart.subCategoryIds.add(response.getString(i));
            }
            temp = temp.substring(0, temp.length() - 1);
           /* DBInteraction dbInteraction = new DBInteraction(context);
            dbInteraction.updateCategoryDetail(category, temp);
            dbInteraction.close();*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseInsertUserData(Context context, String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.getString("message").equals("Registered Successfully"))
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
            JSONObject obj = new JSONObject(response);
            return obj.get("message").equals("correct");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseInsertOrderData(Context context, String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.getString("success").equals("true"))
                return true;
            else
                return false;
           // return response.equals("New record created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseGetUserData(Context context, JSONObject response) {
        System.out.println(response);
        try {
            final AppPreferences apref = new AppPreferences();
            ModelUser modelUser = new ModelUser();
            modelUser.firstname = response.getString("firstName");
            modelUser.lastname = response.getString("lastName");
            modelUser.email = response.getString("email");
            modelUser.sex = response.getString("sex");
            modelUser.source = response.getString("source");
            modelUser.mobileno = response.getJSONArray("contactNos").toString();
            ArrayList<ModelAddress> modelAddresses = new ArrayList<>();
            if (response.has("userPhotos")) {
                modelUser.image = response.getJSONArray("userPhotos").toString();
            }
            if (response.has("shippingAddress")) {
                JSONArray array = new JSONArray("shippingAddress");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    ModelAddress address = new ModelAddress();
                    address.city = obj.getString("city");
                    address.house = obj.getString("houseNo");
                    address.pincode = obj.getString("pincode");
                    address.area = obj.getString("localityName");
                    address.street = obj.getString("location");
                    address.name = obj.getString("state");
                    address.detail = obj.getString("others");
                    modelAddresses.add(address);

                }

            } else {
                ModelAddress address = new ModelAddress();
                address.city = "";
                address.house = "";
                address.pincode = "";
                address.area = "";
                address.street = "";
                address.name = "";
                address.detail = "";
                modelAddresses.add(address);

            }
            RocqAnalytics.initialize(context);
            RocqAnalytics.identity(modelUser.firstname + " " + modelUser.lastname, new
                    ActionProperties("Email", modelUser.email));
            apref.writeString(context, "name", modelUser.firstname + " " + modelUser.lastname);
            apref.writeString(context, "image", "");
            apref.writeString(context, "email", modelUser.email);
            /*DBInteraction dbInteraction = new DBInteraction(context);
            dbInteraction.insertUserDetail(modelUser);
            for (int i = 0; i < modelAddresses.size(); i++)
                dbInteraction.insertAddressDetail(modelAddresses.get(i));
            dbInteraction.close();*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseVerifyNumberData(Context context, JSONObject response) {
        try {
            System.out.println("Verify"+response);
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
            for (int i = 0; i < response.length(); i++) {
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
            /*DBInteraction dbInteraction = new DBInteraction(context);
            for(int i=0;i<modelOrderArrayList.size();i++)
            //dbInteraction.insertMyOrdersDetail(modelOrderArrayList.get(i));
            dbInteraction.close();*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public ArrayList<ModelAddress> getAddress(JSONArray array)
    {
        ArrayList<ModelAddress> list = new ArrayList<ModelAddress>();
        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);
                if (obj.has("shippingAddresses")) {
                    System.out.println("Found");

                    JSONArray add = obj.getJSONArray("shippingAddresses");
                    for (int j = 0; j < add.length(); j++) {
                        ModelAddress model = new ModelAddress();
                        JSONObject obj_add = add.getJSONObject(j);
                        System.out.println(obj_add.getString("location"));
                        model.location = obj_add.getString("location");
                        model.city = obj_add.getString("city");
                        model.state = obj_add.getString("state");
                        model.houseNo = obj_add.getString("houseNo");
                        model.localityName = obj_add.getString("localityName");
                        model.pincode = obj_add.getString("pincode");
                        model.others = obj_add.getString("others");
                        list.add(model);

                    }


                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

}


