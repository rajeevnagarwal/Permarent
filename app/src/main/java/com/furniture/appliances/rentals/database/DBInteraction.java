package com.furniture.appliances.rentals.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CursorAdapter;

import com.furniture.appliances.rentals.model.ModelAddress;
import com.furniture.appliances.rentals.model.ModelCart;
import com.furniture.appliances.rentals.model.ModelCategory;
import com.furniture.appliances.rentals.model.ModelOrder;
import com.furniture.appliances.rentals.model.ModelSubCategory;
import com.furniture.appliances.rentals.model.ModelUser;

import java.util.ArrayList;

/**
 * DBInteraction Class is used for Local Database. Used to perform Database
 * connectivity, Queries to the database, close database connection.
 */

public class DBInteraction extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "Permarent.db";
    public Context context = null;

    // Tables
    private String TABLE_CATEGORY = "category";
    private String TABLE_SUBCATEGORY = "subcategory";
    private String TABLE_USER = "user";
    private String TABLE_ADDRESS = "address";
    private String TABLE_CART = "cart";
    private String TABLE_MY_ORDERS = "myorders";
    private String TABLE_MY_WISHLIST = "wishlist";

    //TABLE CATEGORY
    private String FIELD_TABLE_CATEGORY_HEADING_NAME = "heading_name";
    private String FIELD_TABLE_CATEGORY_IMG_NAME = "img_name";
    private String FIELD_TABLE_CATEGORY_SUBCATEGORY = "subcategory";

    //TABLE SUB CATEGORY

    private String FIELD_TABLE_SUBCATEGORY_PROD_CODE = "prod_code";
    private String FIELD_TABLE_SUBCATEGORY_PROD_ID = "prod_id";
    private String FIELD_TABLE_SUBCATEGORY_PROD_NAME = "prod_name";
    private String FIELD_TABLE_SUBCATEGORY_VENDOR_CODE = "vendor_code";
    private String FIELD_TABLE_SUBCATEGORY_CATEGORY_CODE = "category_code";
    private String FIELD_TABLE_SUBCATEGORY_CATEGORY_DESC = "category_desc";
    private String FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_CODE = "subcategory_code";
    private String FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_DESC = "subcategory_desc";
    private String FIELD_TABLE_SUBCATEGORY_SMALL_IMG = "small_img";
    private String FIELD_TABLE_SUBCATEGORY_BIG_IMG = "big_img";
    private String FIELD_TABLE_SUBCATEGORY_MATERIAL = "material";
    private String FIELD_TABLE_SUBCATEGORY_DIMENSIONS = "dimensions";
    private String FIELD_TABLE_SUBCATEGORY_COLOR = "color";
    private String FIELD_TABLE_SUBCATEGORY_TYPE = "type";
    private String FIELD_TABLE_SUBCATEGORY_BRAND = "brand";
    private String FIELD_TABLE_SUBCATEGORY_OTHER_DESCRIPTION = "other_description";
    private String FIELD_TABLE_SUBCATEGORY_MAX_QUANTITY = "max_quantity";
    private String FIELD_TABLE_SUBCATEGORY_RENT_AMOUNT = "rent_amount";
    private String FIELD_TABLE_SUBCATEGORY_RENT_DURATION = "rent_duration";
    private String FIELD_TABLE_SUBCATEGORY_SECURITY_DEPOSIT = "security_deposit";
    private String FIELD_TABLE_SUBCATEGORY_MIN_RENT_PERIOD = "min_rent_period";
    private String FIELD_TABLE_SUBCATEGORY_SHIPPING_CHARGES = "shipping_charges";
    private String FIELD_TABLE_SUBCATEGORY_CREATED_ON = "created_on";
    private String FIELD_TABLE_SUBCATEGORY_RENT_TO_OWN = "rent_to_own";
    private String FIELD_TABLE_SUBCATEGORY_QUANTITY = "quantity";
    private String FIELD_TABLE_SUBCATEGORY_QUANTITY_QUARTERLY = "quantity_quarterly";
    private String FIELD_TABLE_SUBCATEGORY_QUANTITY_MONTHLY = "quantity_monthly";
    private String FIELD_TABLE_SUBCATEGORY_PACKAGE_PRODUCTS = "package_products";

    //TABLE USER
    private String FIELD_TABLE_USER_FIRST_NAME = "firstname";
    private String FIELD_TABLE_USER_LAST_NAME = "lastname";
    private String FIELD_TABLE_USER_EMAIL = "email";
    private String FIELD_TABLE_USER_MOBILE_NO = "mobileno";
    private String FIELD_TABLE_USER_ADDRESS = "address";
    private String FIELD_TABLE_USER_PINCODE = "pincode";
    private String FIELD_TABLE_USER_IMAGE = "image";
    private String FIELD_TABLE_USER_SOURCE = "source";

    //TABLE ADDRESS
    private String FIELD_TABLE_ADDRESS_DETAIL = "detail";
    private String FIELD_TABLE_ADDRESS_AREA = "area";
    private String FIELD_TABLE_ADDRESS_CITY = "city";
    private String FIELD_TABLE_ADDRESS_HOUSE_NO = "house";
    private String FIELD_TABLE_ADDRESS_STREET = "street";
    private String FIELD_TABLE_ADDRESS_NAME = "name";
    private String FIELD_TABLE_ADDRESS_PINCODE = "pincode";
    private String FIELD_TABLE_ADDRESS_MOBILE_NO = "mobile_no";

    //TABLE CART
    private String FIELD_TABLE_CART_ITEM_NAME = "item_name";
    private String FIELD_TABLE_CART_SMALL_IMG = "small_img";
    private String FIELD_TABLE_CART_RENT_AMOUNT = "rent_amount";
    private String FIELD_TABLE_CART_SECURITY_AMOUNT = "security_amount";
    private String FIELD_TABLE_CART_PROD_ID = "prod_id";
    private String FIELD_TABLE_CART_QUANTITY = "quantity";
    private String FIELD_TABLE_CART_RENT_TYPE = "rent_type";
    private String FIELD_TABLE_CART_ITEM_ID = "item_id";
    private String FIELD_TABLE_CART_TOTAL_AMOUNT = "total_amount";


    private String FIELD_TABLE_ORDER_ORDER_ID = "order_id";
    private String FIELD_TABLE_ORDER_PRODUCT_LIST = "product_list";
    private String FIELD_TABLE_ORDER_PAYMENT_ID = "payment_id";
    private String FIELD_TABLE_ORDER_EMAIL = "email";
    private String FIELD_TABLE_ORDER_NAME = "name";
    private String FIELD_TABLE_ORDER_ADDRESS = "address";
    private String FIELD_TABLE_ORDER_PINCODE = "pincode";
    private String FIELD_TABLE_ORDER_MOBILE_NO = "mobile_no";
    private String FIELD_TABLE_ORDER_AMOUNT_PAID = "amount_paid";
    private String FIELD_TABLE_ORDER_SECURITY_PAID = "security_paid";
    private String FIELD_TABLE_ORDER_RENT_PAID = "rent_paid";
    private String FIELD_TABLE_ORDER_TRANSACTION_STATUS = "transaction_status";
    private String FIELD_TABLE_ORDER_INVOICE_ID = "invoice_id";
    private String FIELD_TABLE_ORDER_DELIVERY_CHARGES = "delivery_charges";

    //TABLE WISHLIST
    private String FIELD_TABLE_WISHLIST_PID = "pid";
    private String FIELD_TABLE_WISHLIST_EMAIL = "email";
    private String FIELD_TABLE_WISHLIST_NAME = "name";






    public DBInteraction(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table wishlist "+"(_id integer primary key,pid text,email text,name text)");
       /* db.execSQL(
                "create table category " +
                        "(_id integer primary key,heading_name text unique,img_name text,subcategory text)"
        );
        db.execSQL(
                "create table subcategory " +
                        "(_id integer primary key, prod_code text unique,prod_id text,prod_name text,vendor_code text," +
                        "category_code text,category_desc text,subcategory_code text,subcategory_desc text," +
                        "small_img text,big_img text,material text,package_products text,dimensions text,color text,rent_to_own text" +
                        ",type text,brand text,other_description text,max_quantity text,rent_amount text,rent_duration text,security_deposit text," +
                        "min_rent_period text,shipping_charges text,created_on text,quantity int,quantity_quarterly int,quantity_monthly int)"
        );*/
        db.execSQL(
                "create table cart " +
                        "(_id integer primary key,small_img text,rent_amount text,prod_id text,quantity int,item_name text," +
                        "rent_type text,item_id text unique,total_amount text,security_amount text)"
        );
        /*db.execSQL(
                "create table user " +
                        "(_id integer primary key,firstname text,lastname text,email text," +
                        "mobileno text,address text,pincode text,image text,source text)"
        );
        db.execSQL(
                "create table address " +
                        "(_id integer primary key,detail text,area text,city text," +
                        "house text,street text,name text,pincode text,mobile_no text)"
        );

            db.execSQL(
                    "create table myorders " +
                            "(_id integer primary key,order_id text,product_list text,payment_id text," +
                            "email text,name text,address text,pincode text,mobile_no text,amount_paid text," +
                            "security_paid text,rent_paid text,transaction_status text,invoice_id text,delivery_charges text)"
            );*/



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
/*        db.delete(TABLE_ADDRESS, null, null);
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_MY_ORDERS,null,null);*/
        return true;
    }

    public boolean insertWishItem(String pid,String email,String name)
    {
        if(checkWishProduct(pid)==false) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put(FIELD_TABLE_WISHLIST_EMAIL, email);
            content.put(FIELD_TABLE_WISHLIST_NAME, name);
            content.put(FIELD_TABLE_WISHLIST_PID, pid);
            long a = db.insert(TABLE_MY_WISHLIST, null, content);
            return true;
        }
        else
        {
            return false;
        }
    }
   /* public boolean insertSubCategoryDetail(ArrayList<ModelSubCategory> modelSubCategoryArrayList) {

        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < modelSubCategoryArrayList.size(); i++) {
            ModelSubCategory modelSubCategory = modelSubCategoryArrayList.get(i);

            long a = db.insert("subcategory", null, addSubCategoryDetailFields(modelSubCategory));


        }
        return true;


    }*/

    public boolean insertCartDetail(ArrayList<ModelCart> modelCartArrayList) {

        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < modelCartArrayList.size(); i++) {
            ModelCart modelCart = modelCartArrayList.get(i);
            long a = db.insert("cart", null, addCartDetailFields(modelCart));
        }
        return true;


    }

   /* public boolean insertMyOrdersDetail(ModelOrder modelOrder) {

        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.insert("myorders", null, addOrderDetailFields(modelOrder));
        return true;


    }*/

    /*public boolean insertCategoryDetail(ArrayList<ModelCategory> modelCategoryArrayList) {

        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < modelCategoryArrayList.size(); i++) {
            ModelCategory modelCategory = modelCategoryArrayList.get(i);
            long a = db.insert("category", null, addCategoryDetailFields(modelCategory));
        }
        return true;


    }*/

    /*public boolean insertUserDetail(ModelUser modelUser) {

        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.insert("user", null, addUserDetailFields(modelUser));
        return true;
    }*/

    /*public boolean insertAddressDetail(ModelAddress modelAddress) {

        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.insert("address", null, addAddressDetailFields(modelAddress));
        return true;
    }*/

    /*public boolean updateCategoryDetail(String heading_name, String subcategory) {

        ContentValues values = new ContentValues();
        values.put(FIELD_TABLE_CATEGORY_SUBCATEGORY, subcategory);
        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.update("category", values, FIELD_TABLE_CATEGORY_HEADING_NAME + " = ?",
                new String[]{heading_name});
        return true;
    }

    public boolean updateSubCategoryDetail(String prod_id, int quantity, int flag) {

        ContentValues values = new ContentValues();
        if (flag == 0)
            values.put(FIELD_TABLE_SUBCATEGORY_QUANTITY, quantity);
        else if (flag == 1)
            values.put(FIELD_TABLE_SUBCATEGORY_QUANTITY_QUARTERLY, quantity);
        else
            values.put(FIELD_TABLE_SUBCATEGORY_QUANTITY_MONTHLY, quantity);
        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.update("subcategory", values, FIELD_TABLE_SUBCATEGORY_PROD_ID + " = ?",
                new String[]{prod_id});
        return true;
    }*/

    public boolean updateCartDetail(String item_id, int quantity) {

        ContentValues values = new ContentValues();
        values.put(FIELD_TABLE_CART_QUANTITY, quantity);
        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.update("cart", values, FIELD_TABLE_CART_ITEM_ID + " = ?",
                new String[]{item_id});
        return true;
    }

   /* public boolean resetProducts(int quantity, int quantity_quarterly, int quantity_monthly) {

        ContentValues values = new ContentValues();
        values.put(FIELD_TABLE_SUBCATEGORY_QUANTITY, quantity);
        values.put(FIELD_TABLE_SUBCATEGORY_QUANTITY_QUARTERLY, quantity_quarterly);
        values.put(FIELD_TABLE_SUBCATEGORY_QUANTITY_MONTHLY, quantity_monthly);
        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.update("subcategory", values, null, null);
        return true;
    }*/

    public boolean resetCart(int quantity) {

        ContentValues values = new ContentValues();
        values.put(FIELD_TABLE_CART_QUANTITY, quantity);
        SQLiteDatabase db = this.getWritableDatabase();
        long a = db.update("cart", values, null, null);
        return true;
    }


   /* private ContentValues addSubCategoryDetailFields(ModelSubCategory modelSubCategory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_TABLE_SUBCATEGORY_PROD_CODE, modelSubCategory.prod_code);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_PROD_ID, modelSubCategory.prod_id);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_PROD_NAME, modelSubCategory.prod_name);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_VENDOR_CODE, modelSubCategory.vendor_code);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_CATEGORY_CODE, modelSubCategory.category_code);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_CATEGORY_DESC, modelSubCategory.category_desc);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_CODE, modelSubCategory.subcategory_code);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_DESC, modelSubCategory.subcategory_desc);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_SMALL_IMG, modelSubCategory.small_img);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_BIG_IMG, modelSubCategory.big_img);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_MATERIAL, modelSubCategory.material);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_DIMENSIONS, modelSubCategory.dimensions);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_COLOR, modelSubCategory.color);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_TYPE, modelSubCategory.type);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_BRAND, modelSubCategory.brand);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_OTHER_DESCRIPTION, modelSubCategory.other_description);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_MAX_QUANTITY, modelSubCategory.max_quantity);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_RENT_AMOUNT, modelSubCategory.rent_amount);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_RENT_DURATION, modelSubCategory.rent_duration);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_SECURITY_DEPOSIT, modelSubCategory.security_deposit);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_MIN_RENT_PERIOD, modelSubCategory.min_rent_period);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_SHIPPING_CHARGES, modelSubCategory.shipping_charges);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_CREATED_ON, modelSubCategory.created_on);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_RENT_TO_OWN, modelSubCategory.rent_to_own);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_QUANTITY, modelSubCategory.quantity);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_QUANTITY_MONTHLY, modelSubCategory.quantity_monthly);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_QUANTITY_QUARTERLY, modelSubCategory.quantity_quarterly);
        contentValues.put(FIELD_TABLE_SUBCATEGORY_PACKAGE_PRODUCTS, modelSubCategory.package_products);
        return contentValues;

    }

    private ContentValues addCategoryDetailFields(ModelCategory modelCategory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_TABLE_CATEGORY_HEADING_NAME, modelCategory.heading_name);
        contentValues.put(FIELD_TABLE_CATEGORY_IMG_NAME, modelCategory.img_name);
        contentValues.put(FIELD_TABLE_CATEGORY_SUBCATEGORY, modelCategory.subcategory);
        return contentValues;
    }

    private ContentValues addUserDetailFields(ModelUser modelUser) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_TABLE_USER_FIRST_NAME, modelUser.firstname);
        contentValues.put(FIELD_TABLE_USER_LAST_NAME, modelUser.lastname);
        contentValues.put(FIELD_TABLE_USER_EMAIL, modelUser.email);
        contentValues.put(FIELD_TABLE_USER_MOBILE_NO, modelUser.mobileno);
        contentValues.put(FIELD_TABLE_USER_ADDRESS, modelUser.address);
        contentValues.put(FIELD_TABLE_USER_PINCODE, modelUser.pincode);
        contentValues.put(FIELD_TABLE_USER_IMAGE, modelUser.image);
        contentValues.put(FIELD_TABLE_USER_SOURCE, modelUser.source);
        return contentValues;
    }

    private ContentValues addAddressDetailFields(ModelAddress modelAddress) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_TABLE_ADDRESS_DETAIL, modelAddress.detail);
        contentValues.put(FIELD_TABLE_ADDRESS_AREA, modelAddress.area);
        contentValues.put(FIELD_TABLE_ADDRESS_CITY, modelAddress.city);
        contentValues.put(FIELD_TABLE_ADDRESS_HOUSE_NO, modelAddress.house);
        contentValues.put(FIELD_TABLE_ADDRESS_STREET, modelAddress.street);
        contentValues.put(FIELD_TABLE_ADDRESS_PINCODE, modelAddress.pincode);
        contentValues.put(FIELD_TABLE_ADDRESS_NAME, modelAddress.name);
        contentValues.put(FIELD_TABLE_ADDRESS_MOBILE_NO, modelAddress.mobile_no);
        return contentValues;
    }*/

    private ContentValues addCartDetailFields(ModelCart modelCart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_TABLE_CART_PROD_ID, modelCart.prod_id);
        contentValues.put(FIELD_TABLE_CART_RENT_AMOUNT, modelCart.rent_amount);
        contentValues.put(FIELD_TABLE_CART_SECURITY_AMOUNT, modelCart.security_amount);
        contentValues.put(FIELD_TABLE_CART_QUANTITY, modelCart.quantity);
        contentValues.put(FIELD_TABLE_CART_SMALL_IMG, modelCart.small_img);
        contentValues.put(FIELD_TABLE_CART_ITEM_NAME, modelCart.item_name);
        contentValues.put(FIELD_TABLE_CART_ITEM_NAME, modelCart.item_name);
        contentValues.put(FIELD_TABLE_CART_RENT_TYPE, modelCart.rent_type);
        contentValues.put(FIELD_TABLE_CART_ITEM_ID, modelCart.item_id);
        contentValues.put(FIELD_TABLE_CART_TOTAL_AMOUNT, modelCart.total_amount);
        return contentValues;

    }


    /*private ContentValues addOrderDetailFields(ModelOrder modelOrder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_TABLE_ORDER_ORDER_ID, modelOrder.orderid);
        contentValues.put(FIELD_TABLE_ORDER_PRODUCT_LIST, modelOrder.productlist);
        contentValues.put(FIELD_TABLE_ORDER_PAYMENT_ID, modelOrder.paymentid);
        contentValues.put(FIELD_TABLE_ORDER_EMAIL, modelOrder.email);
        contentValues.put(FIELD_TABLE_ORDER_NAME, modelOrder.name);
        contentValues.put(FIELD_TABLE_ORDER_ADDRESS, modelOrder.address);
        contentValues.put(FIELD_TABLE_ORDER_PINCODE, modelOrder.pincode);
        contentValues.put(FIELD_TABLE_ORDER_MOBILE_NO, modelOrder.mobileno);
        contentValues.put(FIELD_TABLE_ORDER_AMOUNT_PAID, modelOrder.amountpaid);
        contentValues.put(FIELD_TABLE_ORDER_SECURITY_PAID, modelOrder.securitypaid);
        contentValues.put(FIELD_TABLE_ORDER_RENT_PAID, modelOrder.rentpaid);
        contentValues.put(FIELD_TABLE_ORDER_TRANSACTION_STATUS, modelOrder.transactionstatus);
        contentValues.put(FIELD_TABLE_ORDER_INVOICE_ID, modelOrder.invoiceid);
        contentValues.put(FIELD_TABLE_ORDER_DELIVERY_CHARGES, modelOrder.deliverycharges);
        return contentValues;

    }*/
    public boolean checkWishProduct(String pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_MY_WISHLIST+" where "+FIELD_TABLE_WISHLIST_PID+" = "+"'"+pid+"'",null);
        if(cursor!=null&&cursor.getColumnCount()>0)
        {
            if(cursor.getCount()>0)
            {
                return true;
            }
            else
                return false;
        }
        return false;

    }

    public String getWishProducts(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("select pid from "+TABLE_MY_WISHLIST+" where "+FIELD_TABLE_WISHLIST_EMAIL+" = "+"'"+email+"'");
        Cursor cursor = db.rawQuery("select pid from "+TABLE_MY_WISHLIST+" where "+FIELD_TABLE_WISHLIST_EMAIL+" = "+"'"+email+"'",null);
        String result="";
        if(cursor!=null&&cursor.getColumnCount()>0)
        {
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++)
            {
                result = result+" "+cursor.getString(cursor.getColumnIndex(FIELD_TABLE_WISHLIST_PID));
                cursor.moveToNext();
            }
            return result;

        }
        else
            return "";
    }


    /*public ArrayList<ModelSubCategory> getSubCategories(String category) {
        ArrayList<ModelSubCategory> modelSubCategoryList = new ArrayList<ModelSubCategory>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_SUBCATEGORY + " where subcategory_desc = '" + category + "'", null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                String prod_code = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_CODE));
                String prod_id = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_ID));
                String prod_name = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_NAME));
                String vendor_code = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_VENDOR_CODE));
                String category_code = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CATEGORY_CODE));
                String category_desc = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CATEGORY_DESC));
                String subcategory_code = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_CODE));
                String subcategory_desc = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_DESC));
                String small_img = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SMALL_IMG));
                String big_img = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_BIG_IMG));
                String material = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MATERIAL));
                String dimensions = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_DIMENSIONS));
                String color = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_COLOR));
                String type = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_TYPE));
                String brand = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_BRAND));
                String other_description = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_OTHER_DESCRIPTION));
                String max_quantity = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MAX_QUANTITY));
                String rent_amount = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_AMOUNT));
                String rent_duration = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_DURATION));
                String security_deposit = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SECURITY_DEPOSIT));
                String min_rent_period = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MIN_RENT_PERIOD));
                String shipping_charges = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SHIPPING_CHARGES));
                String created_on = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CREATED_ON));
                String rent_to_own = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_TO_OWN));
                Integer quantity = cursor.getInt(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY));
                Integer quantity_quarterly = cursor.getInt(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY_QUARTERLY));
                Integer quantity_monthly = cursor.getInt(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY_MONTHLY));
                String package_products = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PACKAGE_PRODUCTS));

                ModelSubCategory modelSubCategory = new ModelSubCategory();
                modelSubCategory.prod_code = prod_code;
                modelSubCategory.prod_id = prod_id;
                modelSubCategory.prod_name = prod_name;
                modelSubCategory.vendor_code = vendor_code;
                modelSubCategory.category_code = category_code;
                modelSubCategory.category_desc = category_desc;
                modelSubCategory.subcategory_code = subcategory_code;
                modelSubCategory.subcategory_desc = subcategory_desc;
                modelSubCategory.small_img = small_img;
                modelSubCategory.big_img = big_img;
                modelSubCategory.material = material;
                modelSubCategory.dimensions = dimensions;
                modelSubCategory.color = color;
                modelSubCategory.type = type;
                modelSubCategory.brand = brand;
                modelSubCategory.other_description = other_description;
                modelSubCategory.max_quantity = max_quantity;
                modelSubCategory.rent_amount = rent_amount;
                modelSubCategory.rent_duration = rent_duration;
                modelSubCategory.security_deposit = security_deposit;
                modelSubCategory.min_rent_period = min_rent_period;
                modelSubCategory.shipping_charges = shipping_charges;
                modelSubCategory.created_on = created_on;
                modelSubCategory.rent_to_own = rent_to_own;
                modelSubCategory.quantity = quantity;
                modelSubCategory.quantity_quarterly = quantity_quarterly;
                modelSubCategory.quantity_monthly = quantity_monthly;
                modelSubCategory.package_products = package_products;
                modelSubCategoryList.add(modelSubCategory);


                cursor.moveToNext();
            }
            cursor.close();
            return modelSubCategoryList;
        }
        return null;

    }


    public ModelSubCategory getSubCategoryItemById(String id) {

        ModelSubCategory modelSubCategory = new ModelSubCategory();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_SUBCATEGORY + " where prod_id = '" + id + "'", null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();

            String prod_code = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_CODE));
            String prod_id = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_ID));
            String prod_name = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_NAME));
            String vendor_code = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_VENDOR_CODE));
            String category_code = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CATEGORY_CODE));
            String category_desc = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CATEGORY_DESC));
            String subcategory_code = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_CODE));
            String subcategory_desc = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_DESC));
            String small_img = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SMALL_IMG));
            String big_img = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_BIG_IMG));
            String material = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MATERIAL));
            String dimensions = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_DIMENSIONS));
            String color = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_COLOR));
            String type = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_TYPE));
            String brand = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_BRAND));
            String other_description = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_OTHER_DESCRIPTION));
            String max_quantity = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MAX_QUANTITY));
            String rent_amount = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_AMOUNT));
            String rent_duration = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_DURATION));
            String security_deposit = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SECURITY_DEPOSIT));
            String min_rent_period = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MIN_RENT_PERIOD));
            String shipping_charges = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SHIPPING_CHARGES));
            String created_on = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CREATED_ON));
            String rent_to_own = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_TO_OWN));
            Integer quantity = cursor.getInt(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY));
            Integer quantity_quarterly = cursor.getInt(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY_QUARTERLY));
            Integer quantity_monthly = cursor.getInt(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY_MONTHLY));
            String package_products = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PACKAGE_PRODUCTS));


            modelSubCategory.prod_code = prod_code;
            modelSubCategory.prod_id = prod_id;
            modelSubCategory.prod_name = prod_name;
            modelSubCategory.vendor_code = vendor_code;
            modelSubCategory.category_code = category_code;
            modelSubCategory.category_desc = category_desc;
            modelSubCategory.subcategory_code = subcategory_code;
            modelSubCategory.subcategory_desc = subcategory_desc;
            modelSubCategory.small_img = small_img;
            modelSubCategory.big_img = big_img;
            modelSubCategory.material = material;
            modelSubCategory.dimensions = dimensions;
            modelSubCategory.color = color;
            modelSubCategory.type = type;
            modelSubCategory.brand = brand;
            modelSubCategory.other_description = other_description;
            modelSubCategory.max_quantity = max_quantity;
            modelSubCategory.rent_amount = rent_amount;
            modelSubCategory.rent_duration = rent_duration;
            modelSubCategory.security_deposit = security_deposit;
            modelSubCategory.min_rent_period = min_rent_period;
            modelSubCategory.shipping_charges = shipping_charges;
            modelSubCategory.created_on = created_on;
            modelSubCategory.rent_to_own = rent_to_own;
            modelSubCategory.quantity = quantity;
            modelSubCategory.quantity_quarterly = quantity_quarterly;
            modelSubCategory.quantity_monthly = quantity_monthly;
            modelSubCategory.package_products = package_products;


            cursor.close();
            return modelSubCategory;
        }
        return null;

    }*/


    public ArrayList<ModelCart> getCart() {
        ArrayList<ModelCart> modelCartArrayList = new ArrayList<ModelCart>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_CART + " where quantity > 0", null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                Integer quantity = cursor.getInt(cursor
                        .getColumnIndex(FIELD_TABLE_CART_QUANTITY));
                String small_img = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CART_SMALL_IMG));
                String rent_amount = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CART_RENT_AMOUNT));
                String security_amount = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CART_SECURITY_AMOUNT));
                String prod_id = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CART_PROD_ID));
                String item_name = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CART_ITEM_NAME));
                String rent_type = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CART_RENT_TYPE));
                String item_id = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CART_ITEM_ID));
                String total_amount = cursor.getString(cursor.
                        getColumnIndex(FIELD_TABLE_CART_TOTAL_AMOUNT));

                ModelCart modelCart = new ModelCart();
                modelCart.quantity = quantity;
                modelCart.prod_id = prod_id;
                modelCart.small_img = small_img;
                modelCart.rent_amount = rent_amount;
                modelCart.item_name = item_name;
                modelCart.rent_type = rent_type;
                modelCart.item_id = item_id;
                modelCart.total_amount = total_amount;
                modelCart.security_amount = security_amount;
                modelCartArrayList.add(modelCart);


                cursor.moveToNext();
            }
            cursor.close();
            return modelCartArrayList;
        }
        return null;

    }

    public ModelCart getCartItemById(String id) {
        ModelCart modelCart = new ModelCart();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_CART + " where item_id = '" + id + "'", null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();

            Integer quantity = cursor.getInt(cursor
                    .getColumnIndex(FIELD_TABLE_CART_QUANTITY));
            String small_img = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CART_SMALL_IMG));
            String rent_amount = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CART_RENT_AMOUNT));
            String security_amount = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CART_SECURITY_AMOUNT));
            String prod_id = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CART_PROD_ID));
            String item_name = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CART_ITEM_NAME));
            String rent_type = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CART_RENT_TYPE));
            String item_id = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CART_ITEM_ID));
            String total_amount = cursor.getString(cursor.
                    getColumnIndex(FIELD_TABLE_CART_TOTAL_AMOUNT));


            modelCart.quantity = quantity;
            modelCart.prod_id = prod_id;
            modelCart.small_img = small_img;
            modelCart.rent_amount = rent_amount;
            modelCart.item_name = item_name;
            modelCart.rent_type = rent_type;
            modelCart.item_id = item_id;
            modelCart.total_amount = total_amount;
            modelCart.security_amount = security_amount;
            cursor.close();
            return modelCart;
        }
        return null;

    }


   /* public ModelUser getUserById(String emailid) {
        ModelUser modelUser = new ModelUser();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_USER + " where email = '" + emailid + "'", null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();

            String firstname = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_USER_FIRST_NAME));
            String lastname = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_USER_LAST_NAME));
            String email = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_USER_EMAIL));
            String mobileno = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_USER_MOBILE_NO));
            String address = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_USER_ADDRESS));
            String pincode = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_USER_PINCODE));


            modelUser.firstname = firstname;
            modelUser.lastname = lastname;
            modelUser.email = email;
            modelUser.mobileno = mobileno;
            modelUser.password = "";
            modelUser.address = address;
            modelUser.pincode = pincode;
            modelUser.image = "";
            modelUser.source = "false";


            cursor.close();
            return modelUser;
        }
        return null;

    }

    public ArrayList<ModelCategory> getAllCategories() {
        ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_CATEGORY, null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String heading_name = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CATEGORY_HEADING_NAME));
                String img_name = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CATEGORY_IMG_NAME));
                String subcategory = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_CATEGORY_SUBCATEGORY));

                ModelCategory modelCategory = new ModelCategory();
                modelCategory.heading_name = heading_name;
                modelCategory.img_name = img_name;
                modelCategory.subcategory = subcategory;
                modelCategoryArrayList.add(modelCategory);
                cursor.moveToNext();
            }
            cursor.close();
            return modelCategoryArrayList;
        }
        return null;

    }

    public ModelCategory getCategoryByName(String category) {
        ModelCategory modelCategory = new ModelCategory();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_CATEGORY + " where heading_name = '" + category + "'", null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();

            String heading_name = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CATEGORY_HEADING_NAME));
            String img_name = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CATEGORY_IMG_NAME));
            String subcategory = cursor.getString(cursor
                    .getColumnIndex(FIELD_TABLE_CATEGORY_SUBCATEGORY));


            modelCategory.heading_name = heading_name;
            modelCategory.img_name = img_name;
            modelCategory.subcategory = subcategory;

            cursor.close();
            return modelCategory;
        }
        return null;

    }

    public ArrayList<ModelSubCategory> getSelectedItems() {
        ArrayList<ModelSubCategory> modelSubCategoryList = new ArrayList<ModelSubCategory>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_SUBCATEGORY + " where quantity >0 OR quantity_quarterly >0 OR quantity_monthly>0", null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String prod_code = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_CODE));
                String prod_id = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_ID));
                String prod_name = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PROD_NAME));
                String vendor_code = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_VENDOR_CODE));
                String category_code = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CATEGORY_CODE));
                String category_desc = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CATEGORY_DESC));
                String subcategory_code = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_CODE));
                String subcategory_desc = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SUBCATEGORY_DESC));
                String small_img = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SMALL_IMG));
                String big_img = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_BIG_IMG));
                String material = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MATERIAL));
                String dimensions = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_DIMENSIONS));
                String color = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_COLOR));
                String type = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_TYPE));
                String brand = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_BRAND));
                String other_description = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_OTHER_DESCRIPTION));
                String max_quantity = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MAX_QUANTITY));
                String rent_amount = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_AMOUNT));
                String rent_duration = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_DURATION));
                String security_deposit = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SECURITY_DEPOSIT));
                String min_rent_period = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_MIN_RENT_PERIOD));
                String shipping_charges = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_SHIPPING_CHARGES));
                String created_on = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_CREATED_ON));
                String rent_to_own = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_RENT_TO_OWN));
                Integer quantity = cursor.getInt(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY));
                Integer quantity_quarterly = cursor.getInt(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY_QUARTERLY));
                Integer quantity_monthly = cursor.getInt(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_QUANTITY_MONTHLY));
                String package_products = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_SUBCATEGORY_PACKAGE_PRODUCTS));


                ModelSubCategory modelSubCategory = new ModelSubCategory();
                modelSubCategory.prod_code = prod_code;
                modelSubCategory.prod_id = prod_id;
                modelSubCategory.prod_name = prod_name;
                modelSubCategory.vendor_code = vendor_code;
                modelSubCategory.category_code = category_code;
                modelSubCategory.category_desc = category_desc;
                modelSubCategory.subcategory_code = subcategory_code;
                modelSubCategory.subcategory_desc = subcategory_desc;
                modelSubCategory.small_img = small_img;
                modelSubCategory.big_img = big_img;
                modelSubCategory.material = material;
                modelSubCategory.dimensions = dimensions;
                modelSubCategory.color = color;
                modelSubCategory.type = type;
                modelSubCategory.brand = brand;
                modelSubCategory.other_description = other_description;
                modelSubCategory.max_quantity = max_quantity;
                modelSubCategory.rent_amount = rent_amount;
                modelSubCategory.rent_duration = rent_duration;
                modelSubCategory.security_deposit = security_deposit;
                modelSubCategory.min_rent_period = min_rent_period;
                modelSubCategory.shipping_charges = shipping_charges;
                modelSubCategory.created_on = created_on;
                modelSubCategory.rent_to_own = rent_to_own;
                modelSubCategory.quantity = quantity;
                modelSubCategory.quantity_quarterly = quantity_quarterly;
                modelSubCategory.quantity_monthly = quantity_monthly;
                modelSubCategory.package_products = package_products;
                modelSubCategoryList.add(modelSubCategory);


                cursor.moveToNext();
            }
            cursor.close();
            return modelSubCategoryList;
        }
        return null;

    }

    public ArrayList<ModelAddress> getAllAddress() {
        ArrayList<ModelAddress> modelAddressArrayList = new ArrayList<ModelAddress>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_ADDRESS, null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String area = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ADDRESS_AREA));
                String city = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ADDRESS_CITY));
                String house = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ADDRESS_HOUSE_NO));
                String street = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ADDRESS_STREET));
                String pincode = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ADDRESS_PINCODE));
                String name = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ADDRESS_NAME));
                String detail = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ADDRESS_DETAIL));
                String mobile_no = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ADDRESS_MOBILE_NO));


                ModelAddress modelAddress = new ModelAddress();
                modelAddress.name = name;
                modelAddress.area = area;
                modelAddress.city = city;
                modelAddress.house = house;
                modelAddress.street = street;
                modelAddress.pincode = pincode;
                modelAddress.detail = detail;
                modelAddress.mobile_no = mobile_no;
                modelAddressArrayList.add(modelAddress);


                cursor.moveToNext();
            }
            cursor.close();
            return modelAddressArrayList;
        }
        return null;

    }

    public ArrayList<ModelOrder> getAllOrders() {
        ArrayList<ModelOrder> modelOrderArrayList = new ArrayList<ModelOrder>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                + TABLE_MY_ORDERS, null);
        if (cursor != null & cursor.getColumnCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String orderid = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_ORDER_ID));
                String productlist = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_PRODUCT_LIST));
                String paymentid = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_PAYMENT_ID));
                String email = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_EMAIL));
                String name = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_NAME));
                String address = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_ADDRESS));
                String pincode = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_PINCODE));
                String mobile_no = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_MOBILE_NO));
                String amountpaid = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_AMOUNT_PAID));
                String securitypaid = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_SECURITY_PAID));
                String rentpaid = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_RENT_PAID));
                String transactionstatus = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_TRANSACTION_STATUS));
                String invoiceid = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_INVOICE_ID));
                String deliverycharges = cursor.getString(cursor
                        .getColumnIndex(FIELD_TABLE_ORDER_DELIVERY_CHARGES));


                ModelOrder modelOrder = new ModelOrder();
                modelOrder.orderid = orderid;
                modelOrder.productlist = productlist;
                modelOrder.paymentid = paymentid;
                modelOrder.email = email;
                modelOrder.name = name;
                modelOrder.address = address;
                modelOrder.pincode = pincode;
                modelOrder.mobileno = mobile_no;
                modelOrder.amountpaid = amountpaid;
                modelOrder.securitypaid = securitypaid;
                modelOrder.rentpaid = rentpaid;
                modelOrder.transactionstatus = transactionstatus;
                modelOrder.invoiceid = invoiceid;
                modelOrder.deliverycharges = deliverycharges;

                modelOrderArrayList.add(modelOrder);


                cursor.moveToNext();
            }
            cursor.close();
            return modelOrderArrayList;
        }
        return null;

    }*/

}