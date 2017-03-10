package com.headrun.evidyaloka.activity.base;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.headrun.evidyaloka.activity.profileUpdate.ProfileUpdate;
import com.headrun.evidyaloka.activity.sessionDetails.SessionDetails;
import com.headrun.evidyaloka.config.ApiEndpoints;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.EvdApplication;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.IntialHandShakeResponse;
import com.headrun.evidyaloka.evdservices.ChangeSessionStatusService;
import com.headrun.evidyaloka.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sujith on 13/2/17.
 */

public class MainActivity extends AppCompatActivity implements ResponseListener<IntialHandShakeResponse> {

    public String TAG = MainActivity.class.getSimpleName();
    ProgressBar progress_bar;
    Utils utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.i(TAG, "Refreshed token: " + refreshedToken);

        utils = new Utils(this);


        if (!utils.getCookieValue(this, "sessionid").isEmpty())
            utils.userSession.setIsLoign(true);
        else
            utils.userSession.setIsLoign(false);


        Inteview();

        // getData();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (utils.userSession.getIsLogin()) {
                HashMap<String, String> key_map = new HashMap<>();
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
                    key_map.put(key, value.toString());
                    Log.d(TAG, "Key: " + key + " Value: " + value.toString());

                }

                if (key_map.containsKey("type")) {
                    Constants.ISNOTIFICATION = true;
                    if (key_map.get("type").toString().toLowerCase().equals("session")) {
                        startActivity(new Intent(this, SessionDetails.class).putExtra("session_id", key_map.get("id").toString()));
                    } else if (key_map.get("type").toString().toLowerCase().equals("profile")) {
                        startActivity(new Intent(this, ProfileUpdate.class));
                    } else {
                        LoginChecking();
                    }

                } else {
                    LoginChecking();
                }
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        } else {
            LoginChecking();
        }
    }

    private void Inteview() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        splash();
    }

    private void splash() {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    getData();
                }
            }
        };
        timerThread.start();
    }

    private void LoginChecking() {

        if (utils.getCookieValue(this, "sessionid").isEmpty())
            getToken();
        else
            loginactivty();

    }

    private void getToken() {

        // utils.showProgressBar(true, progress_bar);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiEndpoints.CSRFTOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loginactivty();

                        //callIntialhandShake();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                utils.showProgressBar(false, progress_bar);
                if (error instanceof NetworkError) {
                    utils.showAlertDialog(getResources().getString(R.string.no_internet));
                } else {
                    utils.showAlertDialog(getResources().getString(R.string.faceing_issue));
                }
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                new Utils().setHeaders(MainActivity.this, response);
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setTag(TAG);

        EvdApplication.getInstance().addToRequestQueue(stringRequest);
    }

    private void callIntialhandShake() {
        new EVDNetowrkServices().intailHandShake(this, this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        // utils.showProgressBar(false, progress_bar);
        startActivity(new Intent(this, HomeActivity.class));

    }

    @Override
    public void onResponse(IntialHandShakeResponse response) {
        // Log.i(TAG, "fb id " + response.keys.fb_id + "google id " + response.keys.google_id);

        if (response != null) {
            if (response.keys.fb_id != null)
                utils.userSession.setFb_Id(response.keys.fb_id);
            if (response.keys.google_id != null)
                utils.userSession.setGoogle_Id(response.keys.google_id);
        }
        // utils.showProgressBar(false, progress_bar);
        loginactivty();
        finish();
    }

    private void loginactivty() {

        PackageInfo pinfo = utils.getPackageInfo();

        if (utils.userSession.getLoginFirst().equals("0")) {
            utils.userSession.setLogin_first("1");
            startActivity(new Intent(this, LoginActivity.class));
            getApplicationContext().startService(new Intent(getApplicationContext(), ChangeSessionStatusService.class)
                    .putExtra("request_type", "fcm"));
            finish();
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

    }

    public boolean isInstallFromUpdate() {
        try {
            long firstInstallTime = this.getPackageManager().getPackageInfo(getPackageName(), 0).firstInstallTime;
            long lastUpdateTime = this.getPackageManager().getPackageInfo(getPackageName(), 0).lastUpdateTime;
            return firstInstallTime != lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


}
