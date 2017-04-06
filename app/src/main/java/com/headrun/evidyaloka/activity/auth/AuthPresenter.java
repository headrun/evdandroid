package com.headrun.evidyaloka.activity.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.activity.demand_slots.DemandSlotActivity;
import com.headrun.evidyaloka.activity.profileUpdate.ProfileUpdate;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.evdservices.ChangeSessionStatusService;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by sujith on 7/3/17.
 */

public class AuthPresenter {

    static AuthView mAuthView;
    static Context mContext;
    static Utils utils;
    static String REDIRECT_TO = "";
    static String demand_id = "";
    CallbackManager callbackManager;

    public AuthPresenter(Context mContext, String REDIRECT_TO, String demand_id) {
        this.REDIRECT_TO = REDIRECT_TO;
        this.demand_id = demand_id;
        this.mContext = mContext;

        utils = new Utils(mContext);
    }

    public AuthPresenter(AuthView mAuthView, Context mContext) {
        this.mAuthView = mAuthView;
        this.mContext = mContext;

        utils = new Utils(mContext);
    }

    public void loginValidation(String email, String pwd) {

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
            mAuthView.setError(mContext.getString(R.string.missing_fields));
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("username", email);
            params.put("password", pwd);
            new AuthNetworkCall(mContext, params, Constants.LOGIN);
        }
    }

    public void signupValidation(String fname, String lname, String email, String pwd) {

        if (TextUtils.isEmpty(fname) && TextUtils.isEmpty(lname) && TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && TextUtils.isEmpty(pwd)) {
            mAuthView.setError(mContext.getString(R.string.missing_fields));
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("first_name", fname);
            params.put("last_name", lname);
            params.put("email", email);
            params.put("password", pwd);
            params.put("username", email);
            new AuthNetworkCall(mContext, params, Constants.SIGNUP);
        }

    }

    public void signGoogle() {
        new GoogleAPI(mContext);
    }

    public void signFacebook(Activity activity) {
        //fbLoginRegister(activity);
        new FacebookAPI(activity);
    }

    public static class AuthNetworkCall implements ResponseListener<LoginResponse> {

        public AuthNetworkCall(Context mcontaxt, HashMap<String, String> params, int type) {
            mAuthView.showProgress();
            if (Constants.LOGIN == type)
                new EVDNetowrkServices().login_authentication(mcontaxt, this, params);
            else
                new EVDNetowrkServices().sigup_auth(mcontaxt, this, params);

        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mAuthView.hideProgress();
            Log.i(TAG, "login resposne volley error fail");

        }

        @Override
        public void onResponse(LoginResponse response) {
            mAuthView.hideProgress();
            Log.i(TAG, "login resposne is" + response);

            if (response != null) {
                Log.i(TAG, "login sucess");
                if (response.status == 0) {
                    //Constants.LIST_DEMANDS.clear();
                    utils.userSession.setUserData(response);
                    redirctToAtivty();
                } else {
                    utils.showAlertDialog(response.message);
                }
            }
        }

    }

    protected static void redirctToAtivty() {

        if (REDIRECT_TO.isEmpty()) {
            mContext.startActivity(new Intent(mContext, HomeActivity.class));
            ((Activity) mContext).finish();
        } else if (REDIRECT_TO.equals(Constants.SLOTS_TYPE)) {

            if (Constants.LIST_DEMANDS.containsKey(demand_id)) {
                Demand demand = Constants.LIST_DEMANDS.get(demand_id);
                demand.Scholle_deatils = null;
                Constants.LIST_DEMANDS.put(demand_id, demand);
                mContext.startActivity(new Intent(mContext, DemandSlotActivity.class)
                        .putExtra(Demand.TAG_DEMAND, Constants.LIST_DEMANDS.get(demand_id))
                        .putExtra(SchoolDetails.TAG_SCHOOLDEATILS, demand.Scholle_deatils)
                        .putExtra(Constants.TYPE, false));
                ((Activity) mContext).finish();
            } else {
                mContext.startActivity(new Intent(mContext, HomeActivity.class));
            }
        } else if (REDIRECT_TO.equals(Constants.PROFILE_TYPE)) {
            mContext.startActivity(new Intent(mContext, ProfileUpdate.class));
        } else if (REDIRECT_TO.equals(Constants.HOME_TYPE)) {
            mContext.startActivity(new Intent(mContext, HomeActivity.class));
        }

        mContext.getApplicationContext().startService(new Intent(getApplicationContext(), ChangeSessionStatusService.class)
                .putExtra("request_type", "fcm"));

    }

    public void fbLoginRegister(Activity activty) {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (loginResult != null && !loginResult.getAccessToken().getToken().isEmpty()) {
                    Log.i(TAG, "acces token is" + loginResult.getAccessToken());

                }
            }

            @Override
            public void onCancel() {
                // App code
                Log.i(TAG, "cancel the sign in");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
                // Log.i(TAG, "login result is " + exception);
            }
        });

        if (AccessToken.getCurrentAccessToken() == null && Profile.getCurrentProfile() == null) {

            LoginManager.getInstance().logInWithReadPermissions(activty, Arrays.asList("email", "user_status", "user_about_me", "user_birthday",
                    "user_videos", "user_events", "public_profile"));
            // LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this, Arrays.asList("rsvp_event", "publish_actions"));

        } else {
            Log.i(TAG, "  alredy got acess token");
                /*LoginManager.getInstance().logOut();*/

        }
    }

}

