package com.headrun.evidyaloka.activity.homePage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.BaseEVDFragment;
import com.headrun.evidyaloka.utils.NetworkUtils;
import com.squareup.okhttp.internal.Util;

public class InfluencerFragment extends BaseEVDFragment implements View.OnClickListener {

    private TextInputEditText email;
    private TextInputEditText mobile;
    private Button enroll;

    public static InfluencerFragment newInstance() {
        return new InfluencerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.influencer_fragment, container, false);
        email = view.findViewById(R.id.input_email);
        mobile = view.findViewById(R.id.input_mobile);
        enroll = view.findViewById(R.id.enroll);
        enroll.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enroll:
                String enteredEmail=email.getText().toString();
                String enteredMobile=mobile.getText().toString();

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()){
                    email.setError(getString(R.string.enter_valid_email));
                }else if(enteredMobile.length()!=10){
                    mobile.setError("Enter valid mobile no.");
                }else {
                    if(NetworkUtils.isNetworkConnected(getActivity().getApplicationContext())){
                        //TODO api to send email and mobile
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        }
    }
}
