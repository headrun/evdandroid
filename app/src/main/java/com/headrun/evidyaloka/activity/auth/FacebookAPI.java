package com.headrun.evidyaloka.activity.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.headrun.evidyaloka.config.Constants;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by sujith on 8/3/17.
 */

public class FacebookAPI extends Activity {

    protected String TAG = FacebookAPI.class.getSimpleName();
    protected CallbackManager callbackManager;
    protected AccessTokenTracker accessTokenTracker;
    protected AccessToken accessToken;
    private Context mContext;

    public FacebookAPI(Context mContext) {

        this.mContext = mContext;

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (loginResult != null && !loginResult.getAccessToken().getToken().isEmpty()) {
                    /*utils.userSession.setSelLoginType("fb");
                    getFilerData("", "", "", loginResult.getAccessToken().getToken());*/
                    fbAccesToken(loginResult.getAccessToken());

                }
            }

            @Override
            public void onCancel() {
                // App code
                // Log.i(TAG, "cancel the sign in");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
                // Log.i(TAG, "login result is " + exception);
            }
        });

        if (AccessToken.getCurrentAccessToken() == null && Profile.getCurrentProfile() == null) {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_status", "user_about_me", "user_birthday",
                    "user_videos", "user_events", "public_profile"));
            //LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this, Arrays.asList("rsvp_event", "publish_actions"));

        } else {
            Log.i(TAG, "  alredy got acess token");
                /*LoginManager.getInstance().logOut();*/
            //userSession.setSelLoginType("fb");
            //getFilerData("", "", "", AccessToken.getCurrentAccessToken().getToken());
            fbAccesToken(AccessToken.getCurrentAccessToken());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        else
            Toast.makeText(this, " Facebook Login fail ", Toast.LENGTH_LONG).show();

    }

    public void fbAccesToken(AccessToken fbtoken) {

        if (fbtoken != null)
            if (fbtoken.getToken() != null) {
                HashMap<String, String> params = new HashMap<>();
                params.put("backend", "facebook");
                params.put("access_token", fbtoken.getToken());
                new AuthNetworkCall(mContext, params, Constants.LOGIN);
            }
    }

}
