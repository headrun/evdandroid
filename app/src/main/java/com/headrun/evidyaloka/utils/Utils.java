package com.headrun.evidyaloka.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.core.RequestErrorActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP-HP on 27-03-2016.
 */

public class Utils {

    private static int screenWidth = 0;
    private static int screenHeight = 0;
    public Context mContext;
    public UserSession userSession;

    public Utils(Context mContext) {
        this.mContext = mContext;
        userSession = new UserSession(mContext);
    }

    public Utils() {
        userSession = new UserSession();
    }

    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static String convertTime(String date) {

        String convert_time = "";
        if (!date.isEmpty()) {

            SimpleDateFormat date_formate = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat convert_formate = new SimpleDateFormat("hh:mm a");

            try {
                Date d1 = date_formate.parse(date);
                convert_time = convert_formate.format(d1);
                return convert_time;
            } catch (ParseException e) {
                e.printStackTrace();
                return convert_time;
            }

        }
        return convert_time;
    }

    public void setHeaders(Context mContext, NetworkResponse response) {

        try {
            Map<String, String> responseHeaders = response.headers;
            UserSession userSession = new UserSession(mContext);

            if (responseHeaders.containsKey("Set-Cookie")) {
                userSession.setCookie(responseHeaders.get("Set-Cookie"));
            }

            String csrf = getCookieValue(mContext, "csrftoken");
            if (!csrf.isEmpty())
                userSession.SetCsrf(csrf);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCookieValue(Context mContext, String value) {

        try {
            String[] Cookie_value = new UserSession(mContext).getCookie().split(";");
            for (String session : Cookie_value) {
                if (session.contains(value)) {
                    String[] data = session.split("=");
                    if (data.length > 0)
                        return data[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public void volleyError(VolleyError error, Context mContext) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            String data = new String(error.networkResponse.data);
            mContext.startActivity(new Intent(mContext, RequestErrorActivity.class).
                    putExtra("error", data));
        } else {
            Log.i(mContext.toString(), "login error");
        }
    }

    public void showProgressBar(boolean value, ProgressBar progressbar) {

        if (value) {
            progressbar.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.GONE);
        }
    }

    public void showAlertDialog(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String error_msg = msg;

        builder.setTitle(R.string.app_name);
        builder.setMessage(error_msg);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public HashMap<String, String> HashMapToString(String value) {

        if (value != null && !value.isEmpty() && value.length() > 0) {

            HashMap<String, String> status_data1 = new HashMap<>();
            try {
                String split_data = value.replaceAll("\\{|\\}|\\s", "");

                if (!split_data.trim().isEmpty()) {
                    String[] data1 = split_data.split(",");

                    if (data1.length > 0) {
                        for (int i = 0; i < data1.length; i++) {
                            String[] data2 = data1[i].split("=");
                            status_data1.put(data2[0].replaceAll("\\{|\\}|\\s", ""), data2[1]).replaceAll("\\{|\\}|\\s", "");
                        }
                    } else {
                        String[] data2 = value.replaceAll("\\{|\\}|\\s", "").split("=");
                        if (data2.length > 0)
                            status_data1.put(data2[0].replaceAll("\\{|\\}|\\s", ""), data2[1]).replaceAll("\\{|\\}|\\s", "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status_data1;
        }
        return new HashMap<String, String>();
    }

}
