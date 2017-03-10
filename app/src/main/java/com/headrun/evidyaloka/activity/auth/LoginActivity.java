package com.headrun.evidyaloka.activity.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.headrun.evidyaloka.activity.demand_slots.DemandSlotActivity;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.BaseActivity;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.evdservices.ChangeSessionStatusService;
import com.headrun.evidyaloka.model.Demand;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.model.SchoolDetails;
import com.headrun.evidyaloka.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by sujith on 30/1/17.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, ResponseListener<LoginResponse> {

    private String TAG = LoginActivity.class.getSimpleName();
    private TextView title_question, title_type, title_txt, error_status;
    private TextInputLayout txt_confirm_pass;
    private LinearLayout signup_lay1;
    private String demand_id = "";
    private TextInputEditText input_username, input_pass, input_confirm_pass, input_firtst_name, input_last_name;
    private Button login_btn;
    private boolean Skip_enable = true;
    private String REDIRECT_TO = "";
    Menu menu_confirm;

    private Button loginButton;
    private com.google.android.gms.common.SignInButton google_login_button;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_login);

        try {
            intitView();
            getData();

            //getToken();

        } catch (Exception e) {
            Log.i(TAG, "login exception");
            e.printStackTrace();
        }
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null)
                Skip_enable = data.getBoolean(Constants.TYPE);
            REDIRECT_TO = data.getString(Constants.REDIRECT_TO);
            demand_id = data.getString(Constants.ID);
            hideSkip();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void hideSkip() {
        if (Skip_enable) {
            menu_confirm.findItem(R.id.action_skip)
                    .setEnabled(true);
        } else {
            menu_confirm.findItem(R.id.action_skip)
                    .setEnabled(false);
        }
    }

    private void intitView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title_question = (TextView) findViewById(R.id.title_question);
        title_txt = (TextView) findViewById(R.id.title_txt);
        title_type = (TextView) findViewById(R.id.title_type);
        error_status = (TextView) findViewById(R.id.error_status);
        input_username = (TextInputEditText) findViewById(R.id.input_username);
        input_firtst_name = (TextInputEditText) findViewById(R.id.input_firtst_name);
        input_last_name = (TextInputEditText) findViewById(R.id.input_last_name);
        input_pass = (TextInputEditText) findViewById(R.id.input_pass);
        input_confirm_pass = (TextInputEditText) findViewById(R.id.input_confirm_pass);
        txt_confirm_pass = (TextInputLayout) findViewById(R.id.txt_confirm_pass);
        login_btn = (Button) findViewById(R.id.login_btn);
        loginButton = (Button) findViewById(R.id.fb_login_button);
        signup_lay1 = (LinearLayout) findViewById(R.id.signup_lay1);
        google_login_button = (com.google.android.gms.common.SignInButton) findViewById(R.id.google_login_button);
        google_login_button.setSize(SignInButton.SIZE_STANDARD);
        google_login_button.setVisibility(View.GONE);
        setActivityTitle("");
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_confirm_pass.setVisibility(View.GONE);
        login_btn.setOnClickListener(this);
        title_type.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        google_login_button.setOnClickListener(this);

        loginVisible();

    }

    private void setTabLayout(int title) {
        title_txt.setText(title);

    }

    @Override
    protected void onDestroy() {
        /*if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }*/
        super.onDestroy();
        //accessTokenTracker.stopTracking();
        //profileTracker.stopTracking();
    }

    public void getFilerData(String email, String pwd, String confirm_pwd, String token) {

        if (!userSession.getCsrf().isEmpty()) {

            HashMap<String, String> params = new HashMap<>();
            String login_type = utils.userSession.getSelLoginType();
            if (login_type.isEmpty()) {
                params.put("username", email);
                params.put("password", pwd);
            } else if (login_type.equals("fb")) {
                params.put("backend", "facebook");
                params.put("access_token", token);
            } else if (login_type.equals("google")) {
                params.put("backend", "google");
                params.put("access_token", token);
            }

            if (!login_type.isEmpty() || txt_confirm_pass.getVisibility() == View.GONE) {
                showProgressDialog();
                new EVDNetowrkServices().login_authentication(LoginActivity.this, this, params);
            }

        }

    }


    private void setEmptyFields() {
        input_confirm_pass.setText("");
        input_pass.setText("");
        input_username.setText("");
        input_firtst_name.setText("");
        input_last_name.setText("");
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.login_btn) {

            String email = input_username.getText().toString().trim();
            String pwd = input_pass.getText().toString().trim();

            userSession.setSelLoginType("");

            if (txt_confirm_pass.getVisibility() != View.VISIBLE) {
                if (validcheck(email, pwd, "", "", ""))
                    getFilerData(email, pwd, "", "");

            } else {
                String confm_pwd = input_confirm_pass.getText().toString().trim();
                String first_name = input_firtst_name.getText().toString().trim();
                String last_name = input_last_name.getText().toString().trim();
                if (validcheck(email, pwd, confm_pwd, first_name, last_name)) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("first_name", first_name);
                    params.put("last_name", last_name);
                    params.put("email", email);
                    params.put("username", email);
                    params.put("password", pwd);
                    new EVDNetowrkServices().sigup_auth(LoginActivity.this, this, params);
                }
            }
        } else if (id == R.id.title_type) {

            boolean login = true;
            if (title_txt.getText().toString().equalsIgnoreCase("login")) {
                login = false;
            }
            setEmptyFields();
            if (login) {
                loginVisible();
            } else {
                signupVisible();
            }
        } else if (id == R.id.fb_login_button) {
            fbLoginRegister();
            if (AccessToken.getCurrentAccessToken() == null && Profile.getCurrentProfile() == null) {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "user_status", "user_about_me", "user_birthday",
                        "user_videos", "user_events", "public_profile"));
                // LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this, Arrays.asList("rsvp_event", "publish_actions"));

            } else {
                Log.i(TAG, "  alredy got acess token");
                /*LoginManager.getInstance().logOut();*/
                userSession.setSelLoginType("fb");
                getFilerData("", "", "", AccessToken.getCurrentAccessToken().getToken());
            }
        } else if (id == R.id.google_login_button) {
            googleSettings();
            googleSignIn();
        }

    }


    private boolean validcheck(String email, String pwd, String confm_pwd, String first_name, String last_name) {

        boolean loginvalid = true;
        if (email.trim().isEmpty()) {
            loginvalid = false;
            input_username.setError(getString(R.string.email_err));
            return loginvalid;
        } else if (pwd.trim().isEmpty()) {
            loginvalid = false;
            input_pass.setError(getString(R.string.error_pwd));
            return loginvalid;
        }

        if (txt_confirm_pass.getVisibility() == View.VISIBLE) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginvalid = false;
                input_username.setError(getString(R.string.email_err));
                return loginvalid;
            } else if (confm_pwd.trim().isEmpty()) {
                loginvalid = false;
                input_confirm_pass.setError(getString(R.string.error_pwd));
                return loginvalid;
            } else if (!pwd.trim().equals(confm_pwd.trim())) {
                loginvalid = false;
                input_confirm_pass.setError(getString(R.string.pwd_match_error));
                return loginvalid;
            } else if (first_name.trim().isEmpty()) {
                loginvalid = false;
                input_firtst_name.setError(getString(R.string.mising_first_name));
                return loginvalid;
            } else if (last_name.trim().isEmpty()) {
                loginvalid = false;
                input_last_name.setError(getString(R.string.mising_last_name));
                return loginvalid;
            }
        }
        return loginvalid;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressDialog();

        new Utils().volleyError(error, LoginActivity.this);
        //parseVolleyError(error);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        menu_confirm = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_skip:
                startActivity(new Intent(this, HomeActivity.class));
            case android.R.id.home:
                startActivity(new Intent(this, HomeActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(LoginResponse response) {
        hideProgressDialog();

        if (response != null) {
            Log.i(TAG, "login sucess");
            if (response.status == 0) {
                //Constants.LIST_DEMANDS.clear();
                utils.userSession.setUserData(response);
                finish();
                redirctToAtivty();
            } else {
                utils.showAlertDialog(response.message);
            }
        }
    }

    private void redirctToAtivty() {

        if (REDIRECT_TO.isEmpty()) {
            startActivity(new Intent(this, HomeActivity.class));
        } else if (REDIRECT_TO.equals(Constants.SLOTS_TYPE)) {

            if (Constants.LIST_DEMANDS.containsKey(demand_id)) {
                Demand demand = Constants.LIST_DEMANDS.get(demand_id);
                demand.Scholle_deatils = null;
                Constants.LIST_DEMANDS.put(demand_id, demand);
                startActivity(new Intent(this, DemandSlotActivity.class)
                        .putExtra(Demand.TAG_DEMAND, Constants.LIST_DEMANDS.get(demand_id))
                        .putExtra(SchoolDetails.TAG_SCHOOLDEATILS, demand.Scholle_deatils)
                        .putExtra(Constants.TYPE, false));
                finish();
            } else {
                startActivity(new Intent(this, HomeActivity.class));
            }
        } else if (REDIRECT_TO.equals(Constants.HOME_TYPE)) {
            startActivity(new Intent(this, HomeActivity.class));
        }

        getApplicationContext().startService(new Intent(getApplicationContext(), ChangeSessionStatusService.class)
                .putExtra("request_type", "fcm"));

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void fbLoginRegister() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (loginResult != null && !loginResult.getAccessToken().getToken().isEmpty()) {
                    utils.userSession.setSelLoginType("fb");
                    getFilerData("", "", "", loginResult.getAccessToken().getToken());
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
    }

    private void showAlertDialog(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String error_msg = msg;
        if (msg.isEmpty())
            error_msg = getResources().getString(R.string.login_error);

        builder.setTitle(error_msg);
        builder.setCancelable(false);

        builder.setMessage("You are trying to select more than 2 Slots");
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    protected void googleSignInResult(GoogleSignInResult result) {
        // Log.d(TAG, "googleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            Log.i(TAG, "g login sucess");
            GoogleSignInAccount acct = result.getSignInAccount();

            utils.userSession.setSelLoginType("google");
            getFilerData("", "", "", acct.getIdToken());
        } else {
            Log.i(TAG, "g login fail");
            Toast.makeText(this, " google token is fail", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleSignInResult(result);
        }
    }

    private void loginVisible() {
        setTabLayout(R.string.login);
        title_type.setText("SignUp");
        title_question.setText(R.string.signnup_title);
        login_btn.setText(R.string.login);
        txt_confirm_pass.setVisibility(View.GONE);
        signup_lay1.setVisibility(View.GONE);
       /* input_username.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        input_username.setHint(getResources().getString(R.string.enter_email) + "/" + getResources().getString(R.string.enter_username));*/
        input_username.setFocusable(true);
        input_username.requestFocus();
        input_pass.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    private void signupVisible() {
        setTabLayout(R.string.signup);
        title_type.setText("Login");
        title_question.setText(R.string.login_title);
        login_btn.setText(R.string.signup);
        txt_confirm_pass.setVisibility(View.VISIBLE);
        signup_lay1.setVisibility(View.VISIBLE);

        input_firtst_name.setFocusable(true);
        input_firtst_name.requestFocus();
        /*input_username.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input_username.setHint(getResources().getString(R.string.enter_email));*/
        input_username.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        input_confirm_pass.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (REDIRECT_TO.contains("home_page") || Constants.ISNOTIFICATION) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

    }
}




