package com.headrun.evidyaloka.activity.fcm;

import android.content.ComponentCallbacks;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.headrun.evidyaloka.evdservices.ChangeSessionStatusService;
import com.headrun.evidyaloka.utils.UserSession;

/**
 * Created by sujith on 10/2/17.
 */
public class EVDFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = EVDFirebaseInstanceIDService.class.getSimpleName();

    // Utils utils;

    public EVDFirebaseInstanceIDService() {

        // utils = new Utils(getApplicationContext());
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Log.i(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);

        //utils.showLog(TAG, "reggister call back" + callback.toString(), Config.ARSFirebaseInstanceIDService);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        //Log.i(TAG, "send to server Refreshed token: " + token);

        UserSession userSession = new UserSession(getApplicationContext());
        userSession.setOLDFCMToken(userSession.getNEWFCMToken());
        userSession.setNEWFCMToken(token);
        if (!userSession.getNEWFCMToken().isEmpty())
            getApplicationContext().startService(new Intent(getApplicationContext(), ChangeSessionStatusService.class)
                    .putExtra("request_type", "fcm"));

    }


}

