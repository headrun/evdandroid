package com.headrun.evidyaloka.activity.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.BaseEVDFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by sujith on 7/3/17.
 */

public class LoginFragment extends BaseEVDFragment implements AuthView, FragmentChanged, View.OnClickListener {

    @BindView(R.id.email_field)
    AutoCompleteTextView email;

    @BindView(R.id.password_field)
    EditText password_field;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.btn_switch_to_signup)
    TextView btn_switch_to_signup;

    @BindView(R.id.btn_forgot_password)
    TextView btn_forgot_password;

    @BindView(R.id.btn_sign_in_with_google)
    ImageView btn_sign_in_with_google;

    @BindView(R.id.btn_sign_in_with_facebook)
    ImageView btn_sign_in_with_facebook;

    AuthPresenter mAuthPresenter;
    AuthActivity mAuthActivty;

    public LoginFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);

        ButterKnife.bind(getActivity(), view);

        mAuthPresenter = new AuthPresenter(this, getActivity());
        mAuthActivty = new AuthActivity();

        btn_login.setOnClickListener(this);
        btn_switch_to_signup.setOnClickListener(this);
        btn_forgot_password.setOnClickListener(this);
        btn_sign_in_with_google.setOnClickListener(this);
        btn_sign_in_with_facebook.setOnClickListener(this);

        return view;
    }


    @Override
    public void setError(String msg) {
    }

    @Override
    public void fragmentChanged(Fragment fragment) {

        mAuthActivty.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_login:
                mAuthPresenter.loginValidation(email.getText().toString().trim(), password_field.getText().toString().trim());
                break;
            case R.id.btn_switch_to_signup:
                fragmentChanged(new SignupFragment());
                break;
            case R.id.btn_forgot_password:
                break;
            case R.id.btn_sign_in_with_google:
                mAuthPresenter.signGoogle();
                break;
            case R.id.btn_sign_in_with_facebook:
                mAuthPresenter.signFacebook();
                break;
        }
    }
}
