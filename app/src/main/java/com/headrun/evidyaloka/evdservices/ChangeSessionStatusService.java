package com.headrun.evidyaloka.evdservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.headrun.evidyaloka.core.EVDNetowrkServices;
import com.headrun.evidyaloka.core.ResponseListener;
import com.headrun.evidyaloka.dto.ChangeSessionStatus;
import com.headrun.evidyaloka.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sujith on 16/2/17.
 */

public class ChangeSessionStatusService extends Service implements ResponseListener<ChangeSessionStatus> {

    private String TAG = ChangeSessionStatus.class.getSimpleName();
    Utils utils;
    private String session_id;
    private HashMap<String, String> session_status = new HashMap<>();
    private String request_type = "request_type";
    private String session_type = "session_status";
    private String update_fcm = "fcm";
    private String ORIENTATION = "orientation";
    private String SET_REQ_TYPE = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public ChangeSessionStatusService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new Utils(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Log.i(TAG, "resonse is change service");

            if (intent != null) {

                SET_REQ_TYPE = intent.getStringExtra(request_type);
                if (SET_REQ_TYPE != null && !SET_REQ_TYPE.isEmpty())
                    if (SET_REQ_TYPE.contains(session_type)) {
                        getSessionCall();
                    } else if (SET_REQ_TYPE.contains(update_fcm)) {
                        fcmServiceCall();
                    } else if (SET_REQ_TYPE.contains(ORIENTATION)) {
                        onBoardingCall();
                    }
            }

        }

        return START_STICKY;
    }

    private void onBoardingCall() {
        new EVDNetowrkServices().orientationStatus(this, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(ChangeSessionStatus response) {

        Log.i(TAG, "resonse is" + response.status);


        if (response.status != null && !response.status.isEmpty() && response.status.toLowerCase().equals("success"))
            if (SET_REQ_TYPE.contains(session_type)) {
                if (session_id != null) {
                    utils.userSession.removeChangeSessionStatus(session_id);
                }
                getSessionCall();
            } else if (SET_REQ_TYPE.contains(update_fcm)) {
                Log.i(TAG, "updated fcm");

            } else if (SET_REQ_TYPE.contains(ORIENTATION)) {
                Log.i(TAG, "orientation updated");

            }
    }

    private void getSessionCall() {

        Log.i(TAG, "call service");
        session_status = utils.userSession.getChangeSessionStatus();
        if (session_status.size() > 0) {
            Map.Entry<String, String> entry = session_status.entrySet().iterator().next();
            session_id = entry.getKey().replaceAll("\\{|\\}|\\s", "");
            String status = entry.getValue().replaceAll("\\{|\\}|\\s", "");

            try {
                new EVDNetowrkServices().sessionStatusChange(this, this, session_id, status);
            } catch (Exception e) {
                e.printStackTrace();
                onDestroy();
            }
        } else {

            utils.userSession.setChangeSessionStatu(session_status);
            onDestroy();
        }
    }


    private void fcmServiceCall() {

        try {
            if (!utils.userSession.getNEWFCMToken().isEmpty())
                new EVDNetowrkServices().fcmTokenRefresh(this, this, utils.userSession.getOLDFCMToken(), utils.userSession.getNEWFCMToken());
        } catch (Exception e) {
            e.printStackTrace();
            onDestroy();
        }
    }

}
