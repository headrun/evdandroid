package com.headrun.evidyaloka.activity.base;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.utils.NetworkUtils;
import com.headrun.evidyaloka.utils.UserSession;
import com.headrun.evidyaloka.utils.Utils;

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public String TAG = BaseActivity.class.getSimpleName();
    public Toolbar toolbar;
    //TabLayout tab_layout;
    CoordinatorLayout coordinatorLayout;
    MaterialDialog materialDialog;
    public UserSession userSession;

    protected CallbackManager callbackManager;
    protected AccessTokenTracker accessTokenTracker;
    protected AccessToken accessToken;
    protected GoogleSignInOptions gso;
    protected GoogleApiClient mGoogleApiClient;
    protected ProfileTracker profileTracker;
    protected static final int RC_SIGN_IN = 9001;

    protected Utils utils;

    private RelativeLayout no_result_lay;
    private TextView error_text;
    private ImageView error_img;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        utils = new Utils(this);

        injectViews();
        userSession = new UserSession(this);
        //Displaying the back button in the action bar
        if (isDisplayHomeAsUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void injectViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            no_result_lay = (RelativeLayout) findViewById(R.id.no_result_lay);
            error_text = (TextView) findViewById(R.id.textView);
            error_img = (ImageView) findViewById(R.id.error_img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //tab_layout = (TabLayout) findViewById(R.id.tabLayout);
        try {
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupToolbar();
    }

    protected void googleSettings() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .requestIdToken(getResources().getString(R.string.G_CLIENT))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setIcon(R.mipmap.action_bar_logo);

            // toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setActivityTitle(int title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle("  " + title);
    }

    public void setActivityTitle(String title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle("  " + title);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onActionBarHomeIconClicked();
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //If back button is displayed in action bar, return false
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    //Method for when home button is clicked
    public void onActionBarHomeIconClicked() {
        if (isDisplayHomeAsUpEnabled()) {
            onBackPressed();
        } else {
            finish();
        }
    }

    public boolean isInternetAvailable() {
        return NetworkUtils.isNetworkConnected(this);
    }

    public void showSnackBar(String value) {
        Snackbar snackbar = Snackbar
                .make(getCoordinatorLayout(), value, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void showSnackBar(int value) {
        Snackbar snackbar = Snackbar
                .make(getCoordinatorLayout(), value, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public void showProgressDialog() {
        materialDialog = new MaterialDialog.Builder(this)
                .content("Please Wait")
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    public void hideProgressDialog() {
        if (materialDialog != null)
            materialDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
        if (profileTracker != null)
            profileTracker.stopTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (profileTracker != null)
            profileTracker.startTracking();

    }

    protected void googleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected boolean isLogin() {

        boolean is_login = false;
        if (!new Utils().getCookieValue(this, "sessionid").isEmpty()) {
            is_login = true;

        }
        return is_login;
    }

    protected void logoutUser() {

        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null)
            LoginManager.getInstance().logOut();
        else if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected())
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {

                            }
                        });
        }
        userSession.clearCookie();
    }

    protected void getfbData() {

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                if (currentProfile != null) {
                    Log.i(TAG, "data is " + currentProfile);

                }
            }
        };

        /*

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                try {
                    if (currentAccessToken != null) {
                        String token = currentAccessToken.getToken();
                        Log.i(TAG, "current token is" + token);
                    } else {
                        Log.i(TAG, "current token is null");
                    }
                } catch (Exception e) {
                    Log.i(TAG, "acess token exe");
                    e.printStackTrace();
                }

            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        if (AccessToken.getCurrentAccessToken() != null) {

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {

                            if (AccessToken.getCurrentAccessToken() != null) {

                                if (me != null) {

                                    String profileImageUrl = ImageRequest.getProfilePictureUri(me.optString("id"), 500, 500).toString();
                                    Log.i(TAG, profileImageUrl);

                                }
                            }
                        }
                    });
            GraphRequest.executeBatchAsync(request);

        }*/
    }

    protected void setErrormessage(int img, String error) {

        if (no_result_lay != null) {
            if (!error.isEmpty())
                error_text.setText(error);
            else
                error_text.setText("");
        }
        error_img.setImageResource(img);

        no_result_lay.setVisibility(View.VISIBLE);
    }

    protected void volleyerror(VolleyError error, int length, String msg) {

        if (error instanceof NetworkError) {
            if (length == 0)
                setErrormessage(R.drawable.connection_error, "");
        }

    }

    protected void hiderrormessage() {
        no_result_lay.setVisibility(View.GONE);
    }

}
