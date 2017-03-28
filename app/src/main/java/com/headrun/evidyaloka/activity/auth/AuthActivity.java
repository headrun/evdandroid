package com.headrun.evidyaloka.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.activity.profileUpdate.ProfileUpdate;
import com.headrun.evidyaloka.config.Constants;

/**
 * Created by sujith on 7/3/17.
 */

public class AuthActivity extends AppCompatActivity implements FragmentChanged {

    private String TAG = AuthActivity.class.getSimpleName();
    private boolean Skip_enable = true;
    private String REDIRECT_TO = "";
    private String demand_id = "";
    private AuthPresenter mAuthPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);

        getData();
        mAuthPresenter = new AuthPresenter(this, REDIRECT_TO, demand_id);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_content, new LoginFragment())
                .commit();

    }


    @Override
    public void fragmentChanged(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_content, fragment)
                .commit();

    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                Skip_enable = data.getBoolean(Constants.TYPE);
                REDIRECT_TO = data.getString(Constants.REDIRECT_TO);
                demand_id = data.getString(Constants.ID);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuthPresenter.redirctToAtivty();
    }
}
