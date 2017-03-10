package com.headrun.evidyaloka.activity.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.headrun.evidyaloka.R;

/**
 * Created by sujith on 7/3/17.
 */

public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_content, new LoginFragment())
                .commit();
    }

}
