package com.headrun.evidyaloka.activity.auth;

import android.content.Context;
import android.text.TextUtils;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;

import java.util.HashMap;

/**
 * Created by sujith on 7/3/17.
 */

public class AuthPresenter {

    AuthView mAuthView;
    Context mContext;

    public AuthPresenter(AuthView mAuthView, Context mContext) {
        this.mAuthView = mAuthView;
        this.mContext = mContext;
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

        if (TextUtils.isEmpty(fname) && TextUtils.isEmpty(lname) && TextUtils.isEmpty(email) && TextUtils.isEmpty(pwd)) {
            mAuthView.setError(mContext.getString(R.string.missing_fields));
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("first_name", fname);
            params.put("last_name", lname);
            params.put("email", email);
            params.put("password", pwd);
            new AuthNetworkCall(mContext, params, Constants.SIGNUP);
        }

    }

    public void signGoogle() {
        new GoogleAPI(mContext);
    }

    public void signFacebook() {
        new FacebookAPI(mContext);
    }

}

