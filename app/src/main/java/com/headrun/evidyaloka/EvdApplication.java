package com.headrun.evidyaloka;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.headrun.evidyaloka.core.OkHttpStack;
import io.fabric.sdk.android.Fabric;


/**
 * Created by HP-HP on 27-03-2016.
 */
public class EvdApplication extends Application {

    public static final String TAG = EvdApplication.class.getSimpleName();

    private static EvdApplication _instance;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
       /* Fabric.with(this, new Crashlytics());


        Fresco.initialize(this);*/

        _instance = this;
    }

    public static EvdApplication getInstance() {
        return _instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
