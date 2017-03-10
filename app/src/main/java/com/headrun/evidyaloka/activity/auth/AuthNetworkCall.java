package com.headrun.evidyaloka.activity.auth;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.LoginResponse;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by sujith on 8/3/17.
 */

public class AuthNetworkCall implements ResponseListener<LoginResponse> {


    public AuthNetworkCall(Context mcontaxt, HashMap<String, String> params, int type) {
        if (Constants.LOGIN == type)
            new EVDNetowrkServices().login_authentication(mcontaxt, this, params);
        else
            new EVDNetowrkServices().sigup_auth(mcontaxt, this, params);

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Log.i(TAG, "login resposne is fail");
    }

    @Override
    public void onResponse(LoginResponse response) {

        Log.i(TAG, "login resposne is" + response);

    }
}
