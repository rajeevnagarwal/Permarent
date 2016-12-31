package com.furniture.appliances.rentals.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.furniture.appliances.rentals.Login;
import com.furniture.appliances.rentals.R;
import com.furniture.appliances.rentals.adapter.NavigationDrawerAdapter;
import com.furniture.appliances.rentals.model.NavigationItem;
import com.furniture.appliances.rentals.util.AppPreferences;
import com.furniture.appliances.rentals.util.NavigationDrawerCallbacks;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class DrawerFragment extends Fragment implements NavigationDrawerCallbacks {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Furniture2 pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    AppPreferences apref = new AppPreferences();
    TextView email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
        email = (TextView)view.findViewById(R.id.txtUserEmail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(layoutManager);
        mDrawerList.setHasFixedSize(true);

        final List<NavigationItem> navigationItems = getMenu();
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(navigationItems);
        adapter.setNavigationDrawerCallbacks(this);
        mDrawerList.setAdapter(adapter);
        selectItem(mCurrentSelectedPosition);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!apref.IsLoginedByEmail(getActivity()) && !apref.IsLoginedByGoogle(getActivity()) && !apref.IsLoginedByFb(getActivity())) {
                    Intent i = new Intent(getActivity(), Login.class);
                    (getActivity()).startActivity(i);
                }
            }
        });
        return view;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    public List<NavigationItem> getMenu() {
        List<NavigationItem> items = new ArrayList<NavigationItem>();
        items.add(new NavigationItem("Home", getResources().getDrawable(R.mipmap.drawer_home_icon)));
        items.add(new NavigationItem("Addresses", getResources().getDrawable(R.mipmap.drawer_my_address)));
        items.add(new NavigationItem("My Orders", getResources().getDrawable(R.mipmap.drawer_order_icon)));
        items.add(new NavigationItem("Contact Us", getResources().getDrawable(R.mipmap.ic_call_grey600_24dp)));
        items.add(new NavigationItem("Rate Us", getResources().getDrawable(R.mipmap.ic_star_grey600_24dp)));
        items.add(new NavigationItem("FAQ's", getResources().getDrawable(R.mipmap.ic_help_grey600_24dp)));
        items.add(new NavigationItem("T & C", getResources().getDrawable(R.mipmap.ic_stars_grey600_24dp)));
        items.add(new NavigationItem("Privacy Policy", getResources().getDrawable(R.mipmap.ic_list_grey600_24dp)));
        items.add(new NavigationItem("Documents Required", getResources().getDrawable(R.drawable.ic_doc)));
        items.add(new NavigationItem("Return and Return Policy", getResources().getDrawable(R.drawable.ic_return)));
        items.add(new NavigationItem("Complaints",getResources().getDrawable(android.R.drawable.ic_input_get)));
        if (apref.IsLoginedByGoogle(getActivity()) || apref.IsLoginedByEmail(getActivity()) || apref.IsLoginedByFb(getActivity())) {

            items.add(new NavigationItem("LogOut", getResources().getDrawable(R.mipmap.drawer_logout_icon)));
        }
        if (!apref.IsLoginedByGoogle(getActivity()) && !apref.IsLoginedByEmail(getActivity()) && !apref.IsLoginedByFb(getActivity()))
            items.add(new NavigationItem("LogIn", getResources().getDrawable(R.mipmap.drawer_login_icon)));
        return items;
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     * @param toolbar      The Toolbar of the activity.
     */
    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = (View) getActivity().findViewById(fragmentId).getParent();
        mDrawerLayout = drawerLayout;


        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        /*if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }*/

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
        ((NavigationDrawerAdapter) mDrawerList.getAdapter()).selectPosition(position);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setUserData(Context context) {
        if (apref.IsLoginedByGoogle(context) && !apref.IsLoginedByEmail(context)) {
            ImageView avatarContainer = (ImageView) mFragmentContainerView.findViewById(R.id.imgAvatar);
            TextView email = (TextView) mFragmentContainerView.findViewById(R.id.txtUserEmail);
            TextView name = (TextView) mFragmentContainerView.findViewById(R.id.txtUsername);
            email.setText(AppPreferences.readString(getActivity(), "email", null));
            name.setText(AppPreferences.readString(getActivity(), "name", null));
            if (AppPreferences.readString(getActivity(), "image", null).contains("http") || AppPreferences.readString(getActivity(), "image", null).contains("https")) {
                Picasso.with(context)
                        .load(AppPreferences.readString(getActivity(), "image", null))
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(avatarContainer);
            } else {
                avatarContainer.setImageResource(R.drawable.user);
            }
        }
        if ((apref.IsLoginedByEmail(context) && !apref.IsLoginedByGoogle(context)) || apref.IsLoginedByFb(context)) {
            ImageView avatarContainer = (ImageView) mFragmentContainerView.findViewById(R.id.imgAvatar);
            ((TextView) mFragmentContainerView.findViewById(R.id.txtUserEmail)).setText(AppPreferences.readString(getActivity(), "email", null));
            ((TextView) mFragmentContainerView.findViewById(R.id.txtUsername)).setText(AppPreferences.readString(getActivity(), "name", null));
            avatarContainer.setImageResource(R.drawable.user);

        } if(!apref.IsLoginedByEmail(context) && !apref.IsLoginedByGoogle(context) && !apref.IsLoginedByFb(context)) {
            ImageView avatarContainer = (ImageView) mFragmentContainerView.findViewById(R.id.imgAvatar);
            ((TextView) mFragmentContainerView.findViewById(R.id.txtUsername)).setText("Hello!");
            ((TextView) mFragmentContainerView.findViewById(R.id.txtUserEmail)).setText("Please sign In");
            avatarContainer.setImageResource(R.drawable.user);
        }
    }

    public View getGoogleDrawer() {
        return mFragmentContainerView.findViewById(R.id.googleDrawer);
    }
}