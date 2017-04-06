package com.headrun.evidyaloka.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.headrun.evidyaloka.R;
import com.headrun.evidyaloka.core.RequestErrorActivity;
import com.headrun.evidyaloka.model.LoginResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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

    public void trimListItems(List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            items.set(i, items.get(i).trim());
        }
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

    public List<LoginResponse.Role> getUserRoles() {
        if (userSession.getIsLogin() && userSession.getUserData() != null && userSession.getUserData().data != null && userSession.getUserData().data.roles != null)
            return Arrays.asList(userSession.getUserData().data.roles);
        else return new ArrayList<>();

    }

    public LinkedHashMap<Integer, String> getUserRolesList() {
        LinkedHashMap<Integer, String> roles = new LinkedHashMap<Integer, String>();
        if (userSession.getIsLogin() && userSession.getUserData() != null && userSession.getUserData().data != null && userSession.getUserData().data.roles != null) {
            LoginResponse.Role[] roles_data = userSession.getUserData().data.roles;

            for (LoginResponse.Role entry : roles_data) {
                roles.put(entry.role_id, entry.role);
            }
        }

        return roles;
    }

    public HashMap<String, String> HashMapToString(String value) {

        if (value != null && !value.isEmpty() && value.length() > 0) {

            HashMap<String, String> status_data1 = new HashMap<>();
            try {
                String split_data = value.replaceAll("\\{|\\}|\"", "");

                if (!split_data.trim().isEmpty()) {
                    String[] data1 = split_data.split(",");

                    if (data1.length > 0) {
                        for (int i = 0; i < data1.length; i++) {
                            String[] data2 = data1[i].split("=");
                            status_data1.put(data2[0].replaceAll("\\{|\\}|\"", "").trim(), data2[1].replaceAll("\\{|\\}|\"", "").trim());
                        }
                    } else {
                        String[] data2 = value.replaceAll("\\{|\\}|\"", "").split("=");
                        if (data2.length > 0)
                            status_data1.put(data2[0].replaceAll("\\{|\\}|\"", "").trim(), data2[1].replaceAll("\\{|\\}|\"", "").trim());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status_data1;
        }
        return new HashMap<String, String>();
    }

    public HashMap<String, String> stringToMap(String value) {

        if (value != null && !value.isEmpty() && value.length() > 0) {

            HashMap<String, String> status_data1 = new HashMap<>();
            try {
                String split_data = value.replaceAll("\\{|\\}|\"", "");

                if (!split_data.trim().isEmpty()) {
                    String[] data1 = split_data.split(",");

                    if (data1.length > 0) {
                        for (int i = 0; i < data1.length; i++) {
                            String[] data2 = data1[i].split(":");
                            status_data1.put(data2[0].replaceAll("\\{|\\}|\"", "").trim(), data2[1].replaceAll("\\{|\\}|\"", "").trim());
                        }
                    } else {
                        String[] data2 = value.replaceAll("\\{|\\}|\"", "").split(":");
                        if (data2.length > 0)
                            status_data1.put(data2[0].replaceAll("\\{|\\}|\"", "").trim(), data2[1].replaceAll("\\{|\\}|\"", "").trim());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status_data1;
        }
        return new HashMap<String, String>();
    }

    public HashMap<String, String> userRolesMap(HashMap<String, String>[] list) {

        HashMap<String, String> map = new HashMap<>();
        if (list != null)
            for (HashMap<String, String> val : list)
                if (val != null)
                    for (Map.Entry<String, String> entry : val.entrySet())
                        if (entry != null)
                            map.put(entry.getKey().replaceAll("\"", "").trim(), entry.getValue().replaceAll("\"", "").trim());

        return map;
    }


    public void setTextDrawable(TextView mTextview, int left, int top, int right, int bottom) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            mTextview.setCompoundDrawablesWithIntrinsicBounds(left != 0 ? mContext.getResources().getDrawable(left, mContext.getTheme()) : null,
                    top != 0 ? mContext.getResources().getDrawable(top, mContext.getTheme()) : null,
                    right != 0 ? mContext.getResources().getDrawable(right, mContext.getTheme()) : null,
                    bottom != 0 ? mContext.getResources().getDrawable(bottom, mContext.getTheme()) : null
            );
        } else {
            mTextview.setCompoundDrawablesWithIntrinsicBounds(left != 0 ? mContext.getResources().getDrawable(left) : null,
                    top != 0 ? mContext.getResources().getDrawable(top) : null,
                    right != 0 ? mContext.getResources().getDrawable(right) : null,
                    bottom != 0 ? mContext.getResources().getDrawable(bottom) : null
            );

        }
    }

    public HashMap<String, String> reverseMap(HashMap<String, String> list) {

        HashMap<String, String> map = new HashMap<>();
        if (list != null)
            for (Map.Entry<String, String> entry : list.entrySet())
                map.put(entry.getValue().replaceAll("\"", "").trim(), entry.getKey().replaceAll("\"", "").trim());
        return map;
    }

    public HashMap<String, String>[] mapToArrayMap(HashMap<String, String> list) {

        HashMap<String, String> map[] = new HashMap[list.size()];
        if (list != null) {
            int count = 0;
            for (Map.Entry<String, String> entry : list.entrySet()) {
                HashMap<String, String> val = new HashMap<>();
                val.put(entry.getKey().trim(), entry.getValue().trim());
                map[count] = val;
                count++;
            }
        }
        return map;
    }

    public List<String> hashmaptoList(List<HashMap<String, String>> list) {

        List<String> data_list = new LinkedList<>();
        if (list != null)
            for (HashMap<String, String> val : list)
                for (Map.Entry<String, String> entry : val.entrySet())
                    data_list.add(entry.getValue().trim());

        return data_list;
    }

    public int set_statusColor(String status) {

        if (status.toLowerCase().contains("scheduled"))
            return ContextCompat.getColor(mContext, R.color.teach_now);
        else if (status.toLowerCase().contains("started") || status.contains("completed"))
            return ContextCompat.getColor(mContext, R.color.button_color);
        else if (status.toLowerCase().contains("cancelled"))
            return ContextCompat.getColor(mContext, R.color.cancelled);

        return ContextCompat.getColor(mContext, R.color.button_color);

    }


    public PackageInfo getPackageInfo() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }


    }

    public List<String> LinkedListTOArrayList(LinkedList<String> list) {

        List<String> value_list = new ArrayList<>();
        for (String item : list) {
            value_list.add(item);
        }
        return value_list;
    }
}
