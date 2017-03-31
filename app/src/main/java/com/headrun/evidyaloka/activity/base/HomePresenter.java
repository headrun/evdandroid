package com.headrun.evidyaloka.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.SelectionDiscussion.SelectionDiscussionActivity;
import com.headrun.evidyaloka.activity.auth.LoginActivity;
import com.headrun.evidyaloka.activity.profileUpdate.ProfileUpdate;
import com.headrun.evidyaloka.activity.self_evaluation.SelfEvaluationActivity;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.LoginResponse;
import com.headrun.evidyaloka.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sujith on 22/3/17.
 */

public class HomePresenter {

    HomeView mHomeView;
    Context mContext;
    Utils utils;

    public HomePresenter(HomeView mHomeView, Context mContext) {
        this.mHomeView = mHomeView;
        this.mContext = mContext;
        utils = new Utils(mContext);
    }

    public void setSession() {

        if (utils.userSession.getIsLogin())
            mHomeView.enableSession();
        else
            mHomeView.disableSession();
    }

    public LinkedHashMap<String, Drawable> setGalleryImages() {

        LinkedHashMap<String, Drawable> gallery_img = new LinkedHashMap<>();
        Constants.SELF_VAL_ONBOARD.clear();
        if (utils.userSession.getIsLogin()) {

            LoginResponse.userData userdata = utils.userSession.getUserData().data;
            if (userdata != null) {

                if (userdata.profile_completion_status != null && userdata.profile_completion_status == false) {
                    gallery_img.put(Constants.PROFILE_UPDATE, getDrawable(R.drawable.update_profile));
                }

                if (userdata.evd_rep != null && userdata.evd_rep == false)
                    gallery_img.put(Constants.ORIENTAION, getDrawable(R.drawable.orientaionn2));

                List<LoginResponse.Role> user_roles = utils.getUserRoles();
                if (user_roles.size() > 0) {
                    for (LoginResponse.Role entry : user_roles) {

                        if (entry.self_eval_status != null && entry.self_eval_status == false) {
                            gallery_img.put(Constants.SE, getDrawable(R.drawable.se));
                            if (Constants.SELF_VAL_ONBOARD.containsKey(Constants.SE)) {
                                LinkedList<String> values = Constants.SELF_VAL_ONBOARD.get(Constants.SE);
                                values.add(entry.role);
                                Constants.SELF_VAL_ONBOARD.put(Constants.SE, values);
                            } else {
                                LinkedList<String> list_val = new LinkedList<>();
                                list_val.add(entry.role);
                                Constants.SELF_VAL_ONBOARD.put(Constants.SE, list_val);
                            }
                        }

                        if (entry.tsd_status != null && entry.tsd_status == false) {
                            gallery_img.put(Constants.TSD, getDrawable(R.drawable.tsd));

                            if (Constants.SELF_VAL_ONBOARD.containsKey(Constants.TSD)) {
                                LinkedList<String> values = Constants.SELF_VAL_ONBOARD.get(Constants.TSD);
                                values.add(entry.role);
                                Constants.SELF_VAL_ONBOARD.put(Constants.TSD, values);
                            } else {
                                LinkedList<String> list_val = new LinkedList<>();
                                list_val.add(entry.role);
                                Constants.SELF_VAL_ONBOARD.put(Constants.TSD, list_val);
                            }
                        }
                    }
                }
            }
        }

        return gallery_img;
    }

    public Drawable getDrawable(int img) {
        return ContextCompat.getDrawable(mContext, img);
    }


    public void navigateGallery(String value) {

        if (utils.userSession.getIsLogin()) {
            if (Constants.PROFILE_UPDATE.toLowerCase().equals(value)) {
                mContext.startActivity(new Intent(mContext, ProfileUpdate.class));
                ((Activity) mContext).finish();
            } else if (Constants.ORIENTAION.toLowerCase().equals(value)) {
                mHomeView.inflateVideoPlayer("DhmVpNCEWOo");
            } else if (Constants.SE.toLowerCase().equals(value)) {
                List<String> list = utils.LinkedListTOArrayList(Constants.SELF_VAL_ONBOARD.get(Constants.SE));
                mContext.startActivity(new Intent(mContext, SelfEvaluationActivity.class).
                        putStringArrayListExtra("role_type", (ArrayList<String>) list));

                ((Activity) mContext).finish();
            } else if (Constants.TSD.toLowerCase().equals(value)) {
                mContext.startActivity(new Intent(mContext, SelectionDiscussionActivity.class));
            }

        } else {

            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            ((Activity) mContext).finish();
        }
    }

    public void setGallery() {

        if (utils.userSession.getIsLogin()) {
            new callProfile();
        } else {
            mHomeView.hideGallery();
        }
    }


    public class callProfile implements ResponseListener<LoginResponse> {

        public callProfile() {

            new EVDNetowrkServices().getUserData(mContext, this);

        }

        @Override
        public void onErrorResponse(VolleyError error) {

            mHomeView.setGalleryImages();
        }

        @Override
        public void onResponse(LoginResponse response) {

            if (response.status == 0) {
                utils.userSession.setUserData(response);
            }
            mHomeView.setGalleryImages();
        }
    }

}
