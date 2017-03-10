package com.headrun.evidyaloka.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.EvdApplication;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.DemandFragment;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.activity.auth.ProfileFragment;
import com.headrun.evidyaloka.activity.sessions.SessionsTabFragment;
import com.headrun.evidyaloka.config.ApiEndpoints;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.FiltersDataResponse;
import com.headrun.evidyaloka.model.FiltersData;
import com.headrun.evidyaloka.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ResponseListener<FiltersDataResponse>, BottomNavigationView.OnNavigationItemSelectedListener {

    public String TAG = HomeActivity.class.getSimpleName();
    private final String TAG_SORT = "sort";

    private boolean mTwoPane;

    private BottomNavigationView bottom_nav;
    // private DrawerLayout drawer;
    // private ActionBarDrawerToggle toggle;
    // private NavigationView navigationView;
    private SimpleDraweeView user_pic;
    private LinearLayout profile_lay;
    private TextView login_txt_btn, txt_user_name;
    Menu NavMenu;
    MenuItem Log_out_item;
    MenuItem Session_item;

    private boolean redirect_to = false;
    private String redirect_type;

    // private TabLayout tabLayout;
    //private ViewPager mViewPager;
    // private HomePageAdapter mHomePageAdapter;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        //Log.i(TAG, "on destory");
    }

    @Override
    protected void onResume() {
        super.onResume();

        enableSession();
        /*if (drawer != null)*/
        /*    drawer.closeDrawers();*/
    }                              /**/

    private void enableSession() {

        if (utils != null && NavMenu != null)
            if (utils.userSession.getIsLogin())
                NavMenu.findItem(R.id.action_sessions).setEnabled(true);
            else
                NavMenu.findItem(R.id.action_sessions).setEnabled(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Log.i(TAG, "Demands  activity");
        getintentData();
        Constants.ISNOTIFICATION = false;
        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_nav);
        NavMenu = bottom_nav.getMenu();
        Session_item = NavMenu.findItem(R.id.action_sessions);

        //enableSession();
        //    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //   toggle = new ActionBarDrawerToggle(                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //navigationView = (NavigationView) findViewById(R.id.nav_view);
        // View headerview = navigationView.getHeaderView(0);

        //login_txt_btn = (TextView) headerview.findViewById(R.id.login_txt_btn);
        //user_pic = (SimpleDraweeView) headerview.findViewById(R.id.user_pic);
        //txt_user_name = (TextView) headerview.findViewById(R.id.txt_user_name);
        //profile_lay = (LinearLayout) headerview.findViewById(R.id.profile_lay);

        //setProfile();

        /*try {*/
        /*    NavMenu = navigationView.getMenu();*/
        /*    Log_out_item = NavMenu.findItem(R.id.action_logout);*/
        /*    Session_item = NavMenu.findItem(R.id.action_sessions);*/
        /*    Session_item.setVisible(false);*/
/*
*/

        /*} catch (Exception e) {*/
        /*    e.printStackTrace();*/
        /*}*/
/*
*/

        // enableLogin();

        // tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //  mViewPager = (ViewPager) findViewById(R.id.viewpager);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // drawer.addDrawerListener(toggle);
        //toggle.syncState();

        // navigationView.setNavigationItemSelectedListener(this);
        bottom_nav.setOnNavigationItemSelectedListener(this);

       /* login_txt_btn.setOnClickListener(new View.OnClickListener() {*/
       /*     @Override*/
       /*     public void onClick(View v) {*/
       /*         startActivity(new Intent(HomeActivity.this, LoginActivity.class));*/
       /*     }*/
       /* });*/

        //setupTabLayout();
        //showMoviesFragment();

        getFilerData();
        showFavoriteMoviesFragment();

    }

    private void getintentData() {

        Bundle data = getIntent().getExtras();
        if (data != null) {
            redirect_to = data.getBoolean(Constants.REDIRECT_TO);
            redirect_type = data.getString(Constants.TYPE);
        }
    }

    /*private void setProfile() {
        try {
            LoginResponse user_data = userSession.getUserData();
            if (user_data != null) {
                profile_lay.setVisibility(View.VISIBLE);

                if (user_data.data.picture != null && !user_data.data.picture.isEmpty()) {
                    ImageLoadingUtils.load(user_pic, userSession.getUserData().data.picture);
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
                    user_pic.getHierarchy().setRoundingParams(roundingParams);
                } else {
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
                    user_pic.getHierarchy().setRoundingParams(roundingParams);
                }

                if (user_data.data.username != null && !user_data.data.username.isEmpty()) {
                    txt_user_name.setText("Hi " + user_data.data.username);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* private void enableLogin() {
        if (isLogin()) {
            profile_lay.setVisibility(View.VISIBLE);
            login_txt_btn.setVisibility(View.GONE);
            Log_out_item.setVisible(true);

            if (userSession.getUserData() != null && userSession.getUserData().data != null) {
                if (userSession.getUserData().data.is_superuser) {
                    Session_item.setVisible(true);
                } else if (userSession.getUserData().data.roles.length > 0) {
                    List<String> roles_list = Arrays.asList(userSession.getUserData().data.roles);
                    if (roles_list.contains("Teacher") || roles_list.contains("Center Admin") || roles_list.contains("Class Assistant"))
                        Session_item.setVisible(true);
                }
            }

        } else {
            profile_lay.setVisibility(View.GONE);
            login_txt_btn.setText(R.string.login);
            login_txt_btn.setVisibility(View.VISIBLE);
            Log_out_item.setVisible(false);
            Session_item.setVisible(false);
        }
    }*/

    private void showFavoriteMoviesFragment() {

        if (redirect_to) {
            onNavigationItemSelected(bottom_nav.getMenu().getItem(1).setChecked(true));
            /*replaceFragment(new SessionsTabFragment().newInstance("prefered"));
            setActivityTitle("Sessions");*/
        } else {
            onNavigationItemSelected(bottom_nav.getMenu().getItem(0).setChecked(true));
            /*replaceFragment(new DemandFragment());
            setActivityTitle("Demands");*/
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeFragment, fragment)
                .commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_demands:
                replaceFragment(new DemandFragment());
                break;
            case R.id.action_sessions:
                if (redirect_type != null && redirect_type.equals("session_page"))
                    replaceFragment(new SessionsTabFragment().newInstance("prefered"));
                else
                    replaceFragment(new SessionsTabFragment());
                break;
            case R.id.action_profile:
                if (utils.userSession.getIsLogin())
                    replaceFragment(new ProfileFragment());
                else
                    startActivity(new Intent(this, LoginActivity.class)
                            .putExtra(Constants.TYPE, true)
                            .putExtra(Constants.REDIRECT_TO, "home_page"));
                break;
        }

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);*/
       /* drawer.closeDrawer(GravityCompat.START);*/
       /* if (!item.getTitle().toString().equals("logout"))*/

        setActivityTitle(item.getTitle().toString());

        return true;

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(FiltersDataResponse response) {

        Log.i(FiltersData.TAG, String.valueOf(response));
        if (response != null) {
            Constants.FILTERDATA = response;
            userSession.setLangFilter(Arrays.asList(response.languages).toString());
            userSession.setStateFilter(Arrays.asList(response.states).toString());
            userSession.setSessionTitles(Arrays.asList(response.sessions_filters).toString());
            userSession.setUserRoles(utils.userRolesMap(response.user_roles));
            userSession.getUserRoles();
        }
    }

    public void getFilerData() {

        if (Constants.FILTERDATA == null)
            new EVDNetowrkServices().getFiltersData(this, this);
    }

    private void logoutuser() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiEndpoints.SIGNOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //logoutUser();
                        //  enableLogin();
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                // utils.showProgressBar(false, progress_bar);
                if (error instanceof NetworkError) {
                    utils.showAlertDialog(getResources().getString(R.string.no_internet));
                } else {
                    utils.showAlertDialog(getResources().getString(R.string.faceing_issue));
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put("csrfmiddlewaretoken", utils.userSession.getCsrf());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new EVDNetowrkServices().getSessionHeaders(HomeActivity.this);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                new Utils().setHeaders(HomeActivity.this, response);
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setTag(TAG);

        EvdApplication.getInstance().addToRequestQueue(stringRequest);
    }

}
