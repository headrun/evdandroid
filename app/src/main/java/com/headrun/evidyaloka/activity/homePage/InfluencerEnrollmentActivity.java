package com.headrun.evidyaloka.activity.homePage;

import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.EvdApplication;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.BaseActivity;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.utils.NetworkUtils;

import java.util.HashMap;

public class InfluencerEnrollmentActivity extends BaseActivity implements ResponseListener<LoginResponse>, View.OnClickListener {

    private TextInputEditText email;
    private TextInputEditText mobile;
    private Button enroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influencer_enrollment);
        setTitle(R.string.enfluencer_enrollment_title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        email = findViewById(R.id.input_email);
        mobile = findViewById(R.id.input_mobile);
        enroll = findViewById(R.id.enroll);
        enroll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enroll:
                String enteredEmail = email.getText().toString();
                String enteredMobile = mobile.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
                    email.setError(getString(R.string.enter_valid_email));
                } else if (enteredMobile.length() != 10) {
                    mobile.setError("Enter valid mobile no.");
                } else {
                    if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                        //TODO api to send email and mobile
                        HashMap<String, String> params = new HashMap<>();
                        params.put("email", enteredEmail);
                        params.put("phone", enteredMobile);
                        showProgressDialog();
                        new EVDNetowrkServices().enrollInFluencer(this, this, params);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressDialog();
        Toast.makeText(EvdApplication.getInstance(), getString(R.string.network_error_response), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(LoginResponse response) {
        hideProgressDialog();
        if (response != null) {
            Toast.makeText(EvdApplication.getInstance(), response.message, Toast.LENGTH_SHORT).show();

        }
    }
}
