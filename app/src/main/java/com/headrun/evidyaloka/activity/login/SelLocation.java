package com.headrun.evidyaloka.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.EvdApplication;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.config.Constants;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.model.LocData;

/**
 * Created by sujith on 2/3/17.
 */

public class SelLocation implements ResponseListener<LocData> {

    Context mContext;
    String type;

    public SelLocation(Context mContext) {
        this.mContext = mContext;
    }

    public void getLocationData(String type, String id) {
        this.type = type;
        new EVDNetowrkServices().getLocationsData(mContext, this, type, id);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(LocData response) {

        if (response.loc_data != null)
            if (Constants.COUNTRY.contains(type))
                Constants.COUNTRY_MAP = response.loc_data;
            else if (Constants.STATE.contains(type))
                Constants.STATE_MAP = response.loc_data;
            else if (Constants.CITY.contains(type))
                Constants.CITY_MAP = response.loc_data;
    }
}
