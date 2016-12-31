package com.furniture.appliances.rentals;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.furniture.appliances.rentals.asyncTask.HttpCall;
import com.furniture.appliances.rentals.model.ModelUser;
import com.furniture.appliances.rentals.parser.ParseApi;
import com.furniture.appliances.rentals.restApi.EndPonits;
import com.furniture.appliances.rentals.service.SyncService;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.CheckInternetConnection;
import com.furniture.appliances.rentals.util.Config;
import com.furniture.appliances.rentals.util.Miscellaneous;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Infinia on 22-09-2015.
 */
public class Login extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private static final int RC_SIGN_IN = 0;
    private ConnectionResult mConnectionResult;

    private Toolbar mToolbar;
    Button login, signUp, fbbutton, mSignInButton;
    MaterialEditText username, password;
    String value_username, value_password;
    AppPreferences apref = new AppPreferences();
    public static CallbackManager callbackmanager;
    boolean result;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initView();
        setUpToolbar();
        initCallbackManager();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


    }

    private void initCallbackManager() {
        callbackmanager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject json,
                                    GraphResponse response) {
                                System.out.println(json);
                                System.out.println(response);
                                try {
                                    System.out.println("Hello");
                                    System.out.println("Hello"+json);
                                    String email = json.getString("email");
                                    String name = json.getString("name");
                                    ModelUser modelUser = new ModelUser();
                                    modelUser.firstname = name;
                                    modelUser.lastname = "";
                                    modelUser.email = email;
                                    modelUser.mobileno = "";
                                    modelUser.password = "";
                                    modelUser.address = "";
                                    modelUser.pincode = "";
                                    modelUser.image = "https://graph.facebook.com/"+json.getString("id")+"/picture?type=large";
                                    modelUser.source = "facebook";
                                    apref.setIsLoginedByFb(Login.this, true);
                                    AppPreferences.writeString(Login.this, "name", modelUser.firstname);
                                    AppPreferences.writeString(Login.this, "image", modelUser.image);
                                    AppPreferences.writeString(Login.this, "email", modelUser.email);
                                    Intent serviceIntent = new Intent(Login.this, SyncService.class);
                                    (Login.this).startService(serviceIntent);
                                    Intent i = new Intent(Login.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    if (new CheckInternetConnection(Login.this).isConnectedToInternet()) {
                                       // getUserData(apref.readString(getApplicationContext(), "email", null));
                                    } else {
                                        LoginManager.getInstance().logOut();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    public boolean getUserData(final String email) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        EndPonits.getUserInfo(params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Loading your profile...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                RocqAnalytics.initialize(Login.this);
                RocqAnalytics.identity(AppPreferences.readString(Login.this, "name",""), new
                        ActionProperties("Email",email));
                if (responseString.charAt(0) == '[') {
                    Intent serviceIntent = new Intent(Login.this, SyncService.class);
                    (Login.this).startService(serviceIntent);
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent serviceIntent = new Intent(Login.this, SyncService.class);
                    (Login.this).startService(serviceIntent);
                    JSONObject json = null;
                    try {
                        json = new JSONObject(responseString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    result = new ParseApi().parseGetUserData(getApplicationContext(), json);
                    //getPreviousOrders(email);
                }


            }
        });

        return result;
    }

    public boolean getPreviousOrders(String email) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        EndPonits.getPreviousOrders(params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(Login.this);
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
                result = new ParseApi().parsePreviousOrdersData(getApplicationContext(), response);
                if (apref.IsReadyForCheckout1(Login.this)) {
                    Intent i = new Intent(Login.this, Checkout1.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        });

        return result;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Login");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        login = (Button) findViewById(R.id.login);
       /* signUp = (Button) findViewById(R.id.signUp);
        mSignInButton = (Button) findViewById(R.id.btn_sign_in);*/
        fbbutton = (Button) findViewById(R.id.fbbutton);
        //username = (MaterialEditText) findViewById(R.id.username);
        password = (MaterialEditText) findViewById(R.id.password);
       // mSignInButton.setOnClickListener(this);
        //login.setOnClickListener(this);
        //signUp.setOnClickListener(this);
        fbbutton.setOnClickListener(this);
    }

    private void getDataFromFields() {
        value_username = username.getText().toString();
        value_password = password.getText().toString();
    }

    private boolean validations() {
        getDataFromFields();
        if (Miscellaneous.checkEmptyEditext(Login.this, value_username, "E Mail")) {
            return false;
        }
        if (!Miscellaneous.isEmailValid(value_username)) {
            Toast.makeText(Login.this, "E Mail address is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        return !Miscellaneous.checkEmptyEditext(Login.this, value_password, "Password");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.btn_sign_in:
                if (new CheckInternetConnection(Login.this).isConnectedToInternet()) {
                    System.out.println("Hello1");
                    signInWithGplus();
                } else {
                    System.out.println("Hello2");
                    new CheckInternetConnection(Login.this).showDialog();
                }
                break;*/
            case R.id.login:
                if (validations()) {
                    if (new CheckInternetConnection(Login.this).isConnectedToInternet()) {
                        new HttpCall().signIn(Login.this, value_username, value_password);

                    } else {

                        new CheckInternetConnection(Login.this).showDialog();
                    }
                }
                break;
           /* case R.id.signUp:
                Intent i2 = new Intent(Login.this, SignUp.class);
                startActivity(i2);
                finish();
                break;*/
            case R.id.fbbutton:
                if (new CheckInternetConnection(Login.this).isConnectedToInternet()) {
                    System.out.println("Hello");
                    LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile, email, user_birthday", "user_photos"));
                } else {
                    System.out.println("Hello3");
                    new CheckInternetConnection(Login.this).showDialog();
                }
                break;


        }
    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        RocqAnalytics.startScreen(this);
        RocqAnalytics.initialize(this);
        RocqAnalytics.trackScreen("Login", new ActionProperties());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Config.IS_APP_IN_FOREGROUND = true;
        Config.CURRENT_ACTIVITY_CONTEXT = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
        Config.IS_APP_IN_FOREGROUND = false;
        Config.CURRENT_ACTIVITY_CONTEXT = null;
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        RocqAnalytics.stopScreen(this);
    }


    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        callbackmanager.onActivityResult(requestCode, responseCode, intent);
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        // Get user's information
        getProfileInformation();

    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                ModelUser modelUser = new ModelUser();
                modelUser.firstname = personName;
                modelUser.lastname = "";
                modelUser.email = email;
                modelUser.mobileno = "";
                modelUser.password = "";
                modelUser.address = "";
                modelUser.pincode = "";
                modelUser.image = personPhotoUrl;
                modelUser.source = "google";
                AppPreferences.writeString(Login.this, "name", modelUser.firstname + "" + modelUser.lastname);
                AppPreferences.writeString(Login.this, "image", modelUser.image);
                AppPreferences.writeString(Login.this, "email", modelUser.email);
                apref.setIsLoginedByGoogle(Login.this, true);
                if (new CheckInternetConnection(Login.this).isConnectedToInternet()) {
                    getUserData(AppPreferences.readString(getApplicationContext(), "email", null));
                } else {
                    if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient.connect();

                    }
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }




}
