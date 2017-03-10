package com.headrun.evidyaloka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sujith on 19/1/17.
 */

public class FiltersData {

    public static String TAG = "FiltersDataResponse";

    @SerializedName("languages")
    public String[] languages;
    @SerializedName("states")
    public String[] states;
    @SerializedName("sessions_filters")
    public String[] sessions_filters;
    @SerializedName("roles")
    public List<HashMap<String, String>> user_roles;


}
