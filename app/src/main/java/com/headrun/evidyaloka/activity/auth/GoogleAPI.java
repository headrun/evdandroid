package com.headrun.evidyaloka.activity.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.LoginResponse;

import java.util.HashMap;

/**
 * Created by sujith on 8/3/17.
 */

public class GoogleAPI extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, ResponseListener<LoginResponse> {

    protected GoogleSignInOptions gso;
    protected GoogleApiClient mGoogleApiClient;
    Context mContext;

    protected static final int RC_SIGN_IN = 9001;

    public GoogleAPI(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .requestIdToken(mContext.getResources().getString(R.string.G_CLIENT))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignIn();

    }

    protected void googleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleSignInResult(result);
        }
    }

    protected void googleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            //utils.userSession.setSelLoginType("google");
            HashMap<String, String> params = new HashMap<>();
            params.put("backend", "facebook");
            params.put("access_token", acct.getIdToken());
            new AuthNetworkCall(mContext, params, Constants.LOGIN);
        } else {
            Toast.makeText(this, " google token is fail", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(LoginResponse response) {

    }
}
