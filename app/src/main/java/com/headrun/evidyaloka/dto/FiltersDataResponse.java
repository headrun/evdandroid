package com.headrun.evidyaloka.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sujith on 19/1/17.
 */

public class FiltersDataResponse {

    public static String TAG = "FiltersDataResponse";

    @SerializedName("languages")
    public String[] languages;
    @SerializedName("states")
    public String[] states;
    @SerializedName("sessions_filters")
    public String[] sessions_filters;
    @SerializedName("roles")
    public HashMap<String, String>[] user_roles;

}
