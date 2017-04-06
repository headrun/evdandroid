package com.headrun.evidyaloka.activity.self_evaluation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.SelfEval;
import com.headrun.evidyaloka.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.headrun.evidyaloka.utils.EndlessRecyclerView.TAG;

/**
 * Created by sujith on 20/3/17.
 */

public class SelfEvaluationPresenter {

    SelfEvaluationView mSelfEvaluationView;
    Context mContext;
    Utils utils;


    public SelfEvaluationPresenter(SelfEvaluationView SE_VIEW, Context mContext) {
        mSelfEvaluationView = SE_VIEW;
        this.mContext = mContext;
        utils = new Utils(mContext);
    }

    public void sEServerCall(LinkedHashMap<String, String> params, String role_sel) {

        mSelfEvaluationView.showProgressBar();

        HashMap<String, String> finalParams = new HashMap<>();
        HashMap<String, String> final_Params = new HashMap<>();

        if (params.containsKey(Constants.NATIVE_LANG_FLUENT))
            finalParams.put(mContext.getString(R.string.fluent_sel_native_lang).trim(), params.get(Constants.NATIVE_LANG_FLUENT));
        if (params.containsKey(Constants.OTHER_LANG_FLUENT))
            finalParams.put(mContext.getString(R.string.fluent_sel_other_lang).trim(), params.get(Constants.OTHER_LANG_FLUENT));
        if (params.containsKey(Constants.PROVIDE_TIME))
            finalParams.put(mContext.getString(R.string.provide_2hours).trim(), params.get(Constants.PROVIDE_TIME));
        if (params.containsKey(Constants.MOTIVATE_YOU))
            finalParams.put(mContext.getString(R.string.motivate).trim(), params.get(Constants.MOTIVATE_YOU));
        if (params.containsKey(Constants.SCENARIO_TWO))
            finalParams.put(mContext.getString(R.string.scenario_one).trim(), params.get(Constants.SCENARIO_TWO));
        if (params.containsKey(Constants.SCENARIO_THREE))
            finalParams.put(mContext.getString(R.string.scenario_two).trim(), params.get(Constants.SCENARIO_THREE));
        if (params.containsKey(Constants.SCENARIO_FOUR))
            finalParams.put(mContext.getString(R.string.scenario_three).trim(), params.get(Constants.SCENARIO_FOUR));
        if (params.containsKey(Constants.SCENARIO_FIVE))
            finalParams.put(mContext.getString(R.string.scenario_four).trim(), params.get(Constants.SCENARIO_FIVE));
        if (params.containsKey(Constants.SCENARIO_SIX))
            finalParams.put(mContext.getString(R.string.scenario_five).trim(), params.get(Constants.SCENARIO_SIX));


        int role_id = getRoleIds(role_sel);
        if (role_id != -1) {
            final_Params.put("roles", String.valueOf(role_id));
            final_Params.put("form_dump", Arrays.asList(finalParams).toString());
            new CallServer(final_Params);
        } else {
            Toast.makeText(mContext, "it seems an getting error", Toast.LENGTH_LONG).show();
        }
    }

    private List<Integer> getRoleIds(LinkedList<String> user_role_list, boolean content_dev) {
        List<Integer> roles_list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : utils.getUserRolesList().entrySet()) {

            if (content_dev == false && !entry.getValue().equals("Content Developer") && user_role_list.contains(entry.getValue())) {
                roles_list.add(entry.getKey());
            } else if (content_dev && entry.getValue().equals("Content Developer")) {
                roles_list.add(entry.getKey());
            }
        }
        return roles_list;
    }

    private Integer getRoleIds(String role) {

        for (Map.Entry<Integer, String> entry : utils.getUserRolesList().entrySet()) {

            if (entry.getValue().trim().equals(role)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public void contentDevSelf_eval(LinkedHashMap<String, String> params, String role_sel) {

        HashMap<String, String> final_Params = new HashMap<>();


        int role_id = getRoleIds(role_sel);
        if (role_id != -1) {
            final_Params.put("roles", String.valueOf(role_id));
            final_Params.put("form_dump", Arrays.asList(final_Params).toString());
            new CallServer(params);
        } else {
            Toast.makeText(mContext, "it seems an getting error", Toast.LENGTH_LONG).show();
        }
    }

    public class CallServer implements ResponseListener<SelfEval> {

        public CallServer(HashMap<String, String> params) {
            mSelfEvaluationView.showProgressBar();
            Log.i(TAG, "self eval is " + params);
            new EVDNetowrkServices().selfEvalCall(mContext, params, this);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            mSelfEvaluationView.hideProgressBar();
        }

        @Override
        public void onResponse(SelfEval response) {

            mSelfEvaluationView.hideProgressBar();
            if (response.status == 0) {
                mContext.startActivity(new Intent(mContext, HomeActivity.class));
                ((Activity) mContext).finish();
            } else {
                Toast.makeText(mContext, response.message, Toast.LENGTH_SHORT).show();
            }

        }

    }

}
