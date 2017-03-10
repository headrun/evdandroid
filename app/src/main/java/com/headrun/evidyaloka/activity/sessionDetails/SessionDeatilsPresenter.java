package com.headrun.evidyaloka.activity.sessionDetails;

import android.content.Context;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.SessionDetailsResponse;
import com.headrun.evidyaloka.model.*;

/**
 * Created by sujith on 6/3/17.
 */

public class SessionDeatilsPresenter {

    SessionDetailsView mSessionview;
    Context montext;

    public SessionDeatilsPresenter(Context montext, SessionDetailsView mSessionview) {
        this.mSessionview = mSessionview;
        this.montext = montext;
    }

    public void callSessionnData(String session_id) {
        if (session_id != null && !session_id.isEmpty())
            new CallSessionData(session_id);
    }

    public class CallSessionData implements ResponseListener<SessionDetailsResponse> {

        public String id;

        public CallSessionData(String id) {
            new EVDNetowrkServices().sessionDeatils(montext, this, id);
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void onResponse(SessionDetailsResponse response) {

            if (response != null) {

                if (response.status != null && response.status.equals("0")) {

                    mSessionview.setSessionData(response.data);
                }
            }
        }
    }

}
