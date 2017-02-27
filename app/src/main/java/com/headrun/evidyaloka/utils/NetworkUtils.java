package com.headrun.evidyaloka.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.util.Map;

/**
 * Created by HP-HP on 27-03-2016.
 */
public class NetworkUtils {

    public static String getUrl(String baseUrl, Map<String, String> queryParams) {
        StringBuffer urlBuilder = new StringBuffer();
        urlBuilder.append(baseUrl);
        if (queryParams != null) {
            urlBuilder.append("?");
            int count = 0;
            int size = queryParams.size();
            for (String paramName : queryParams.keySet()) {
                count++;
                urlBuilder.append(paramName + "=" + queryParams.get(paramName));
                if (count != size) {
                    urlBuilder.append("&");
                }
            }
        }

        return urlBuilder.toString();
    }

    /*public static String getUrl(String baseUrl, Map<String, String> queryParams) {
        Uri.Builder urlBuilder = Uri.parse(baseUrl).buildUpon();
        if (queryParams != null) {
            for (String paramName : queryParams.keySet()) {
                urlBuilder.appendQueryParameter(paramName, queryParams.get(paramName));
            }
        }

        return urlBuilder.build().toString();
    }*/

    public static String getUrlData(String baseUrl, Map<String, ?> queryParams) {
        Uri.Builder urlBuilder = Uri.parse(baseUrl).buildUpon();
        if (queryParams != null) {
            for (String paramName : queryParams.keySet()) {
                urlBuilder.appendQueryParameter(paramName, String.valueOf(queryParams.get(paramName)));
            }
        }

        return urlBuilder.build().toString();
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
