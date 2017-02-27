package com.headrun.evidyaloka.utils;

import android.content.Context;
import android.support.compat.BuildConfig;
import android.util.Log;
import android.widget.Toast;


public class ViewUtils {

    public static void showToast(String message, Context context) {
        showMessageInToast(message, context);
    }

    private static void showMessageInToast(String message, Context ctx) {
        if (ctx != null)
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLog(Context mContext, String message) {
        if (BuildConfig.DEBUG)
            Log.wtf(mContext.toString(), message);
    }


}
