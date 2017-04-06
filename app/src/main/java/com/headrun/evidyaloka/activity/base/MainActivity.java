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
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.headrun.evidyaloka.activity.SelectionDiscussion.SelectionDiscussionActivity;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.activity.profileUpdate.ProfileUpdate;
import com.headrun.evidyaloka.activity.self_evaluation.SelfEvaluationActivity;
import com.headrun.evidyaloka.activity.sessionDetails.SessionDetails;
import com.headrun.evidyaloka.config.ApiEndpoints;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.EvdApplication;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.IntialHandShakeResponse;
import com.headrun.evidyaloka.evdservices.ChangeSessionStatusService;
import com.headrun.evidyaloka.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by sujith on 13/2/17.
 */

public class MainActivity extends AppCompatActivity implements ResponseListener<IntialHandShakeResponse> {

    public String TAG = MainActivity.class.getSimpleName();
    ProgressBar progress_bar;
    Utils utils;
    int sample = 1;
    boolean onstart = false;

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

    }


    private void getData() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            navigateBundleData(bundle);
        } else {
            Log.i(TAG, "extras getting null");
            LoginChecking();
        }
    }

    private void navigateBundleData(Bundle bundle) {
        Intent intent = getIntent();
        String key_map = "";
        String id = "";
        new HomePresenter(this).setGalleryImages();
        Log.i(TAG, "bundle has data ");
        if (utils.userSession.getIsLogin()) {
            //new HomePresenter(this).setGalleryImages();
            Log.i(TAG, "user hass login ");
            if (intent.hasExtra("type"))
                key_map = bundle.getString("type").toLowerCase().trim();

            if (intent.hasExtra("id"))
                id = bundle.getString("id").toLowerCase().trim();

            Log.i(TAG, "notify type is " + key_map);
            Log.i(TAG, "notify id is  " + key_map);

            if (!key_map.isEmpty()) {
                Constants.ISNOTIFICATION = true;
                if (key_map.equals("session") && !id.isEmpty()) {
                    startActivity(new Intent(this, SessionDetails.class).putExtra("session_id", id));
                } else if (key_map.equals("profile")) {
                    startActivity(new Intent(this, ProfileUpdate.class));
                } else if (key_map.equals("latest_demands")) {
                    String medium = utils.userSession.getUserData().data.pref_medium;
                    if (medium != null && !medium.trim().isEmpty()) {
                        utils.userSession.setSelLangFilter(Arrays.asList(medium.trim()).toString());
                        utils.userSession.setSelStateFilter(Arrays.asList("").toString());
                    }
                    startActivity(new Intent(this, HomeActivity.class));
                } else if (key_map.equals("self_eval") && checkSE()) {
                    startActivity(new Intent(this, SelfEvaluationActivity.class));
                    finish();
                } else if (key_map.equals("tsd")) {
                    startActivity(new Intent(this, SelectionDiscussionActivity.class));
                    finish();
                } else {
                    Log.i(TAG, "kee map is differ ");
                    LoginChecking();
                }

            } else {
                Log.i(TAG, "kee map is empty ");
                LoginChecking();
            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private boolean checkSE() {
        boolean check_se_val = false;
        if (Constants.SELF_VAL_ONBOARD.containsKey(Constants.SE) && Constants.SELF_VAL_ONBOARD.get(Constants.SE).size() > 0)
            check_se_val = true;

        Log.i(TAG, "check se is " + check_se_val);
        return check_se_val;
    }

    private void Inteview() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onstart = true;
        Log.i(TAG, "start onstrat " + onstart);
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "resume onstrat " + onstart);

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
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "pause onstrat " + onstart);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onstart = false;
        Log.i(TAG, "stop onstrat " + onstart);
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

        // PackageInfo pinfo = utils.getPackageInfo();

        if (utils.userSession.getIsLogin() == false && utils.userSession.getLoginFirst().equals("0")) {
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
