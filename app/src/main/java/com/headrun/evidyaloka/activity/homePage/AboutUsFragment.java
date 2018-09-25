package com.headrun.evidyaloka.activity.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.utils.Utils;

public class AboutUsFragment extends Fragment implements View.OnClickListener {
    private Button signUp;
    private TextView privacyPolicy;

    public static AboutUsFragment newInstance(){
        return new AboutUsFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.about_us_fragment,container,false);
        signUp=view.findViewById(R.id.sign_up);
        privacyPolicy=view.findViewById(R.id.privacy_policy);

        signUp.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);

        Utils utils=new Utils(getActivity());
        if (utils.userSession.getIsLogin()){
            signUp.setVisibility(View.GONE);
        }else{
            signUp.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_up:
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("signUp",Constants.SIGNUP);
                startActivity(intent);
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(getActivity(),PrivacyPolicyActivity.class));
                break;
        }
    }
}
