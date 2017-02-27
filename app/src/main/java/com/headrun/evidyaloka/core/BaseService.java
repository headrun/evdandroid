package com.headrun.evidyaloka.core;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.headrun.evidyaloka.EvdApplication;
import com.headrun.evidyaloka.utils.NetworkUtils;

import java.util.Map;

/**
 * Created by HP-HP on 27-03-2016.
 */
public class BaseService {

    //Opens connection port via GET Method
    protected void executeGetRequest(Context context, String url, Map<String, String> headers, Map<String, String> params, TypeToken typeToken, ResponseListener listener) {
        url = NetworkUtils.getUrl(url, params);
        executeRequest(context, Request.Method.GET, url, headers, params, typeToken, listener);
    }

    //Opens connection port via POST Method
    /*protected void executePostRequest(String url, Map<String, String> headers, Map<String, String> params, TypeToken typeToken, ResponseListener listener) {
        //url = NetworkUtils.getUrl(url, params);
        executeRequest(Request.Method.POST, url, headers, params, typeToken, listener);
    }*/

    protected void executePostRequest(Context context, String url, Map<String, String> headers, Map<String, String> params, TypeToken typeToken, ResponseListener listener) {
        //url = NetworkUtils.getUrl(url, params);

        executeRequest(context, Request.Method.POST, url, headers, params, typeToken, listener);
    }

    /*protected void executeRequest(int method, String url, Map<String, String> headers, Map<String, String> params, TypeToken typeToken, ResponseListener listener) {
        BaseRequest request = new BaseRequest(method, url, getSessionHeaders(), params, typeToken, listener);
        EvdApplication.getInstance().addToRequestQueue(request);
    }*/

    protected void executeRequest(Context context, int method, String url, Map<String, String> headers, Map<String, String> params, TypeToken typeToken, ResponseListener listener) {
        BaseRequest request = new BaseRequest(context, method, url, headers, params, typeToken, listener);
        EvdApplication.getInstance().addToRequestQueue(request);
    }


}
