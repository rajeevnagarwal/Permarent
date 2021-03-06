package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.furniture.appliances.rentals.Cart;
import com.furniture.appliances.rentals.Login;
import com.furniture.appliances.rentals.MainActivity;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.OptionsAdapter;
import com.furniture.appliances.rentals.database.DBInteraction;
import com.furniture.appliances.rentals.model.ModelOptions;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.ui.RoundedBitmapView;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Config;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.razorpay.Segment.context;

/**
 * Created by Rajeev Nagarwal on 12/10/2016.
 */

public class Account extends Fragment {
    public static final String TAG = "Account";
    ListView list;
    ImageView user_image;
    TextView welcome_text;
    TextView welcome_mail;
    LinearLayout ll_login;
    public static boolean isFragmentOpened;
    //Button ac_login;
    private AppPreferences apref = new AppPreferences();
    GoogleApiClient mGoogleApiClient;
    private ArrayList<ModelOptions> options;
    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ((MainActivity)getActivity()).changeToolbar("Account",true);
        System.out.println("Title"+getActivity().getTitle());
        getActivity().setTitle("Account");
            initialize(v);
            if(apref.IsLoginedByFb(getActivity()))
            {
                //ac_login.setText("Logout");
                System.out.println("Facebook");
                ll_login.setClickable(false);
                String name = AppPreferences.readString(getActivity(),"name","");
                if(name!=null) {
                    welcome_text.setText(name);
                    welcome_mail.setText(AppPreferences.readString(getActivity(),"email",""));
                }
                else {
                    welcome_text.setText("Please Login!!");
                }
                System.out.println("Image"+ AppPreferences.readString(getActivity(),"image",null));
                if (AppPreferences.readString(getActivity(), "image", null).contains("http") || AppPreferences.readString(getActivity(), "image", null).contains("https")) {
                    Picasso.with(context)
                            .load(AppPreferences.readString(getActivity(), "image", null))
                            .into(user_image,new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap bitmap = ((BitmapDrawable)user_image.getDrawable()).getBitmap();
                                    user_image.setImageDrawable(new RoundedBitmapView(getActivity()).createRoundedBitmapDrawableWithBorder(bitmap));

                                }

                                @Override
                                public void onError() {
                                    user_image.setImageResource(R.drawable.user);
                                    Bitmap bitmap = ((BitmapDrawable)user_image.getDrawable()).getBitmap();
                                    user_image.setImageDrawable(new RoundedBitmapView(getActivity()).createRoundedBitmapDrawableWithBorder(bitmap));

                                }
                            });
                } else {
                    user_image.setImageResource(R.drawable.user);
                    Bitmap bitmap = ((BitmapDrawable)user_image.getDrawable()).getBitmap();
                    user_image.setImageDrawable(new RoundedBitmapView(getActivity()).createRoundedBitmapDrawableWithBorder(bitmap));

                }

            }
            else
                {
                    //ac_login.setText("Login");
                    ll_login.setClickable(true);
                    welcome_text.setText("Please Login!!");
                    user_image.setImageResource(R.drawable.user);
                    Bitmap bitmap = ((BitmapDrawable)user_image.getDrawable()).getBitmap();
                    user_image.setImageDrawable(new RoundedBitmapView(getActivity()).createRoundedBitmapDrawableWithBorder(bitmap));

                }

        return v;
    }

    @Override
    public void onStart() {
        System.out.println("Start");
        super.onStart();
    }


    private void initialize(View v)
    {
       // ac_login = (Button)v.findViewById(R.id.ac_login);
        ll_login = (LinearLayout)v.findViewById(R.id.ll_login);
        list = (ListView)v.findViewById(R.id.list_account);
        welcome_text = (TextView)v.findViewById(R.id.text_welcome);
        welcome_mail = (TextView)v.findViewById(R.id.welcome_mail);
        welcome_mail.setText("");
        user_image = (ImageView)v.findViewById(R.id.user_image);
        options = new ArrayList<ModelOptions>();
        options.add(new ModelOptions("My Orders",R.mipmap.drawer_order_icon));
        options.add(new ModelOptions("My Wallet",R.drawable.ic_wallet));
        options.add(new ModelOptions("My Addresses",R.mipmap.drawer_my_address));
        options.add(new ModelOptions("My Coupons",R.drawable.ic_coupon));
        options.add(new ModelOptions("FAQs",R.mipmap.ic_help_grey600_24dp));
        options.add(new ModelOptions("Refer & Earn",R.drawable.ic_refer));
        options.add(new ModelOptions("Rate the App",R.mipmap.ic_star_grey600_24dp));
        options.add(new ModelOptions("Contact Us",R.mipmap.ic_call_grey600_24dp));
        options.add(new ModelOptions("Send Feedback",android.R.drawable.ic_menu_info_details));
        options.add(new ModelOptions("Notifications",R.drawable.ic_home));
        options.add(new ModelOptions("Our Policies",R.mipmap.ic_star_grey600_24dp));
        options.add(new ModelOptions("Documents Required",R.drawable.ic_doc));
        options.add(new ModelOptions("Track Order",R.drawable.ic_track_order));
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);

            }
        });
        /*ac_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(apref.IsLoginedByFb(getActivity()))
                {
                    Cart.QUANTITY=0;
                    Cart.TOTAL_AMOUNT=0;
                    DBInteraction dbInteraction = new DBInteraction(getActivity());
                    //dbInteraction.resetProducts(0, 0, 0);
                    dbInteraction.resetCart(0);
                    dbInteraction.clearDatabase();
                    dbInteraction.close();
                    getActivity().getSharedPreferences("permarent", 0).edit().clear().commit();
                    LoginManager.getInstance().logOut();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();


                }
                else
                {
                    Intent i = new Intent(getActivity(), Login.class);
                    startActivity(i);
                }
            }
        });*/
        if(apref.IsLoginedByGoogle(getActivity())||apref.IsLoginedByEmail(getActivity())||apref.IsLoginedByFb(getActivity())) {

            options.add(new ModelOptions("Logout", R.mipmap.drawer_login_icon));
        }
        else
        {
            options.add(new ModelOptions("Login", R.mipmap.drawer_logout_icon));

        }
        OptionsAdapter adapter = new OptionsAdapter(getActivity(),options);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;
                if(position==0)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(MyOrders.TAG);
                    if(fragment==null)
                    {
                        fragment = new MyOrders();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,MyOrders.TAG).addToBackStack(MyOrders.TAG).commit();
                }
                else if(position==1)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(MyWallet.TAG);
                    if(fragment==null)
                    {
                        fragment = new MyWallet();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,MyWallet.TAG).addToBackStack(MyWallet.TAG).commit();




                }
                else if(position==2)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Addresses.TAG);
                    if(fragment==null)
                    {
                        fragment = new Addresses();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,Addresses.TAG).addToBackStack(Addresses.TAG).commit();


                }
                else if(position==3)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(MyCoupon.TAG);
                    if(fragment==null)
                    {
                        fragment = new MyCoupon();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,MyCoupon.TAG).addToBackStack(MyCoupon.TAG).commit();


                }
                else if(position==4)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(FAQ.TAG);
                    if(fragment==null)
                    {
                        fragment = new FAQ();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,FAQ.TAG).addToBackStack(FAQ.TAG).commit();


                }
                else if(position==5)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ReferEarn.TAG);
                    if(fragment==null)
                    {
                        fragment = new ReferEarn();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,ReferEarn.TAG).addToBackStack(ReferEarn.TAG).commit();


                }
                else if(position==6)
                {
                    String url = "";
                    try {
                        //Check whether Google Play store is installed or not:
                        getActivity().getPackageManager().getPackageInfo("com.android.vending", 0);
                        url = Config.playStoreUrl;
                    } catch ( final Exception e ) {
                        url = Config.browserUrl;
                    }
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(in);

                }
                else if(position==7)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Contact.TAG);
                    if(fragment==null)
                    {
                        fragment = new Contact();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,Contact.TAG).addToBackStack(Contact.TAG).commit();




                }
                else if(position==8)
                {

                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ContactUs.TAG);
                    if(fragment==null)
                    {
                        fragment = new ContactUs();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,ContactUs.TAG).addToBackStack(ContactUs.TAG).commit();



                }
                else if(position==9)
                {


                }
                else if(position==10)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Policies.TAG);
                    if(fragment==null)
                    {
                        fragment = new Policies();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,Policies.TAG).addToBackStack(Policies.TAG).commit();


                }
                else if(position==11)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Documents.TAG);
                    if(fragment==null)
                    {
                        fragment = new Documents();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,Documents.TAG).addToBackStack(Documents.TAG).commit();



                }
                else if(position==12)
                {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Track.TAG);
                    if(fragment==null)
                    {
                        fragment = new Track();
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.tabcontent,fragment,Track.TAG).addToBackStack(Track.TAG).commit();
                }
                else if(position==13)
                {
                    if(apref.IsLoginedByEmail(getActivity()))
                    {
                        Cart.QUANTITY=0;
                        Cart.TOTAL_AMOUNT=0;
                        DBInteraction dbInteraction = new DBInteraction(getActivity());
                        //dbInteraction.resetProducts(0, 0, 0);
                        dbInteraction.resetCart(0);
                        dbInteraction.clearDatabase();
                        dbInteraction.close();
                        getActivity().getSharedPreferences("permarent", 0).edit().clear().commit();
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                    else if(apref.IsLoginedByGoogle(getActivity()))
                    {
                        Cart.QUANTITY=0;
                        Cart.TOTAL_AMOUNT=0;
                        DBInteraction dbInteraction = new DBInteraction(getActivity());
                        //dbInteraction.resetProducts(0, 0, 0);
                        dbInteraction.resetCart(0);
                        dbInteraction.clearDatabase();
                        dbInteraction.close();
                        getActivity().getSharedPreferences("permarent", 0).edit().clear().commit();
                        if (mGoogleApiClient.isConnected()) {
                            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                            mGoogleApiClient.disconnect();
                            mGoogleApiClient.connect();
                        }
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                    else if(apref.IsLoginedByFb(getActivity()))
                    {
                        Cart.QUANTITY=0;
                        Cart.TOTAL_AMOUNT=0;
                        DBInteraction dbInteraction = new DBInteraction(getActivity());
                        //dbInteraction.resetProducts(0, 0, 0);
                        dbInteraction.resetCart(0);
                        dbInteraction.clearDatabase();
                        dbInteraction.close();
                        getActivity().getSharedPreferences("permarent", 0).edit().clear().commit();
                        LoginManager.getInstance().logOut();
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                    else
                    {
                        Intent i = new Intent(getActivity(), Login.class);
                        startActivity(i);


                    }

                }
            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();
        isFragmentOpened = true;
        Config.OPENED_FRAGMENT = this;
        Config.CURRENT_ACTIVITY_CONTEXT = getActivity();
    }
    @Override
    public void onPause() {
        super.onPause();
        isFragmentOpened = false;
        Config.OPENED_FRAGMENT = null;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ab_cart).setVisible(false).setEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.ic_logo);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Account");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

}



