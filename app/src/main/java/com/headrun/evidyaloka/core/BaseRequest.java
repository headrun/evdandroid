package com.headrun.evidyaloka.core;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.headrun.evidyaloka.dto.DemandResponse;
import com.headrun.evidyaloka.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Created by HP-HP on 27-03-2016.
 */
public class BaseRequest<T> extends Request<T> {

    private GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
    private String TAG = BaseRequest.class.getSimpleName();

    private Map<String, String> params;
    private Map<String, String> headers;
    private ResponseListener<T> listener;
    private TypeToken<T> typeToken;
    private Gson gson = gsonBuilder.create();
    private Context mContext;

    public BaseRequest(int method, String url, Map<String, String> headers, Map<String, String> params, TypeToken<T> typeToken, ResponseListener<T> listener) {
        super(method, url, listener);
        this.headers = headers;
        this.params = params;
        this.typeToken = typeToken;
        this.listener = listener;
        // Log.i("Base request", url);
        setRetryPolicy(new DefaultRetryPolicy(5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public BaseRequest(Context mContext, int method, String url, Map<String, String> headers, Map<String, String> params, TypeToken<T> typeToken, ResponseListener<T> listener) {
        super(method, url, listener);
        this.headers = headers;
        this.params = params;
        this.typeToken = typeToken;
        this.listener = listener;
        this.mContext = mContext;
        //Log.i("Base request", url);

        setRetryPolicy(new DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {

        T response = null;
        String jsonResponse = "";
        try {

            if (!new Utils(mContext).userSession.getIsLogin())
                new Utils().setHeaders(mContext, networkResponse);
            jsonResponse = new String(networkResponse.data,
                    HttpHeaderParser.parseCharset(networkResponse.headers));

            Log.i(TAG, "url is" + getUrl() + " repsonse is " + jsonResponse);

            if (jsonResponse.startsWith("[")) {
                String data = jsonResponse.substring(1, jsonResponse.length() - 1);
                response = gson.fromJson(data, typeToken.getType());
            } else {
                response = gson.fromJson(jsonResponse, typeToken.getType());
            }
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(T t) {
        if (listener != null) {
            listener.onResponse(t);
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        //params.put("api_key", BuildConfig.MOVIEDB_API);
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

}
