package com.headrun.evidyaloka.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.headrun.evidyaloka.EvdApplication;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.demands.BaseEVDFragment;
import com.headrun.evidyaloka.activity.profileUpdate.ProfileUpdate;
import com.headrun.evidyaloka.config.ApiEndpoints;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.utils.ImageLoadingUtils;
import com.headrun.evidyaloka.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sujith on 24/2/17.
 */

public class ProfileFragment extends BaseEVDFragment {

    private String TAG = ProfileFragment.class.getSimpleName();
    private SimpleDraweeView user_pic;
    private TextView logout, txt_user_name, edit_profile;
    private Utils utils;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_frament_lay, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user_pic = (SimpleDraweeView) view.findViewById(R.id.user_pic);
        logout = (TextView) view.findViewById(R.id.log_out);
        txt_user_name = (TextView) view.findViewById(R.id.txt_user_name);
        edit_profile = (TextView) view.findViewById(R.id.edit_profile);

        utils = new Utils(getActivity());

        if (utils.userSession.getIsLogin()) {
            edit_profile.setVisibility(View.VISIBLE);
        } else {
            edit_profile.setVisibility(View.GONE);
        }

        setProfile();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutuser();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ProfileUpdate.class));

            }
        });


    }


    private void setProfile() {
        try {
            LoginResponse user_data = utils.userSession.getUserData();
            if (user_data != null) {
                //profile_lay.setVisibility(View.VISIBLE);

                if (user_data.data.picture != null && !user_data.data.picture.isEmpty()) {
                    ImageLoadingUtils.load(user_pic, user_data.data.picture);
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
                    user_pic.getHierarchy().setRoundingParams(roundingParams);
                } else {
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
                    user_pic.getHierarchy().setRoundingParams(roundingParams);
                }

                if (user_data.data.username != null && !user_data.data.username.isEmpty()) {
                    txt_user_name.setText("Hi " + user_data.data.username);
                } else {
                    txt_user_name.setText("Hi Guset");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void logoutuser() {

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiEndpoints.SIGNOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        utils.userSession.clearCookie();
                        hideProgressDialog();
                        setProfile();
                        removeData();
                        startActivity(new Intent(getActivity(), LoginActivity.class)
                                .putExtra(Constants.TYPE, true)
                                .putExtra(Constants.REDIRECT_TO, "home_page"));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                // utils.showProgressBar(false, progress_bar);
                if (error instanceof NetworkError) {
                    utils.showAlertDialog(getResources().getString(R.string.no_internet));
                } else {
                    utils.showAlertDialog(getResources().getString(R.string.faceing_issue));
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put("csrfmiddlewaretoken", utils.userSession.getCsrf());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return new EVDNetowrkServices().getSessionHeaders(getActivity());
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                new Utils().setHeaders(getActivity(), response);
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setTag(TAG);

        EvdApplication.getInstance().addToRequestQueue(stringRequest);
    }


    private void removeData() {

        Constants.LIST_SESSIONS.clear();
        Constants.LIST_DEMANDS.clear();

    }
}
