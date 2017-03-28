package com.headrun.evidyaloka.activity.self_evaluation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.activity.base.HomeActivity;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.SelfEval;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by sujith on 20/3/17.
 */

public class SelfEvaluationPresenter {

    SelfEvaluationView mSelfEvaluationView;
    Context mContext;


    public SelfEvaluationPresenter(SelfEvaluationView SE_VIEW, Context mContext) {
        mSelfEvaluationView = SE_VIEW;
        this.mContext = mContext;
    }

    public void sEServerCall(LinkedHashMap<String, String> params) {

        mSelfEvaluationView.showProgressBar();

        HashMap<String, String> finalParams = new HashMap<>();

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

        finalParams.put("role", Constants.SELF_VAL_ONBOARD.get(Constants.SE).toString().replaceAll("\\[|\\]|\\s", ""));

        new CallServer(finalParams);
    }

    public void contentDevSelf_eval(LinkedHashMap<String, String> params) {


        params.put("role", "Content Developer");

        new CallServer(params);
    }


    public class CallServer implements ResponseListener<SelfEval> {

        public CallServer(HashMap<String, String> params) {
            mSelfEvaluationView.showProgressBar();
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
            }

        }

    }

}
